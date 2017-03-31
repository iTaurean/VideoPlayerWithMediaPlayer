package com.android.lvxin.videoplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.android.lvxin.base.BaseApplication;
import com.android.lvxin.controller.AudioPlayerController;
import com.android.lvxin.controller.BGAudioPlayerController;
import com.android.lvxin.controller.HeartRateAudioController;
import com.android.lvxin.controller.LocalAudioController;
import com.android.lvxin.controller.VideoPlayerController;
import com.android.lvxin.enums.MediaEnum;
import com.android.lvxin.listener.OnCompletedListener;
import com.android.lvxin.listener.OnErrorListener;
import com.android.lvxin.listener.OnPreparedListener;
import com.android.lvxin.model.CachePlayerStateInfo;
import com.android.lvxin.model.GetPlanDetailAck;
import com.android.lvxin.model.MediaFinishInfoBean;
import com.android.lvxin.model.MediaPlayerArgs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MediaPlayerPresenter
 * @Description: 视频播放页业务处理
 * @Author: lvxin
 * @Date: 6/28/16 16:52
 */
public class MediaPlayerPresenter implements MediaPlayerContract.Presenter {

    private static final String TAG = "MediaPlayerPresenter";

    private static final String REST_AUDIO = "misc_audio_3_35";
    private static final int HANDLER_MESSAGE_ID = 11;

    private static MediaPlayerPresenter INSTANCE;

    private MediaPlayerContract.View mView;
    private MediaPlayerArgs mCacheData; // 从前一个页面传过来的参数对象
    private CachePlayerStateInfo cacheInfo; // 缓存数据

    private LocalAudioController mLocalAudioController; // 其他本地音频控制器
    private HeartRateAudioController mHeartRateAudioController; // 心率音频控制器
    private BGAudioPlayerController mBGAudioController; // 背景音控制器
    private AudioPlayerController mAudioController; // 音频播放控制器
    private VideoPlayerController mVideoController; // 视频控制器

    private Context mContext;
    //    private BleManager bleManager;
    private CountdownHandler handler;
    private boolean isSurfaceDestroyed = false;
    private boolean isOnPause = false;
    private MediaPlayerContract.OnViewChangeListener onViewChangeListener;
    private OnCompletedListener videoCompletedListener = new OnCompletedListener() {
        @Override
        public void onCompleted() {
            onMediaPlayCompleted();
        }
    };
    private OnPreparedListener videoPreparedListener = new OnPreparedListener() {
        @Override
        public void onPrepared() {
            Log.i(TAG, "onPrepared: ");
            onMediaPrepared();
        }
    };

    private OnErrorListener videoErrorListener = new OnErrorListener() {
        @Override
        public void onError(int what, int extra) {
            Log.i(TAG, "onError: what=" + what + ", extra=" + extra);
        }
    };


