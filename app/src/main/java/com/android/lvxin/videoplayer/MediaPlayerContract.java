package com.android.lvxin.videoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import com.android.lvxin.BasePresenter;
import com.android.lvxin.BaseView;
import com.android.lvxin.data.VideoInfo;

import java.util.List;

/**
 * @ClassName: MediaPlayerContract
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 15:25
 */
public interface MediaPlayerContract {

    /**
     * 媒体文件播放状态
     */
    enum MediaStatus {
        IS_PLAYING, // 正在播放
        PAUSE, // 暂停
        IS_RESTING // 休息中
    }

    interface View extends BaseView<Presenter> {
        // =========================底部条形进度条相关=========================

        /**
         * 初始化进度条
         *
         * @param totalVideos        不同动作的总个数
         * @param firstVideoDuration 第一个动作的时长
         */
        void setupProgressBar(int totalVideos, long firstVideoDuration);

        /**
         * 开始播放进度条
         */
        void startProgressBar();

        /**
         * 点击上一个/下一个时进度条更新
         *
         * @param duration
         */
        void updateProgressBar(boolean isNext, long duration);

        // ===============================结束===============================


        // ===========================圆形形进度条相关=========================

        /**
         * 设置圆环进度条的最大值
         *
         * @param maxValue
         */
        void setupMaxCircleProgress(int maxValue);

        /**
         * 设置圆环进度条当前位置
         *
         * @param currentProgress
         */
        void updateCircleProgress(int currentProgress);

        // ===============================结束===============================

        // =============================休息页面相关===========================

        /**
         * 显示休息页面
         *
         * @param isShow   是否休息状态
         * @param restTime 休息时长
         */
        void showRestPage(boolean isShow, int restTime);

        /**
         * 更新倒计时数字
         *
         * @param restTime
         */
        void updateCountdownView(int restTime);

        /**
         * 设置休息的时间
         *
         * @param maxValue 休息的秒数
         */
        void setupMaxCountdownProgress(int maxValue);

        /**
         * 停止倒计时
         */
        void stopCountdownProgress();

        // ===============================结束===============================

        /**
         * 更新播放/暂停的图标
         */
        void updatePlayPauseView(boolean isPlaying);


        /**
         * 显示或隐藏多媒体控制器(快进，快退，播放，暂停，返回按钮)
         *
         * @param isShow
         */
        void showMediaController(boolean isShow);

        /**
         * 更新剩余时间
         *
         * @param remainTime 单位：秒
         */
        void updateRemainTimeText(long remainTime);

        /**
         * 暂停更新剩余时间
         */
        void pauseRemainTime();

        /**
         * 释放handler
         */
        void releaseHandler();

        /**
         * 移除handler队列中的消息
         */
        void removeHandlerMessage();

    }

    interface Presenter extends BasePresenter {

        /**
         * 获取多媒体数据
         */
        void loadData();

        /**
         * 获取视频对象列表
         *
         * @return
         */
        List<VideoInfo> getVideos();

        /**
         * 获取指定的视频对象
         *
         * @param index
         * @return
         */
        VideoInfo getVideo(int index);

        /**
         * 获取视频/视频的uri
         *
         * @param contentUri
         * @param id
         * @return
         */
        Uri getMediaUri(Uri contentUri, long id);

        /**
         * 建立视频播放器
         *
         * @param context
         * @param surfaceHolder
         */
        void setupMediaPlayer(Context context, SurfaceHolder surfaceHolder);

        /**
         * 准备并开始播放视频
         *
         * @param context
         */
        void prepareAndStartVideo(Context context);

        /**
         * 准备视频
         *
         * @param context
         */
        void prepareVideo(Context context);

        /**
         * 准备音频
         *
         * @param context
         */
        void prepareAudio(Context context);

        /**
         * 开始播放
         */
        void onStartPlay();

        /**
         * 视频播放完成后的处理
         *
         * @param mp
         */
        void onVideoPlayCompleted(Context context, MediaPlayer mp);

        /**
         * 暂停后继续播放
         */
        void continuePlayMedia();

        /**
         * 暂停播放
         */
        void pauseMedia();

        /**
         * 重新打开时恢复播放
         */
        void resumeAndStartMedia();

        /**
         * 重新打开时回复并处于暂停状态
         */
        void resumeAndPauseMedia();

        /**
         * 下一个
         *
         * @param context
         */
        void nextMedia(Context context);

        /**
         * 前一个
         *
         * @param context
         */
        void prevMedia(Context context);

        /**
         * 释放资源
         */
        void onRelease();

        /**
         * 两组动作之间的休息状态
         *
         * @param restTime 休息的秒数
         */
        void onRest(int restTime);

        /**
         * 跳过休息
         */
        void onSkipRest();

        /**
         * 休息结束后开始下一个动作
         */
        void startAfterRest();

        /**
         * 更新休息时的倒计时
         */
        void updateRestCountDown();

        /**
         * 获取播放状态
         *
         * @return
         */
        boolean getPlayingStatus();

        /**
         * 设置播放/暂停状态
         *
         * @return
         */
        void updatePlayStatus();

        /**
         * 设置视频退到后台时播放的位置
         *
         * @param position
         */
        void setVideoPlayedPosition(int position);

        /**
         * 设置音频退到后台时播放的位置
         *
         * @param position
         */
        void setAudioPlayedPosition(int position);

        /**
         * 更新剩余时间
         */
        void updateRemainTime();

        /**
         * 是否还有剩余播放时间
         *
         * @return
         */
        boolean hasRemainTime();

        /**
         * 获取多媒体文件的播放状态
         *
         * @return
         */
        MediaStatus getMediaStatus();

        /**
         * 设置多媒体文件的播放状态
         */
        void setMediaStatus();

    }
}
