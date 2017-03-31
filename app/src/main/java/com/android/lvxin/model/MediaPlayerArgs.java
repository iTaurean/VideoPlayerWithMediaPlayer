package com.android.lvxin.model;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: MediaPlayerArgs
 * @Description: 视频播放页需要传入的数据
 * @Author: lvxin
 * @Date: 7/19/16 21:46
 */
public class MediaPlayerArgs implements Serializable {
    private int schemeId; // 方案id
    private int lowestHeartRate; // 有效心率最低值
    private int highestHeartRate; // 有效心率最高值
    private int totalDuration; // 视频播放总时长
    private String bgAudioFileName; // 背景音文件名
    private int coast; // 付费情况：0：免费，1：会员，2：已购买
    private List<GetPlanDetailAck.PlanVideoInfo> videos; // 视频列表

    private boolean isRestoreFromLock; // 是否从锁屏中恢复

    //多媒体播放状态相关
    private CachePlayerStateInfo cacheInfo;

    public int getCoast() {
        return coast;
    }

    public void setCoast(int coast) {
        this.coast = coast;
    }

    public int getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(int schemeId) {
        this.schemeId = schemeId;
    }

    public int getLowestHeartRate() {
        return lowestHeartRate;
    }

    public void setLowestHeartRate(int lowestHeartRate) {
        this.lowestHeartRate = lowestHeartRate;
    }

    public int getHighestHeartRate() {
        return highestHeartRate;
    }

    public void setHighestHeartRate(int highestHeartRate) {
        this.highestHeartRate = highestHeartRate;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getBgAudioFileName() {
        return bgAudioFileName;
    }

    public void setBgAudioFileName(String bgAudioFileName) {
        this.bgAudioFileName = bgAudioFileName;
    }

    public List<GetPlanDetailAck.PlanVideoInfo> getVideos() {
        return videos;
    }

    public void setVideos(List<GetPlanDetailAck.PlanVideoInfo> videos) {
        this.videos = videos;
    }

    public CachePlayerStateInfo getCacheInfo() {
        if (null == cacheInfo) {
            cacheInfo = new CachePlayerStateInfo();
        }
        return cacheInfo;
    }

    public void setCacheInfo(CachePlayerStateInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }

    public boolean isRestoreFromLock() {
        return isRestoreFromLock;
    }

    public void setRestoreFromLock(boolean restoreFromLock) {
        isRestoreFromLock = restoreFromLock;
    }

}
