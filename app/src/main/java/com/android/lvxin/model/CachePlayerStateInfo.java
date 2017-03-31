package com.android.lvxin.model;

import com.android.lvxin.enums.MediaEnum;

import java.io.Serializable;

/**
 * @ClassName: CachePlayerStateInfo
 * @Description: TODO
 * @Author: lvxin
 * @Date: 7/19/16 11:28
 */
public class CachePlayerStateInfo implements Serializable {
    private int videoPlayIndex;
    private int videoPausePosition;
    private int videoPlayRepeats;
    private int audioPauseIndex;
    private int audioPausePosition;
    private boolean isPlaying; // 是否正在播放
    //    private boolean isIntro = true; // 是否处于开场阶段
    private boolean isCountDown; // 是否处于3,2,1,go的倒计时中
    private int countDownValue; // 3,2,1,go的倒计时当前的值
    private int playSeconds; // 视频播放时长（秒数）
    private long pauseTime; // 暂停时的系统毫秒数
    private long sendMessageTime; // 发出消息处理时的系统毫秒数
    private int restTime;
    private int circleProgressPausePosition; // 左下角圆形按秒进度，暂停时的秒数
    private boolean needHandleHeartRate = true; // 是否处理接收到的心率值, 休息和完成播放时不需要处理

    // 播放进度条相关
    private int currentClip; // 当前处于哪一段
    private float currentValue; // 当前进度条进度值

    private MediaEnum.MediaStatus mediaPlayStatus = MediaEnum.MediaStatus.NONE; // 多媒体文件的播放状态：播放，暂停，...
    private MediaEnum.MediaPreparedStatus mediaPreparedStatus = MediaEnum.MediaPreparedStatus.IS_PREPARED_START; // 视频准备就绪后的状态

    private MediaEnum.ViewShowStatus currentShowView = MediaEnum.ViewShowStatus.ACTION_NAME; // 当前显示的页面

    public CachePlayerStateInfo() {
        videoPlayIndex = 0;
        videoPausePosition = 0;
        videoPlayRepeats = 0;
        audioPauseIndex = 0;
        audioPausePosition = 0;
        isPlaying = false;
        isCountDown = false;
        countDownValue = 0;
        playSeconds = 0;
        pauseTime = 0;
        sendMessageTime = 0;
        needHandleHeartRate = true;
        restTime = 0;
        circleProgressPausePosition = 1;
        mediaPlayStatus = MediaEnum.MediaStatus.NONE;
        mediaPreparedStatus = MediaEnum.MediaPreparedStatus.IS_PREPARED_START;
        currentShowView = MediaEnum.ViewShowStatus.ACTION_NAME;

        currentClip = 1;
        currentValue = 0;

    }

    /**
     * @return
     */
    public CachePlayerStateInfo reset() {
        this.videoPausePosition = 0;
        this.videoPlayRepeats = 0;
        this.audioPauseIndex = 0;
        this.audioPausePosition = 0;
        this.isPlaying = true;
        isCountDown = false;
        countDownValue = 0;
        pauseTime = 0;
        sendMessageTime = 0;
        needHandleHeartRate = true;
        restTime = 0;
        circleProgressPausePosition = 1;
        mediaPlayStatus = MediaEnum.MediaStatus.IS_PLAYING;
        mediaPreparedStatus = MediaEnum.MediaPreparedStatus.IS_PREPARED_START;

        return this;
    }

    public int getVideoPausePosition() {
        return videoPausePosition;
    }

    public void setVideoPausePosition(int videoPausePosition) {
        this.videoPausePosition = videoPausePosition;
    }

    public int getAudioPauseIndex() {
        return audioPauseIndex;
    }

    public void setAudioPauseIndex(int audioPauseIndex) {
        this.audioPauseIndex = audioPauseIndex;
    }

    public int getAudioPausePosition() {
        return audioPausePosition;
    }

    public void setAudioPausePosition(int audioPausePosition) {
        this.audioPausePosition = audioPausePosition;
    }

    public int getVideoPlayIndex() {
        return videoPlayIndex;
    }

    public void setVideoPlayIndex(int videoPlayIndex) {
        this.videoPlayIndex = videoPlayIndex;
    }

    public int getVideoPlayRepeats() {
        return videoPlayRepeats;
    }

    public void setVideoPlayRepeats(int videoPlayRepeats) {
        this.videoPlayRepeats = videoPlayRepeats;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isCountDown() {
        return isCountDown;
    }

    public void setCountDown(boolean countDown) {
        isCountDown = countDown;
    }

    public int getCountDownValue() {
        return countDownValue;
    }

    public void setCountDownValue(int countDownValue) {
        this.countDownValue = countDownValue;
    }

    public int getPlaySeconds() {
        return playSeconds;
    }

    public void setPlaySeconds(int playSeconds) {
        this.playSeconds = playSeconds;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public long getSendMessageTime() {
        return sendMessageTime;
    }

    public void setSendMessageTime(long sendMessageTime) {
        this.sendMessageTime = sendMessageTime;
    }

    /**
     * @return
     */
    public boolean needHandleHeartRate() {
        return needHandleHeartRate;
    }

    public void setNeedHandleHeartRate(boolean needHandleHeartRate) {
        this.needHandleHeartRate = needHandleHeartRate;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getCircleProgressPausePosition() {
        return circleProgressPausePosition;
    }

    public void setCircleProgressPausePosition(int circleProgressPausePosition) {
        this.circleProgressPausePosition = circleProgressPausePosition;
    }

    public int getCurrentClip() {
        return currentClip;
    }

    public void setCurrentClip(int currentClip) {
        this.currentClip = currentClip;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * 获取多媒体文件播放的状态
     *
     * @return
     */
    public MediaEnum.MediaStatus getMediaPlayStatus() {
        return mediaPlayStatus;
    }

    /**
     * 设置多媒体文播放的状态
     *
     * @param mediaPlayStatus
     */
    public void setMediaPlayStatus(MediaEnum.MediaStatus mediaPlayStatus) {
        this.mediaPlayStatus = mediaPlayStatus;
    }

    /**
     * 获取多媒体文件准备就绪后的状态
     *
     * @return
     */
    public MediaEnum.MediaPreparedStatus getMediaPreparedStatus() {
        return mediaPreparedStatus;
    }

    /**
     * 设置多媒体文件准备就绪后的状态
     *
     * @param mediaPreparedStatus
     */
    public void setMediaPreparedStatus(MediaEnum.MediaPreparedStatus mediaPreparedStatus) {
        this.mediaPreparedStatus = mediaPreparedStatus;
    }

    public MediaEnum.ViewShowStatus getCurrentShowView() {
        return currentShowView;
    }

    public void setCurrentShowView(MediaEnum.ViewShowStatus currentShowView) {
        this.currentShowView = currentShowView;
    }

    @Override
    public String toString() {
        return "onSaveInstanceState: videoPlayIndex=" + videoPlayIndex + ",videoPlayRepeats="
                + videoPlayRepeats + ", video pause position" + videoPausePosition
                + ", audio pause index=" + audioPausePosition + ", audio index=" + audioPauseIndex;
    }
}
