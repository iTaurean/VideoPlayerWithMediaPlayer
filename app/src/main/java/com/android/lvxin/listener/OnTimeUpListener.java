package com.android.lvxin.listener;

/**
 * @ClassName: OnTimeUpListener
 * @Description: 用于音频播放3，2，1，go开始时的监听
 * @Author: lvxin
 * @Date: 8/5/16 14:48
 */
public interface OnTimeUpListener {
    /**
     * 事件处理
     *
     * @param value
     */
    void onTimeUp(int value);
}
