package com.android.lvxin.videoplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lvxin.R;
import com.android.lvxin.base.BaseApplication;
import com.android.lvxin.enums.MediaEnum;
import com.android.lvxin.model.CachePlayerStateInfo;
import com.android.lvxin.model.GetPlanDetailAck;
import com.android.lvxin.model.MediaFinishInfoBean;
import com.android.lvxin.model.MediaPlayerArgs;
import com.android.lvxin.util.ViewUtils;
import com.android.lvxin.widget.CalendarHelper;
import com.android.lvxin.widget.CircleProgressBar;
import com.android.lvxin.widget.MediaProgressBar;
import com.android.lvxin.widget.RiseNumberTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @ClassName: MediaPlayerFragment
 * @Description: 视频播放ui
 * @Author: lvxin
 * @Date: 6/12/16 15:25
 */
public class MediaPlayerFragment extends Fragment
        implements MediaPlayerContract.View, SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = "MediaPlayerFragment";
    private static final String ARG_VALUES = "arg_values";
    private static final int HANDLER_TIMER_DOWN_COUNTER = 1;
    private static final int HANDLER_MEDIA_CONTROLLER = 2;
    private static final int HANDLER_TIME_PLAYED = 3;
    private static MediaPlayerFragment INSTANCE;

    LinearLayout mPreviewTextLayout;
    LinearLayout mStaticViewLayout;
    RiseNumberTextView mNumberTextView;
    TextView mContentTextView;
    TextView mUnitTextView;

    FrameLayout mPlayLayout;
    SurfaceView mPlayView;

    ImageView mBackBtn;
    ImageView mNextBtn;
    ImageView mStartPauseBtn;
    ImageView mPrevBtn;

    RelativeLayout mControllerLayout;
    MediaProgressBar mProgressBar;
    TextView mActionName;
    ImageView mActionDetailBtn;

    CircleProgressBar mCircleProgress;
    TextView mPlayTimeText;

    //心率相关
    TextView mHeartRateText;
    ImageView mHeartRateConnected;
    RelativeLayout mHeartRateLayout;

    ImageView mHeartRateInterval;
    GradientDrawable mHeartRateGradient;

    RelativeLayout mCountdownLayout;
    CircleProgressBar mCountdownProgress;
    TextView mSkipBtn;

    private SurfaceHolder mSurfaceHolder;
    private TimerTaskHandler handler;
    private MediaPlayerContract.Presenter mPresenter;
    private MediaPlayerArgs mData;
    private boolean isCreating = true;
    private MediaEnum.BandConnectStatus mBandConnectStatus = MediaEnum.BandConnectStatus.NONE;
    private MediaEnum.HeartRateStatus mCurrentHeartRateStatus = MediaEnum.HeartRateStatus.NONE;
    private MediaPlayerContract.OnViewChangeListener onViewChangeListener = new MediaPlayerContract.OnViewChangeListener() {
        @Override
        public void onViewChanged(MediaEnum.ViewShowStatus nextView, GetPlanDetailAck.PlanVideoInfo currentVideo) {
            mContentTextView.clearAnimation();
            switch (nextView) {
                case ACTION_NAME:
                    mPreviewTextLayout.setVisibility(View.GONE);
                    mStaticViewLayout.setVisibility(View.VISIBLE);
                    mUnitTextView.setVisibility(View.GONE);
                    mNumberTextView.setVisibility(View.GONE);
                    mContentTextView.setVisibility(View.VISIBLE);
                    mContentTextView.setText(currentVideo.getPlanVideoName());
                    mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                    break;
                case PREVIEW:
                    mPreviewTextLayout.setVisibility(View.VISIBLE);
                    mStaticViewLayout.setVisibility(View.GONE);
                    mCircleProgress.setVisibility(View.GONE);
                    break;
                case READY:
                    mPreviewTextLayout.setVisibility(View.GONE);
                    mStaticViewLayout.setVisibility(View.VISIBLE);
                    mContentTextView.setVisibility(View.GONE);
                    mNumberTextView.setVisibility(View.VISIBLE);
                    mUnitTextView.setVisibility(View.VISIBLE);
                    if (1 == currentVideo.getGuideType()) {
                        mNumberTextView.withNumber(currentVideo.getPlayTimes()).start();
                        mUnitTextView.setText("x");
                    } else {
                        mNumberTextView.withNumber(currentVideo.getMovementTime()).start();
                        mUnitTextView.setText("s");
                    }
                    break;
                case COUNT_DOWN:
                    mStaticViewLayout.setVisibility(View.VISIBLE);
                    mUnitTextView.setVisibility(View.GONE);
                    mNumberTextView.setVisibility(View.GONE);
                    mContentTextView.setVisibility(View.VISIBLE);
                    mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 64);
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_in_out);
                    mContentTextView.startAnimation(animation);
                    mContentTextView.setText("3");
                    break;
                case PLAY_MEDIA:
                    mCircleProgress.setVisibility(View.VISIBLE);
                    mStaticViewLayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    public MediaPlayerFragment() {
    }

    public static MediaPlayerFragment getInstance(MediaPlayerArgs arg) {
        if (null == INSTANCE) {
            INSTANCE = new MediaPlayerFragment();
        }
        Bundle args = new Bundle();
        args.putSerializable(ARG_VALUES, arg);
        INSTANCE.setArguments(args);

        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (null != mPresenter) {

            if (null != savedInstanceState) {
                mData = (MediaPlayerArgs) savedInstanceState.getSerializable(ARG_VALUES);
                mData.setRestoreFromLock(true);
            } else {
                if (null != getArguments()) {
                    mData = (MediaPlayerArgs) getArguments().get(ARG_VALUES);
                    mData.setRestoreFromLock(false);
                    CachePlayerStateInfo cacheInfo = new CachePlayerStateInfo();
                    mData.setCacheInfo(cacheInfo);
                }
            }
            mPresenter.setupData(mData);
            mPresenter.start(getContext());

            mPresenter.setOnViewChangeListener(onViewChangeListener);
            handler = new TimerTaskHandler(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mData = mPresenter.getCacheData();
        outState.putSerializable(ARG_VALUES, mData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media_player, container, false);
        initView(root);
        mPresenter.checkHeartRateBand();
        return root;
    }

    private void initView(View root) {
        initCompt(root);
        mPlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int i = v.getId();
                if (i == R.id.video_player_view) {
                    if (View.VISIBLE == mControllerLayout.getVisibility()) {
                        showMediaController(false);
                        handler.removeMessages(HANDLER_MEDIA_CONTROLLER);
                    } else {
                        showMediaController(true);
                        handler.sendMessageDelayed(
                                handler.obtainMessage(HANDLER_MEDIA_CONTROLLER), 3000);
                    }

                } else {
                }

                return false;
            }
        });

        mSurfaceHolder = mPlayView.getHolder();
        mSurfaceHolder.addCallback(this);

        mCircleProgress.setBgColor(R.color.theme_button_deep_bg_trans_70)
                .setRingColor(R.color.trans_blank_bg_70).setProgressColor(android.R.color.white)
                .setTextColor(android.R.color.white).setTextSize(ViewUtils.dip2px(getContext(), 18))
                .setTextSize2(ViewUtils.dip2px(getContext(), 14))
                .setTextFormat(CircleProgressBar.TextFormat.CURRENT_WITH_TOTAL_PROGRESS);
        mCircleProgress
                .setOnProgressUpdateListener(new CircleProgressBar.OnProgressUpdateListener() {
                    @Override
                    public void onProgressUpdate(int progress) {
                        mData.getCacheInfo().setCircleProgressPausePosition(progress);
                    }
                });

        mCountdownProgress.setRingColor(R.color.trans_blank_bg_70).setProgressColor(android.R.color.white)
                .setTextColor(android.R.color.white).setTextSize(ViewUtils.dip2px(getContext(), 50))
                .setIsCountDown(true);

        mProgressBar.setOnProgressUpdateListener(new MediaProgressBar.OnProgressUpdateListener() {
            @Override
            public void onUpdate(int currentClip, float currentValue) {
                mData.getCacheInfo().setCurrentClip(currentClip);
                mData.getCacheInfo().setCurrentValue(currentValue);
            }
        });
        mHeartRateGradient = (GradientDrawable) mHeartRateLayout.getBackground();

        updateRateIcon(MediaEnum.BandConnectStatus.DISCONNECTED, false);
        mHeartRateText.setText("--bpm");
    }


    private void initCompt(View root) {

        mPreviewTextLayout = (LinearLayout) root.findViewById(R.id.layout_media_player_preview_text);
        mStaticViewLayout = (LinearLayout) root.findViewById(R.id.layout_media_player_static_view);
        mNumberTextView = (RiseNumberTextView) root.findViewById(R.id.tv_player_static_number);
        mContentTextView = (TextView) root.findViewById(R.id.tv_player_static_content);
        mUnitTextView = (TextView) root.findViewById(R.id.tv_player_static_unit);

        mPlayLayout = (FrameLayout) root.findViewById(R.id.video_player_layout);
        mPlayView = (SurfaceView) root.findViewById(R.id.video_player_view);

        mBackBtn = (ImageView) root.findViewById(R.id.play_back);
        mBackBtn.setOnClickListener(this);
        mNextBtn = (ImageView) root.findViewById(R.id.play_next);
        mNextBtn.setOnClickListener(this);
        mStartPauseBtn = (ImageView) root.findViewById(R.id.play_start_pause);
        mStartPauseBtn.setOnClickListener(this);
        mPrevBtn = (ImageView) root.findViewById(R.id.play_prev);
        mPrevBtn.setOnClickListener(this);

        mControllerLayout = (RelativeLayout) root.findViewById(R.id.media_controller);
        mProgressBar = (MediaProgressBar) root.findViewById(R.id.play_progress);
        mActionName = (TextView) root.findViewById(R.id.text_play_action_name);
        mActionDetailBtn = (ImageView) root.findViewById(R.id.play_attention);
        mActionDetailBtn.setOnClickListener(this);

        mCircleProgress = (CircleProgressBar) root.findViewById(R.id.play_circle_progress);
        mPlayTimeText = (TextView) root.findViewById(R.id.play_time_passed);

        //心率相关
        mHeartRateText = (TextView) root.findViewById(R.id.play_heart_rate_text);
        mHeartRateConnected = (ImageView) root.findViewById(R.id.play_heart_connect_status);
        mHeartRateLayout = (RelativeLayout) root.findViewById(R.id.play_heart_rate_layout);
        mHeartRateLayout.setOnClickListener(this);

        mHeartRateInterval = (ImageView) root.findViewById(R.id.player_heart_rate_interval);

        mCountdownLayout = (RelativeLayout) root.findViewById(R.id.layout_rest_countdown);
        mCountdownProgress = (CircleProgressBar) root.findViewById(R.id.rest_countdown);
        mSkipBtn = (TextView) root.findViewById(R.id.rest_skip);
        mSkipBtn.setOnClickListener(this);
    }

    @Override
    public void setupProgressBar(int totalVideos) {
        mProgressBar.onPrepare(totalVideos);
    }

    @Override
    public void startProgressBar() {
        //        mProgressBar.onStartDelay(1000);
        Log.i(TAG, "startProgressBar: ");
        mProgressBar.onStart();
    }

    @Override
    public void pauseProgressBar() {
        mProgressBar.onPause();
    }

    @Override
    public void resumeProgressBar() {
        mProgressBar.onResume();
    }

    @Override
    public void updateProgressBar(boolean isNext, long duration) {
        mProgressBar.onUpdate(isNext, duration);
    }

    @Override
    public void updateProgressBar(int currentClip, float currentValue, long duration) {
        mProgressBar.onUpdate(currentClip, currentValue, duration);
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
    public void updateCircleProgress(int currentProgress, int maxProgress) {
        if (null != mCircleProgress) {
            mCircleProgress.setTextFormat(CircleProgressBar.TextFormat.CURRENT_PROGRESS);
            mCircleProgress.setMaxProgress(maxProgress);
            mCircleProgress.update(currentProgress, false);
        }
    }

    @Override
    public void update3SecondsCountdownText(int value) {
        mContentTextView.setText(String.valueOf(1 > value ? 1 : value));
        mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 64);
        if (0 < value) {
            int animRes = 1 == value ? R.anim.translate_in : R.anim.translate_in_out;
            Animation inAnimation = AnimationUtils.loadAnimation(getContext(), animRes);
            mContentTextView.startAnimation(inAnimation);
        } else {
            mContentTextView.clearAnimation();
        }
    }

    @Override
    public void updateCircleProgress(GetPlanDetailAck.PlanVideoInfo video, int currentProgress) {
        if (2 == video.getGuideType()) {
            mCircleProgress.setTextFormat(CircleProgressBar.TextFormat.CURRENT_PER_SECOND_PROGRESS);
            mCircleProgress.setMaxProgress(video.getMovementTime());
        } else {
            mCircleProgress.setTextFormat(CircleProgressBar.TextFormat.CURRENT_WITH_TOTAL_PROGRESS);
            mCircleProgress.setMaxProgress(video.getPlayTimes());
        }
        mCircleProgress.update(currentProgress);
        mData.getCacheInfo().setCircleProgressPausePosition(currentProgress);
    }

    @Override
    public void updateCircleProgressBySecond(GetPlanDetailAck.PlanVideoInfo video, int currentProgress) {
        mCircleProgress.setMaxProgress(video.getMovementTime());
        mCircleProgress.updatePerSecond(currentProgress);
    }

    @Override
    public int pauseCircleProgress() {
        mCircleProgress.pause();
        return mCircleProgress.getCurrentProgress();
    }

    @Override
    public void resumeCircleProgress() {
        mCircleProgress.resume();
    }

    @Override
    public void resetCircleProgress() {
        mCircleProgress.reset();
    }

    @Override
    public void updatePlayTimeText(int playedSecond) {
        if (null != handler) {
            mPlayTimeText.setText(CalendarHelper.secToMinSec(playedSecond));
            handler.sendMessageDelayed(handler.obtainMessage(HANDLER_TIME_PLAYED), 1000);
        }
    }

    @Override
    public void pausePlayedTime() {
        if (null != handler) {
            handler.removeMessages(HANDLER_TIME_PLAYED);
        }
    }

    @Override
    public void updateActionNameText(String name) {
        mActionName.setText(name);
    }

    @Override
    public void updateRateView(final int rateValue, final int lowest, final int highest, final MediaEnum.BandConnectStatus status) {
    }


    @Override
    public void updateRateIcon(MediaEnum.BandConnectStatus status, boolean showToast) {
    }

    @Override
    public void showFinishPage(MediaFinishInfoBean value) {
        ((MediaPlayerActivity) getContext()).finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        //提示打开蓝牙
        mPresenter.checkBluetooth();
        if (!mPresenter.isSurfaceDestroy()) {
            Log.i(TAG, "onResume: restore");
            mPresenter.onRestore(mSurfaceHolder);
        }

    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        isCreating = false;
        mCircleProgress.reset();
        // 移除消息队列
        removeHandlerMessage();
        mProgressBar.onPause();
        mPresenter.onPause();
    }

    @Override
    public void release() {
        mPresenter.onRelease();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        isCreating = true;
        mBandConnectStatus = MediaEnum.BandConnectStatus.NONE;
        mCurrentHeartRateStatus = MediaEnum.HeartRateStatus.NONE;
        if (null != mProgressBar) {
            mProgressBar.onStop();
        }
        releaseHandler();
        mPresenter.onRelease();
    }

    @Override
    public void releaseHandler() {
        if (null != handler) {
            handler.removeMessages(HANDLER_TIMER_DOWN_COUNTER);
            handler.removeMessages(HANDLER_MEDIA_CONTROLLER);
            handler.removeMessages(HANDLER_TIME_PLAYED);
            handler = null;
        }
    }

    @Override
    public void removeHandlerMessage() {
        if (null != handler) {
            handler.removeMessages(HANDLER_TIMER_DOWN_COUNTER);
            handler.removeMessages(HANDLER_MEDIA_CONTROLLER);
            handler.removeMessages(HANDLER_TIME_PLAYED);
        }
    }

    @Override
    public void showMediaController(boolean isShow) {
        long animationDuration = 200;
        if (isShow) {
            mControllerLayout.setAlpha(0f);
            mControllerLayout.setVisibility(View.VISIBLE);
            mControllerLayout.animate().alpha(1f).setDuration(animationDuration).setListener(null);
        } else {
            mControllerLayout.animate().alpha(0f).setDuration(animationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (null != mControllerLayout) {
                                mControllerLayout.setVisibility(View.GONE);
                            }
                        }
                    });
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
        Log.i(TAG, "surfaceCreated: ");
        mPresenter.setSurfaceDestroy(false);

        //两种情况：1. 创建， 2. 从后台恢复
        if (isCreating) {
            // 创建有两种情况：1. 第一次进入播放页， 2. 从锁屏中恢复
            mPresenter.initMedia(mSurfaceHolder);
            mPresenter.setupMedia();
        } else {
            // 从后台恢复
            Log.i(TAG, "surfaceCreated: restore");
            mPresenter.onRestore(mSurfaceHolder);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: ");
        mPresenter.setSurfaceDestroy(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void updatePlayPauseView(boolean isPlaying) {
        if (isPlaying) {
            mStartPauseBtn.setImageResource(R.mipmap.ic_pause);
        } else {
            mStartPauseBtn.setImageResource(R.mipmap.ic_play);
        }
    }

    @Override
    public void updatePlayPauseView(boolean isPlaying, float pausePosition) {
        if (isPlaying) {
            mStartPauseBtn.setImageResource(R.mipmap.ic_pause);
            mProgressBar.onResume();
        } else {
            mStartPauseBtn.setImageResource(R.mipmap.ic_play);
            mProgressBar.onPause(pausePosition);
        }
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        int i = view.getId();
        if (i == R.id.play_back) {
//            final ConfirmTitleDialog dialog = new ConfirmTitleDialog(getContext(),
//                    getString(R.string.dialog_over_plan_video_content), R.string.stage_over,
//                    R.string.btn_continue);
//            dialog.setLeftButtonListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().finish();
//                    dialog.dismiss();
//                }
//            });
//            dialog.setRightButtonListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
            Toast.makeText(getActivity(), "show a dialog to close this page.", Toast.LENGTH_SHORT).show();
            getActivity().finish();


        } else if (i == R.id.play_next) {
            mPresenter.onNext();

        } else if (i == R.id.play_prev) {
            mPresenter.onPrev();

        } else if (i == R.id.play_start_pause) {
            mPresenter.playOrPauseController();

        } else if (i == R.id.rest_skip) {
            mPresenter.skipRest();

        } else if (i == R.id.play_attention) {
            mPresenter.showActionDetailPage();

        } else {
        }
    }

    @Override
    public void showBluetoothPage() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(intent);
    }

    @Override
    public void showActionDetailPage(int schemeId, int videoIndex, ArrayList<String> nameList, ArrayList<String> actionList,
                                     ArrayList<String> picList, ArrayList<String> videoList) {
        //TODO
    }

    @Override
    public void showSubscribeDialog() {
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
                case HANDLER_TIME_PLAYED:
                    fragment.getPresenter().updatePlayedTime();
                    break;

                default:
                    break;
            }
        }
    }
}
