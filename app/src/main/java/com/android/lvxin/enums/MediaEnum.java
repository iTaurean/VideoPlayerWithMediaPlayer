package com.android.lvxin.enums;

import java.io.Serializable;

/**
 * @ClassName: MediaEnum
 * @Description: TODO
 * @Author: lvxin
 * @Date: 8/16/16 09:25
 */
public final class MediaEnum {
    /**
     * 媒体文件播放状态
     */
    public enum MediaStatus {
        IS_PLAYING, // 正在播放
        PAUSE, // 暂停
        IS_RESTING, // 休息中
        NONE
    }

    /**
     * 视频准备就绪后的状态
     */
    public enum MediaPreparedStatus {
        IS_PREPARED_START, // 准备就绪后开始播放
        IS_PREPARED_REST, // 准备就绪后休息状态
        IS_RESUME_START, // 从后台切换回前台后开始播放
        IS_RESUME_PAUSE // 从后台切换回前台后暂停
    }

    /**
     * 心率手环链接状态
     */
    public enum BandConnectStatus {
        CONNECTED, // 已连接
        DISCONNECTED, // 已断开
        NONE // 没有绑定手环
    }

    /**
     * 显示页面的切换
     */
    public enum ViewShowStatus implements Serializable {
        ACTION_NAME, // 显示动作名称
        PREVIEW, // 预览
        READY, // 准备
        COUNT_DOWN,
        PLAY_MEDIA; // 开始播放

        private static ViewShowStatus[] values = values();

        /**
         * 下一个
         *
         * @return
         */
        public ViewShowStatus next() {
            return values[(this.ordinal() + 1) % values.length];
        }
    }

    /**
     * 心率值所在区间
     */
    public enum HeartRateStatus {
        HIGH, // 高
        NORMAL, // 正常
        LOW, // 低
        NONE
    }
}
