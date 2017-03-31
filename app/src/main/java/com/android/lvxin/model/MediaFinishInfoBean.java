package com.android.lvxin.model;

import java.io.Serializable;

/**
 * @ClassName: MediaFinishInfoBean
 * @Description: TODO
 * @Author: yzb2014
 * @Date: 16/7/7 15:17
 */
public class MediaFinishInfoBean implements Serializable {
    private int thisTimeObtain;//本次获得嗨豆
    private int maxHeartRate;//最大心率
    private int minHeartRate;//最小心率
    private int averageHeartRate;//平均心率
    private int effectiveExerciseDuration;//有效运动时长
    private int totalTime;//总时长
    private int planId;//调理计划id
    private int planSecond;//运动时长
    private boolean isShowHeartRate;//是否显示心率  false表示没有心率值 true表示有心率值

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getPlanSecond() {
        return planSecond;
    }

    public void setPlanSecond(int planSecond) {
        this.planSecond = planSecond;
    }

    public boolean isShowHeartRate() {
        return isShowHeartRate;
    }

    public void setShowHeartRate(boolean showHeartRate) {
        isShowHeartRate = showHeartRate;
    }

    public int getThisTimeObtain() {
        return thisTimeObtain;
    }

    public void setThisTimeObtain(int thisTimeObtain) {
        this.thisTimeObtain = thisTimeObtain;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getEffectiveExerciseDuration() {
        return effectiveExerciseDuration;
    }

    public void setEffectiveExerciseDuration(int effectiveExerciseDuration) {
        this.effectiveExerciseDuration = effectiveExerciseDuration;
    }

    public int getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(int minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }
}