    private OnCompletedListener audioCompletedListener = new OnCompletedListener() {
        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted: ");
            changeShowView(cacheInfo.getCurrentShowView().next());
            cacheInfo.reset();
            prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_START);
        }
    };

    private MediaPlayerPresenter(MediaPlayerContract.View view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    public static MediaPlayerPresenter getInstance(MediaPlayerContract.View view) {
        if (null == INSTANCE) {
            INSTANCE = new MediaPlayerPresenter(view);
        }
        return INSTANCE;
    }

    public void setOnViewChangeListener(MediaPlayerContract.OnViewChangeListener onViewChangeListener) {
        this.onViewChangeListener = onViewChangeListener;
    }

    @Override
    public void changeShowView(MediaEnum.ViewShowStatus showView) {
        cacheInfo.setCurrentShowView(showView);
        if (null != onViewChangeListener) {
            onViewChangeListener.onViewChanged(cacheInfo.getCurrentShowView(), getVideo(cacheInfo.getVideoPlayIndex()));
        }
    }

    @Override
    public void setupData(MediaPlayerArgs args) {
        this.mCacheData = args;
        this.cacheInfo = mCacheData.getCacheInfo();
    }

    @Override
    public void initMedia(SurfaceHolder surfaceHolder) {
        if (emptySource()) {
            return;
        }
        // init video
        initVideo(surfaceHolder);
        // init audio
        initAudio();
        // init background audio
        initBGAudio();
        // init local audio, including rest audio, heart rate audio
        initLocalAudio();
    }

    /**
     * @param surfaceHolder
     */
    public void initVideo(SurfaceHolder surfaceHolder) {
        if (null == mVideoController) {
            mVideoController = VideoPlayerController.getInstance();
            mVideoController.setOnCompletedListener(videoCompletedListener);
            mVideoController.setOnPreparedListener(videoPreparedListener);
            mVideoController.setOnErrorListener(videoErrorListener);
        }
        mVideoController.setDisplay(surfaceHolder);
    }

    /**
     *
     */
    public void initAudio() {
        if (null == mAudioController) {
            mAudioController = AudioPlayerController.getInstance(mContext);
            mAudioController.setOnCompletedListener(audioCompletedListener);
        }
    }

    /**
     *
     */
    public void initBGAudio() {
        if (null == mBGAudioController) {
            mBGAudioController = BGAudioPlayerController.getInstance();
        }
    }

    /**
     *
     */
    public void initLocalAudio() {
        if (null == mLocalAudioController) {
            mLocalAudioController = LocalAudioController.getInstance(mContext);
        }
    }

    @Override
    public void setupMedia() {
        if (emptySource()) {
            return;
        }

        cacheInfo.setNeedHandleHeartRate(true);

        handler = new CountdownHandler(this);
        setupBGAudio();

        GetPlanDetailAck.PlanVideoInfo video = getVideo(cacheInfo.getVideoPlayIndex());
        if (null != video) {
            // 设置进度条
            mView.setupProgressBar(getVideos().size());

            setupAudio();
            setupVideo();
            setupHeartRateAudio();

            // 设置心率相关ui
            checkHeartRateBand();
            //更新进度条
            mView.updateProgressBar(cacheInfo.getCurrentClip(), cacheInfo.getCurrentValue(), video.getMovementTime());
            changeShowView(cacheInfo.getCurrentShowView());
            prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_START);
        }
    }

    /**
     *
     */
    public void setupVideo() {
        mVideoController.updateData(mCacheData.getSchemeId());
    }

    /**
     *
     */
    public void setupAudio() {
        mAudioController.updateData(mCacheData.getSchemeId());
    }

    /**
     *
     */
    public void setupBGAudio() {
//        String dataSource = BaseApplication.getVideoDir()
//                + mCacheData.getSchemeId() + "/" + mCacheData.getBgAudioFileName() + ".mp3";
        mBGAudioController.prepareAndStart("audio_2_74");

    }

    /**
     *
     */
    public void setupHeartRateAudio() {
        if (null == mHeartRateAudioController) {
            mHeartRateAudioController = HeartRateAudioController.getInstance(mContext,
                    mCacheData.getLowestHeartRate(), mCacheData.getHighestHeartRate());
        }
    }

    /**
     * @param filename
     */
    public void setupLocalAudio(String filename) {
        mLocalAudioController.prepare(filename);
    }

    @Override
    public void prepareMedia(MediaEnum.MediaPreparedStatus status) {
        Log.i(TAG, "prepareMedia: media prepared status=" + status);
        cacheInfo.setMediaPreparedStatus(status);

        Log.i(TAG, "prepareMedia: cacheInfo.getCurrentShowView()=" + cacheInfo.getCurrentShowView());
        final GetPlanDetailAck.PlanVideoInfo currentVideo = getVideo(cacheInfo.getVideoPlayIndex());
        switch (cacheInfo.getCurrentShowView()) {
            case ACTION_NAME:
                // for tv demo load local assets
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoController.updateData(currentVideo);
                        mVideoController.prepareAsync();
                    }
                }, 100);
