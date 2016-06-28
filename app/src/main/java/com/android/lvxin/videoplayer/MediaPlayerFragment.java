package com.android.lvxin.videoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.lvxin.R;
import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.util.Tools;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class MediaPlayerFragment extends Fragment
        implements MediaPlayerContract.View, SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = "MediaPlayerFragment";
    private static final String ARG_VIDEOS = "videos";

    private static final int HANDLER_TIMER_DOWN_COUNTER = 1;
    private static final int HANDLER_MEDIA_CONTROLLER = 2;
    private static final int HANDLER_TIME_REMAIN = 3;

    private static MediaPlayerFragment INSTANCE;
    @Bind(R.id.video_player_view)
    SurfaceView mPlayView;
    @Bind(R.id.play_back)
    ImageView mBackBtn;
    @Bind(R.id.play_next)
    ImageView mNextBtn;
    @Bind(R.id.play_start_pause)
    ImageView mStartPauseBtn;
    @Bind(R.id.play_prev)
    ImageView mPrevBtn;
    @Bind(R.id.media_controller)
    RelativeLayout mControllerLayout;
    @Bind(R.id.play_progress)
    MediaProgressBar mProgressBar;


    @Bind(R.id.text_play_action_name)
    TextView mActionName;
    @Bind(R.id.play_attention)
    ImageView mAttentionBtn;
    @Bind(R.id.play_circle_progress)
    CircleProgressBar mCircleProgress;
    @Bind(R.id.play_time_remain)
    TextView mRemainTimeText;
    @Bind(R.id.play_heart_rate_text)
    TextView mHeartRateText;

    @Bind(R.id.layout_rest_countdown)
    RelativeLayout mCountdownLayout;
    @Bind(R.id.rest_countdown)
    CircleProgressBar mCountdownProgress;
    @Bind(R.id.rest_skip)
    TextView mSkipBtn;


    private SurfaceHolder mSurfaceHolder;

    private TimerTaskHandler handler;
    private MediaPlayerContract.Presenter mPresenter;

    public MediaPlayerFragment() {
    }

    public static MediaPlayerFragment getInstance(List<VideoInfo> videoInfos) {
        if (null == INSTANCE) {
            INSTANCE = new MediaPlayerFragment();
            Bundle args = new Bundle();
            args.putSerializable(ARG_VIDEOS, (Serializable) videoInfos);
            INSTANCE.setArguments(args);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            //TODO GET ARGS
        }

        mPresenter.start();
        handler = new TimerTaskHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);

        ButterKnife.bind(this, root);
        initView();

        return root;
    }

    private void initView() {
        mPlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (View.VISIBLE == mControllerLayout.getVisibility()) {
                    showMediaController(false);
                    handler.removeMessages(HANDLER_MEDIA_CONTROLLER);
                } else {
                    showMediaController(true);
                    handler.sendMessageDelayed(handler.obtainMessage(HANDLER_MEDIA_CONTROLLER), 3000);
                }
                return false;
            }
        });
        mSurfaceHolder = mPlayView.getHolder();
        mSurfaceHolder.addCallback(this);

        mCircleProgress.setBgColor(R.color.theme_button_deep_bg_trans_70)
                .setRingColor(R.color.trans_blank_bg_70)
                .setProgressColor(R.color.white)
                .setTextColor(R.color.white)
                .setTextSize(Tools.dip2px(getContext(), 18))
                .setTextSize2(Tools.dip2px(getContext(), 14))
                .setTextFormat(CircleProgressBar.TextFormat.TEXT_FORMAT_CURRENT_WITH_TOTAL_PROGRESS);

        mCountdownProgress.setRingColor(R.color.trans_blank_bg_70)
                .setProgressColor(R.color.white)
                .setTextColor(R.color.white)
                .setTextSize(Tools.dip2px(getContext(), 50));
    }

    @Override
    public void setupProgressBar(int totalVideos, long firstVideoDuration) {
        mProgressBar.onPrepare(totalVideos, firstVideoDuration);
    }

    @Override
    public void startProgressBar() {
//        mProgressBar.onStart();
        mProgressBar.onStartDelay(1000);
    }

    @Override
    public void updateProgressBar(boolean isNext, long duration) {
        mProgressBar.onUpdate(isNext, duration);
    }

    @Override
    public void showRestPage(boolean isShow, int restTime) {
        showMediaController(false); // 隐藏控制条

        if (isShow) {
            mCountdownLayout.setVisibility(View.VISIBLE);
            setupMaxCountdownProgress(restTime);
            updateCountdownView(restTime);
        } else {
            mCountdownLayout.setVisibility(View.GONE);
            handler.removeMessages(HANDLER_TIMER_DOWN_COUNTER);
        }
    }

    @Override
    public void updateCountdownView(int restTime) {
        mCountdownProgress.update(restTime);
        if (0 < restTime) {
            handler.sendMessageDelayed(handler.obtainMessage(HANDLER_TIMER_DOWN_COUNTER), 1000);
        } else {
            stopCountdownProgress();
            mPresenter.startAfterRest();
        }
    }

    @Override
    public void setupMaxCountdownProgress(int maxValue) {
        mCountdownProgress.setMaxProgress(maxValue);
    }

    @Override
    public void stopCountdownProgress() {
        handler.removeMessages(HANDLER_TIMER_DOWN_COUNTER);
    }

    @Override
    public void setupMaxCircleProgress(int maxValue) {
        mCircleProgress.setMaxProgress(maxValue);
    }

    @Override
    public void updateCircleProgress(int currentProgress) {
        mCircleProgress.update(currentProgress);
    }

    @Override
    public void updateRemainTimeText(long remainTime) {
        mRemainTimeText.setText(String.valueOf(remainTime));
        if (mPresenter.hasRemainTime()) {
            handler.sendMessageDelayed(handler.obtainMessage(HANDLER_TIME_REMAIN), 1000);
        }
    }

    @Override
    public void pauseRemainTime() {
        handler.removeMessages(HANDLER_TIME_REMAIN);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null == mPresenter.getMediaStatus()) {
            return;
        }
        switch (mPresenter.getMediaStatus()) {
            case IS_RESTING:
                mPresenter.updateRestCountDown();
                break;
            case IS_PLAYING:
                mPresenter.resumeAndStartMedia();
                mProgressBar.onResume();
                break;
            case PAUSE:
                mPresenter.resumeAndPauseMedia();
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 更新多媒体文件的播放状态
        mPresenter.setMediaStatus();
        // 移除消息队列
        removeHandlerMessage();
        mProgressBar.onPause();
        if (mPresenter.getMediaStatus() == MediaPlayerContract.MediaStatus.IS_PLAYING) {
            mPresenter.pauseMedia();
        }

        // 播放页从前台到后台时的处理
        // 情形1：播放时
        // 情形2：暂停时
        // 情形3：休息时
        // 暂停进度条
        // 暂停时间播放
        // 暂停左下角圆形进度条
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mProgressBar) {
            mProgressBar.onStop();
        }
        mPresenter.onRelease();
        releaseHandler();
    }

    @Override
    public void releaseHandler() {
        if (null != handler) {
            handler.removeMessages(HANDLER_TIMER_DOWN_COUNTER);
            handler.removeMessages(HANDLER_MEDIA_CONTROLLER);
            handler.removeMessages(HANDLER_TIME_REMAIN);
            handler = null;
        }
    }

    @Override
    public void removeHandlerMessage() {
        if (null != handler) {
            handler.removeMessages(HANDLER_TIMER_DOWN_COUNTER);
            handler.removeMessages(HANDLER_MEDIA_CONTROLLER);
            handler.removeMessages(HANDLER_TIME_REMAIN);
        }
    }

    @Override
    public void showMediaController(boolean isShow) {
        int visibility = isShow ? View.VISIBLE : View.GONE;
        mControllerLayout.setVisibility(visibility);
        if (isShow) {
            handler.removeMessages(HANDLER_MEDIA_CONTROLLER);
        }
    }

    protected MediaPlayerContract.Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(MediaPlayerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPresenter.setupMediaPlayer(getContext(), mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void updatePlayPauseView(boolean isPlaying) {

        if (isPlaying) {
            mStartPauseBtn.setImageResource(R.drawable.ic_pause);
            mProgressBar.onResume();
        } else {
            mStartPauseBtn.setImageResource(R.drawable.ic_play);
            mProgressBar.onPause();
        }
    }

    @OnClick({R.id.play_back, R.id.play_next, R.id.play_start_pause, R.id.play_prev, R.id.rest_skip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_back:
                getActivity().finish();
                break;
            case R.id.play_next:
                mPresenter.nextMedia(getContext());
                break;
            case R.id.play_prev:
                mPresenter.prevMedia(getContext());
                break;
            case R.id.play_start_pause:
                mPresenter.updatePlayStatus();
                break;
            case R.id.rest_skip:
                mPresenter.onSkipRest();
                break;
            default:
                break;
        }
    }

    static class TimerTaskHandler extends Handler {
        WeakReference<Fragment> mActivity;

        TimerTaskHandler(MediaPlayerFragment activity) {
            mActivity = new WeakReference<Fragment>(activity);

        }

        @Override
        public void handleMessage(Message msg) {
            MediaPlayerFragment fragment = (MediaPlayerFragment) mActivity.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_TIMER_DOWN_COUNTER:
                    fragment.getPresenter().updateRestCountDown();
                    break;
                case HANDLER_MEDIA_CONTROLLER:
                    // 处理media controller(快进，快退，暂停，播放按钮)的显示和隐藏
                    fragment.showMediaController(false);
                    break;
                case HANDLER_TIME_REMAIN:
                    fragment.getPresenter().updateRemainTime();
                    break;
                default:
                    break;
            }
        }
    }
}