//                mVideoController.updateData(currentVideo);
//                mVideoController.prepareAsync();
                mAudioController.update(currentVideo.getIntroAudios(), 0, true);
                mAudioController.start();
                // 重置左下角圆形进度条
                mView.updateCircleProgress(currentVideo, cacheInfo.getCircleProgressPausePosition());
                break;
            case PREVIEW:
                // 更新动作名称
                mView.updateActionNameText(currentVideo.getPlanVideoName());
                if (mCacheData.isRestoreFromLock()) {
                    mVideoController.updateData(getVideo(cacheInfo.getVideoPlayIndex()));
                    mVideoController.prepareAsync();
                } else {
                    mVideoController.start();
                }
                break;
            case READY:
                mAudioController.update(currentVideo.getOtherAudios().subList(0, 1), cacheInfo.getAudioPauseIndex(), true);
                mAudioController.start();
                break;
            case COUNT_DOWN:
                mAudioController.update(currentVideo.getOtherAudios().subList(1, 2), 0, true);
                startCountDownFor3Seconds();
                mAudioController.start();
                break;
            case PLAY_MEDIA:
                Log.i(TAG, "prepareMedia: PLAY_MEDIA");
                mAudioController.update(currentVideo.getUnitAudios(), cacheInfo.getAudioPauseIndex(), false);
                mVideoController.updateData(currentVideo);
                mVideoController.prepareAsync();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMediaPrepared() {
        Log.i(TAG, "onMediaPrepared: cacheInfo.getMediaPreparedStatus()=" + cacheInfo.getMediaPreparedStatus());
        switch (cacheInfo.getMediaPreparedStatus()) {
            case IS_PREPARED_START:
                if (mCacheData.isRestoreFromLock()) {
                    //解锁屏幕恢复播放
                    onRestoreFromLockScreen();
                    mCacheData.setRestoreFromLock(false);
                } else {
                    if (!isOnPause()) {
                        Log.i(TAG, "onMediaPrepared: !isOnPause()");
                        onStart();
                    }
                }
                break;
            case IS_PREPARED_REST:
                mVideoController.prepareAndRest();
                break;
            case IS_RESUME_START:
                onResume();
                break;
            case IS_RESUME_PAUSE:
                mVideoController.resumeAndPause();
                mAudioController.resumeAndPause();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        // 第一次进入播放页
        cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.IS_PLAYING);
        mVideoController.start();
        if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
            mAudioController.start();
        }
        Log.i(TAG, "onStart: ");
        updateView();

    }

    @Override
    public void onRestoreFromLockScreen() {
        Log.i(TAG, "onRestoreFromLockScreen: ");
        updateView();
        seekToAndStart();
    }

    @Override
    public void seekToAndStart() {
        cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.IS_PLAYING);
        mVideoController.seekToStart(cacheInfo.getVideoPausePosition());
        mAudioController.seekToStart(cacheInfo.getAudioPauseIndex(), cacheInfo.getAudioPausePosition());
    }

    @Override
    public void updateView() {
        GetPlanDetailAck.PlanVideoInfo video = getVideo(cacheInfo.getVideoPlayIndex());
        if (null != video) {
            // 更新动作名称
            mView.updateActionNameText(video.getPlanVideoName());
            // 更新播放进度条
            updateViewForUnit();
        }

        // 更新播放时间
        mView.updatePlayTimeText(cacheInfo.getPlaySeconds());
    }

    /**
     * 更新单元运动时的ui
     */
    private void updateViewForUnit() {

        GetPlanDetailAck.PlanVideoInfo video = getVideo(cacheInfo.getVideoPlayIndex());
        if (null != video) {
            if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
                Log.i(TAG, "updateViewForUnit: start progress bar");
                mView.startProgressBar();
                if (2 == video.getGuideType()) {
                    // guide type 为2时按秒自动更新圆形进度条
                    mView.updateCircleProgressBySecond(video, cacheInfo.getCircleProgressPausePosition());
                } else {
                    // guide type为1时按次数更新圆形进度条，每播完一次视频更新一次
                    mView.updateCircleProgress(video, cacheInfo.getCircleProgressPausePosition());
                }
            } else {

            }
        }
    }

    @Override
    public void onMediaPlayCompleted() {
        // 播放完一组多媒体文件，紧接着准备下一组多媒体文件的播放

        // 累加重复播放的次数
        cacheInfo.setVideoPlayRepeats(1 + cacheInfo.getVideoPlayRepeats());

        GetPlanDetailAck.PlanVideoInfo currentVideo = getVideo(cacheInfo.getVideoPlayIndex());

        // 获取当前播放视频的重复播放次数

        int repeat = (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView())
                ? currentVideo.getPlayTimes() : currentVideo.getIntroPlayTimes();

        if (cacheInfo.getVideoPlayRepeats() >= repeat) {
            //完成指定的重复播放次数后准备下一组多媒体文件
            // 一个动作完成重置缓存的数据
            cacheInfo.reset();
            // for tv: 需要会员的播放完一个动作之后暂停，弹出购买框
            if (1 == mCacheData.getCoast()) {
                mView.showSubscribeDialog();
            }
            setupNextMedia();
        } else {
            // 循环播放当前多媒体文件
            boolean isStartNext = !isSurfaceDestroy() && !isOnPause() && MediaEnum.MediaStatus.IS_PLAYING == cacheInfo.getMediaPlayStatus();
            if (isStartNext) {
                mVideoController.start();
                if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
                    if (1 == currentVideo.getGuideType()) {
                        mView.updateCircleProgress(currentVideo, cacheInfo.getVideoPlayRepeats() + 1);
                    }
                }
            }
        }
    }

    private void setupNextMedia() {
        // 重置视频的重复播放次数
        cacheInfo.setVideoPlayRepeats(0);

        //停止累加播放时间
        mView.pausePlayedTime();
        // 获取上一个单元动作视频播放完的休息时间
        int restTime = getVideo(cacheInfo.getVideoPlayIndex()).getRestTime();

        if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
            calculateMediaIndex(true);
        }

        if (getVideos().size() <= cacheInfo.getVideoPlayIndex()) {
            completedWholeMedia();
        } else {
            GetPlanDetailAck.PlanVideoInfo nextVideo = getVideo(cacheInfo.getVideoPlayIndex());
            if (null != nextVideo) {
                // 更新进度条
                if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
                    mView.updateProgressBar(true, nextVideo.getMovementTime());
                    //设置左下角圆形进度条的最大值
                    int maxProgressValue = (2 == nextVideo.getGuideType()) ? nextVideo.getMovementTime() : nextVideo.getPlayTimes();
                    mView.setupMaxCircleProgress(maxProgressValue);
                    if (0 != restTime) {
                        // 进入休息页面
                        doRest(restTime);
                        return;
                    }
                }
                changeShowView(cacheInfo.getCurrentShowView().next());
                if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
                    prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_START);
                } else {
                    prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_REST);
                }
            }
        }
    }

    @Override
    public void completedWholeMedia() {
        cacheInfo.setNeedHandleHeartRate(false);

        MediaFinishInfoBean bean = new MediaFinishInfoBean();
        bean.setAverageHeartRate(mHeartRateAudioController.getAvgHeartRate());
        bean.setMaxHeartRate(mHeartRateAudioController.getMaxRateValue());
        bean.setMinHeartRate(mHeartRateAudioController.getMinRateValue());
        bean.setEffectiveExerciseDuration(mHeartRateAudioController.getSecondsInAvailable());
        bean.setPlanSecond(cacheInfo.getPlaySeconds());
        bean.setTotalTime(mCacheData.getTotalDuration());
        bean.setPlanId(mCacheData.getSchemeId());

        onRelease();
        mView.showFinishPage(bean);

    }

    @Override
    public void onRestore(SurfaceHolder surfaceHolder) {
        if (null != mVideoController) {
            mVideoController.setDisplay(surfaceHolder);
            switch (cacheInfo.getMediaPlayStatus()) {
                case IS_RESTING:
                    updateRestCountDown();
                    break;
                case IS_PLAYING:
                    Log.i(TAG, "onRestore: IS_PLAYING");
                    restoreMedia(MediaEnum.MediaPreparedStatus.IS_RESUME_START);
                    break;
                case PAUSE:
                    Log.i(TAG, "onRestore: PAUSE");
                    restoreMedia(MediaEnum.MediaPreparedStatus.IS_RESUME_PAUSE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void restoreMedia(MediaEnum.MediaPreparedStatus status) {
        Log.i(TAG, "restoreMedia: media prepared status=" + status);
        cacheInfo.setMediaPreparedStatus(status);
        onMediaPrepared();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        setOnPause(true);
        pauseView();
        pauseMedia();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        setOnPause(false);
        resumeView();
        resumeMedia();
    }

    /**
     * 暂停后继续更新ui
     */
    private void resumeView() {
        mView.showMediaController(false);
        // 处理开场3，2，1，go时的倒计时暂停中恢复
        if (cacheInfo.isCountDown()) {
            resumeCountDown();
        }
        if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
            // 如果guide type为2时，从暂停恢复播放时也要恢复圆形进度条的更新
            resumeCircleProgress();
            mView.resumeProgressBar();
            //恢复播放时长的计时
            mView.updatePlayTimeText(cacheInfo.getPlaySeconds());
        }
    }

    /**
     * 暂停ui
     */
    private void pauseView() {
        // 暂停进度条
        mView.pauseProgressBar();
        // 暂停3，2，1，go的倒计时
        pauseCountDown();
        // 如果guide type为2时，暂停时也要暂停圆形进度条的更新
        pauseCircleProgress();
        // 暂停播放时间
        mView.pausePlayedTime();
    }

    /**
     * 继续播放多媒体文件
     */
    private void resumeMedia() {
        Log.i(TAG, "resumeMedia: ");
        // 继续播放视频
        mVideoController.resume();
        if (MediaEnum.ViewShowStatus.PREVIEW != cacheInfo.getCurrentShowView()) {
            // 继续播放音频
            mAudioController.resumeAndStart();
        }
        // 继续播放背景音乐
        mBGAudioController.resume();
    }

    /**
     * 暂停多媒体文件的播放
     */
    private void pauseMedia() {
        Log.i(TAG, "pauseMedia: ");
        // 暂停多媒体文件
        if (null != mAudioController) {
            mAudioController.pause();
            cacheInfo.setAudioPauseIndex(mAudioController.getCurrentIndex());
            cacheInfo.setAudioPausePosition(mAudioController.getPausePosition());
        }
        if (null != mVideoController) {
            mVideoController.pause();
            cacheInfo.setVideoPausePosition(mVideoController.getPausePosition());
        }
        if (null != mBGAudioController) {
            mBGAudioController.pause();
        }
    }

    @Override
    public void updateRestCountDown() {
        int restTime = cacheInfo.getRestTime();
        cacheInfo.setRestTime(--restTime);
        mView.updateCountdownView(restTime);
    }

    /**
     * 处理开场3，2，1，go时的倒计时暂停
     */
    private void pauseCountDown() {
        if (null != handler && cacheInfo.getCountDownValue() > 0) {
            cacheInfo.setPauseTime(System.currentTimeMillis());
            handler.removeMessages(HANDLER_MESSAGE_ID);
        }
    }

    /**
     * 处理开场3，2，1，go时的倒计时暂停中恢复
     */
    private void resumeCountDown() {
        if (null != handler && cacheInfo.getCountDownValue() >= 0) {
            long during = cacheInfo.getPauseTime() - cacheInfo.getSendMessageTime();
            if (0 < during && during <= 1000) {
                handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1000 - during);
            } else {
                handler.sendEmptyMessage(HANDLER_MESSAGE_ID);
            }
        }
    }

    /**
     * 如果guide type为2时，暂停时也要暂停圆形进度条的更新
     */
    private void pauseCircleProgress() {
        GetPlanDetailAck.PlanVideoInfo video = getVideo(cacheInfo.getVideoPlayIndex());
        if (null != video) {
            if (2 == video.getGuideType()) {
                mView.pauseCircleProgress();
            }
        }
    }

    /**
     * 如果guide type为2（按秒计）时，从暂停恢复播放时也要恢复圆形进度条的更新
     */
    private void resumeCircleProgress() {
        GetPlanDetailAck.PlanVideoInfo video = getVideo(cacheInfo.getVideoPlayIndex());
        if (null != video) {
            if (2 == video.getGuideType()) {
                mView.resumeCircleProgress();
            }
        }
    }

    @Override
    public void playOrPauseController() {
        switch (cacheInfo.getMediaPlayStatus()) {
            case IS_PLAYING:
                Toast.makeText(BaseApplication.getInstance().getApplicationContext(), "暂停", Toast.LENGTH_SHORT).show();
                cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.PAUSE);
                onPause();
                break;
            case PAUSE:
                Toast.makeText(BaseApplication.getInstance().getApplicationContext(), "播放", Toast.LENGTH_SHORT).show();
                cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.IS_PLAYING);
                onResume();
                break;
            default:
                break;
        }
        boolean isPlaying = cacheInfo.getMediaPlayStatus() == MediaEnum.MediaStatus.IS_PLAYING;
        if (MediaEnum.ViewShowStatus.PLAY_MEDIA == cacheInfo.getCurrentShowView()) {
            //更新ui
            float pausePosition = getVideo(cacheInfo.getVideoPlayIndex()).getPlayTimes()
                    * cacheInfo.getVideoPlayRepeats() * 1000 + mVideoController.getCurrentPosition();
            mView.updatePlayPauseView(isPlaying, pausePosition / 1000);
        } else {
            mView.updatePlayPauseView(isPlaying);
        }
    }

    @Override
    public void onNext() {
        // 重置缓存的数据
        cacheInfo.reset();
        pauseView();

        if (emptySource()) {
            return;
        }

        if (getVideos().size() - 1 > cacheInfo.getVideoPlayIndex()) {
            calculateMediaIndex(true);

            // 更新控制层播放/暂停按钮状态
            cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.IS_PLAYING);
            mView.updatePlayPauseView(true, 0);

            mView.updateProgressBar(true, getVideo(cacheInfo.getVideoPlayIndex()).getMovementTime());

            cacheInfo.setCurrentShowView(MediaEnum.ViewShowStatus.ACTION_NAME);
            changeShowView(cacheInfo.getCurrentShowView());
            prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_REST);
        }
    }

    @Override
    public void onPrev() {
        // 重置缓存的数据
        cacheInfo.reset();
        pauseView();

        if (0 < cacheInfo.getVideoPlayIndex()) {
            calculateMediaIndex(false);

            // 更新控制层播放/暂停按钮状态
            cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.IS_PLAYING);
            mView.updatePlayPauseView(true, 0);

            mView.updateProgressBar(false,
                    getVideo(cacheInfo.getVideoPlayIndex()).getMovementTime());

            cacheInfo.setCurrentShowView(MediaEnum.ViewShowStatus.ACTION_NAME);
            changeShowView(cacheInfo.getCurrentShowView());
            prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_REST);
        }
    }

    @Override
    public void doRest(int restTime) {
        cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.IS_RESTING);
        cacheInfo.setRestTime(restTime);
        //休息时不处理心率
        cacheInfo.setNeedHandleHeartRate(false);
        // 播放休息音频
        setupLocalAudio(REST_AUDIO);
        mLocalAudioController.start();
        // 显示休息页面
        mView.showRestPage(true, restTime);

        prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_REST);

    }

    @Override
    public void startAfterRest() {
        // 开始后需要处理心率
        cacheInfo.setNeedHandleHeartRate(true);
        cacheInfo.setMediaPlayStatus(MediaEnum.MediaStatus.IS_PLAYING);
        //隐藏休息页面
        mView.showRestPage(false, 0);

        cacheInfo.setCurrentShowView(MediaEnum.ViewShowStatus.ACTION_NAME);
        changeShowView(cacheInfo.getCurrentShowView());
        prepareMedia(MediaEnum.MediaPreparedStatus.IS_PREPARED_START);
    }

    @Override
    public void skipRest() {
        mView.stopCountdownProgress();
        startAfterRest();
    }

    @Override
    public void updatePlayedTime() {
        int playSeconds = cacheInfo.getPlaySeconds();
        cacheInfo.setPlaySeconds(++playSeconds);
        mView.updatePlayTimeText(playSeconds);

    }

    @Override
    public void showActionDetailPage() {
        if (MediaEnum.ViewShowStatus.PLAY_MEDIA != cacheInfo.getCurrentShowView()
                && MediaEnum.ViewShowStatus.PREVIEW != cacheInfo.getCurrentShowView()) {
            return;
        }
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> picUrls = new ArrayList<>();
        ArrayList<String> actions = new ArrayList<>();
        ArrayList<String> videoUrls = new ArrayList<>();

        for (GetPlanDetailAck.PlanVideoInfo obj : getVideos()) {
            if (!fileNames.contains(obj.getPlanVideoName())) {
                picUrls.add(obj.getPictureUrl());
                fileNames.add(obj.getPlanVideoName());
                actions.add(obj.getActionDesc());
                videoUrls.add(obj.getVideoUrl());
            }
        }
        GetPlanDetailAck.PlanVideoInfo currentVideo = getVideo(cacheInfo.getVideoPlayIndex());
        for (int i = 0; i < fileNames.size(); i++) {
            if (currentVideo.getPlanVideoName().equals(fileNames.get(i))) {
                mView.showActionDetailPage(mCacheData.getSchemeId(), i, fileNames, actions, picUrls,
                        videoUrls);
                break;
            }
        }
    }

    @Override
    public void onRelease() {

        if (null != mVideoController) {
            mVideoController.onRelease();
        }
        mVideoController = null;

        if (null != mAudioController) {
            mAudioController.onRelease();
        }
        mAudioController = null;

        if (null != mBGAudioController) {
            mBGAudioController.onRelease();
        }
        mBGAudioController = null;

        if (null != mLocalAudioController) {
            mLocalAudioController.onRelease();
        }
        mLocalAudioController = null;

        if (null != mHeartRateAudioController) {
            mHeartRateAudioController.onRelease();
        }
        mHeartRateAudioController = null;

        INSTANCE = null;

    }

    @Override
    public void startCountDownFor3Seconds() {
        // 开场3,2,1,go的3秒倒计时
        cacheInfo.setCountDown(true);
        cacheInfo.setCountDownValue(3);
        cacheInfo.setSendMessageTime(System.currentTimeMillis());
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1000);
    }

    @Override
    public void restoreCountDown() {
        // 恢复Intro的3,2,1,go的倒计时
        // 当播放过程中锁屏，重新点亮屏幕时，需要调用改方法
        if (cacheInfo.isCountDown()) {
            if (null != handler && cacheInfo.getCountDownValue() > 0) {
                long during = cacheInfo.getPauseTime() - cacheInfo.getSendMessageTime();
                if (0 < during && during <= 1000) {
                    handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1000 - during);
                } else {
                    handler.sendEmptyMessage(HANDLER_MESSAGE_ID);
                }
            }
        } else {

        }
    }

    @Override
    public MediaPlayerArgs getCacheData() {
        return mCacheData;
    }

    @Override
    public void setCacheData(CachePlayerStateInfo data) {
        this.cacheInfo = data;
    }

    @Override
    public void start(Context context) {
        this.mContext = context;
    }

    @Override
    public List<GetPlanDetailAck.PlanVideoInfo> getVideos() {
        if (null != mCacheData) {
            return mCacheData.getVideos();
        } else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public GetPlanDetailAck.PlanVideoInfo getVideo(int position) {
        List<GetPlanDetailAck.PlanVideoInfo> videos = getVideos();
        if (!videos.isEmpty() && position < videos.size()) {
            return videos.get(position);
        }
        return null;
    }

    @Override
    public boolean emptySource() {
        return getVideos().isEmpty();
    }

    @Override
    public void calculateMediaIndex(boolean isAdd) {
        int currentPlayIndex = cacheInfo.getVideoPlayIndex();
        if (isAdd) {
            cacheInfo.setVideoPlayIndex(++currentPlayIndex);
        } else {
            if (0 < currentPlayIndex) {
                cacheInfo.setVideoPlayIndex(--currentPlayIndex);
            }
        }
    }

    @Override
    public void updateHeartRateView(int heartRate, MediaEnum.BandConnectStatus status) {
        mView.updateRateIcon(status, true);
        if (null != mCacheData) {
            mView.updateRateView(heartRate, mCacheData.getLowestHeartRate(),
                    mCacheData.getHighestHeartRate(), status);
        }
    }

    @Override
    public void checkHeartRateBand() {
        // do nothing
    }

    @Override
    public void checkBluetooth() {
        // do nothing
    }

    @Override
    public void reconnectBand() {
    }

    @Override
    public boolean isOnPause() {
        return isOnPause;
    }

    @Override
    public void setOnPause(boolean isOnPause) {
        this.isOnPause = isOnPause;
    }

    @Override
    public boolean isSurfaceDestroy() {
        return isSurfaceDestroyed;
    }

    @Override
    public void setSurfaceDestroy(boolean isDestroy) {
        isSurfaceDestroyed = isDestroy;
    }

    private MediaPlayerContract.View getView() {
        return mView;
    }

    private CachePlayerStateInfo getCacheInfo() {
        return cacheInfo;
    }

    private void updateSendMsgTime(int countDownValue) {
        cacheInfo.setSendMessageTime(System.currentTimeMillis());
        mView.update3SecondsCountdownText(countDownValue);
    }


    /**
     * 用于处理开场3，2，1，go的语音
     */
    static class CountdownHandler extends Handler {
        WeakReference<MediaPlayerPresenter> mPresenter;

        CountdownHandler(MediaPlayerPresenter presenter) {
            mPresenter = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            MediaPlayerPresenter presenter = mPresenter.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_MESSAGE_ID:
                    int countDownValue = presenter.getCacheInfo().getCountDownValue();
                    presenter.getCacheInfo().setCountDownValue(--countDownValue);

                    if (countDownValue >= 0) {
                        presenter.updateSendMsgTime(countDownValue);
                        sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1055);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
