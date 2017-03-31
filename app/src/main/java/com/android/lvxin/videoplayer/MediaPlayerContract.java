package com.android.lvxin.videoplayer;

import android.view.SurfaceHolder;

import com.android.lvxin.BasePresenter;
import com.android.lvxin.BaseView;
import com.android.lvxin.enums.MediaEnum;
import com.android.lvxin.model.CachePlayerStateInfo;
import com.android.lvxin.model.GetPlanDetailAck;
import com.android.lvxin.model.MediaFinishInfoBean;
import com.android.lvxin.model.MediaPlayerArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MediaPlayerContract
 * @Description: 视频播放业务相关接口类
 * @Author: lvxin
 * @Date: 6/12/16 15:25
 */
public interface MediaPlayerContract {

    /**
     * 改变页面显示的监听
     */
    interface OnViewChangeListener {
        /**
         * 页面改变
         *
         * @param nextView     下一个显示的页面
         * @param currentVideo 当前播放的视频
         */
        void onViewChanged(MediaEnum.ViewShowStatus nextView, GetPlanDetailAck.PlanVideoInfo currentVideo);
    }

    /**
     * ui相关接口
     */
    interface View extends BaseView<Presenter> {

        // for tv 购买dialog
        void showSubscribeDialog();

        // =========================底部条形进度条相关=========================

        /**
         * 初始化进度条
         *
         * @param totalVideos 不同动作的总个数
         */
        void setupProgressBar(int totalVideos);

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

        void updateProgressBar(int currentClip, float currentValue, long duration);

        /**
         * 暂停进度条
         */
        void pauseProgressBar();

        /**
         * 暂停后恢复进度条
         */
        void resumeProgressBar();

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
        void updateCircleProgress(GetPlanDetailAck.PlanVideoInfo video, int currentProgress);

        /**
         * 按秒更新圆形进度条
         *
         * @param video
         */
        void updateCircleProgressBySecond(GetPlanDetailAck.PlanVideoInfo video, int pauseProgress);

        /**
         * 暂停圆形进度条的更新
         *
         * @return 暂停时的当前进度
         */
        int pauseCircleProgress();

        /**
         * 从暂停恢复圆形进度条的更新
         */
        void resumeCircleProgress();

        /**
         * 用于更新开场3秒倒计时
         *
         * @param currentProgress
         * @param maxProgress
         */
        void updateCircleProgress(int currentProgress, int maxProgress);

        /**
         * 重置圆形进度条
         */
        void resetCircleProgress();

        void update3SecondsCountdownText(int value);

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
         * 更新播放/暂停的图标, 以及进度条
         */
        void updatePlayPauseView(boolean isPlaying, float pausePosition);

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
         */
        void updatePlayTimeText(int playedTime);

        /**
         * 暂停更新剩余时间
         */
        void pausePlayedTime();

        /**
         * 更新动作名称
         *
         * @param name 动作名称
         */
        void updateActionNameText(String name);

        /**
         * 视频完成后显示完成页面
         *
         * @param value 完成页面需要的数据对象
         */
        void showFinishPage(MediaFinishInfoBean value);

        /**
         * 更新心率显示的ui
         *
         * @param rateValue
         */
        void updateRateView(int rateValue, int lowest, int highest, MediaEnum.BandConnectStatus status);

        /**
         * 更新心率值左边的图标，如果是连接状态显示心跳，断开显示重连图标
         *
         * @param status
         */
        void updateRateIcon(MediaEnum.BandConnectStatus status, boolean showToast);

        /**
         * 释放handler
         */
        void releaseHandler();

        /**
         * 移除handler队列中的消息
         */
        void removeHandlerMessage();

        void release();

        /**
         * 打开系统蓝牙设置页面
         */
        void showBluetoothPage();

        /**
         * 打开动作指导页
         *
         * @param schemeId
         * @param videoIndex
         * @param nameList
         * @param actionList
         * @param picList
         * @param videoList
         */
        void showActionDetailPage(int schemeId, int videoIndex, ArrayList<String> nameList,
                                  ArrayList<String> actionList, ArrayList<String> picList,
                                  ArrayList<String> videoList);

    }

    /**
     * 逻辑处理
     */
    interface Presenter extends BasePresenter {
        /**
         * 更新显示的页面
         *
         * @param view
         */
        void changeShowView(MediaEnum.ViewShowStatus view);

        /**
         * 改变页面的监听
         *
         * @param onViewChangeListener
         */
        void setOnViewChangeListener(MediaPlayerContract.OnViewChangeListener onViewChangeListener);

        /**
         * 播放所需的数据
         *
         * @param args 所需参数的对象
         */
        void setupData(MediaPlayerArgs args);

        /**
         * 初始化多媒体控件
         *
         * @param surfaceHolder 播放视频所需的surfaceView
         */
        void initMedia(SurfaceHolder surfaceHolder);

        /**
         * 赋予创建的多媒体控件所需的数据
         */
        void setupMedia();

        /**
         * 准备将要播放的多媒体文件
         */
        void prepareMedia(MediaEnum.MediaPreparedStatus status);

        /**
         * 开始播放已准备的多媒体文件
         */
        void onStart();

        /**
         * 从锁屏中恢复
         */
        void onRestoreFromLockScreen();

        /**
         * 跳到指定位置
         */
        void seekToAndStart();

        /**
         * 多媒体文件准备就绪
         */
        void onMediaPrepared();

        /**
         * 播放完成
         */
        void onMediaPlayCompleted();

        /**
         * 暂停
         */
        void onPause();

        /**
         * 继续播放暂停中的多媒体文件
         */
        void onResume();

        /**
         * 从后台恢复时调用
         */
        void onRestore(SurfaceHolder surfaceHolder);

        /**
         * 从后台到前台是恢复多媒体资源
         *
         * @param status
         */
        void restoreMedia(MediaEnum.MediaPreparedStatus status);

        /**
         * 下一个
         */
        void onNext();

        /**
         * 上一个
         */
        void onPrev();

        /**
         * 控制开始或暂停
         */
        void playOrPauseController();

        /**
         * 休息
         *
         * @param restTime 休息秒数
         */
        void doRest(int restTime);

        /**
         * 休息结束开始播放多媒体资源
         */
        void startAfterRest();

        /**
         * 跳过休息
         */
        void skipRest();

        /**
         * 释放多媒体资源
         */
        void onRelease();

        /**
         * 完成整个视频的播放
         */
        void completedWholeMedia();

        /**
         * 更新ui
         * 包括进度条，圆形进度，播放时间，心率，动作名称
         */
        void updateView();

        /**
         * 更新心率相关ui
         */
        void updateHeartRateView(int heartRate, MediaEnum.BandConnectStatus status);

        /**
         * 更新休息倒计时
         */
        void updateRestCountDown();

        /**
         * 更新播放时长
         */
        void updatePlayedTime();

        /**
         * 开始Intro的3,2,1,go的3秒倒计时
         */
        void startCountDownFor3Seconds();

        /**
         * 恢复Intro的3,2,1,go的倒计时
         * 当播放过程中锁屏，重新点亮屏幕时，需要调用改方法
         */
        void restoreCountDown();

        /**
         * @return
         */
        boolean isSurfaceDestroy();

        /**
         * @param isDestroy
         */
        void setSurfaceDestroy(boolean isDestroy);

        /**
         * 是否处于暂停状态
         *
         * @return
         */
        boolean isOnPause();

        /**
         * 设置是否处于暂停状态
         *
         * @param isOnPause
         */
        void setOnPause(boolean isOnPause);

        //////////////////手环相关方法///////////////////////

        void checkBluetooth();

        void checkHeartRateBand();

        /**
         * 重新连接手环
         */
        void reconnectBand();

        ///////////////////////////////////////////////////////////////////////////////////////////

        /**
         * 获取视频列表
         */
        List<GetPlanDetailAck.PlanVideoInfo> getVideos();

        /**
         * 获取指定的视频对象
         *
         * @param position
         * @return
         */
        GetPlanDetailAck.PlanVideoInfo getVideo(int position);

        /**
         * 是否有需要的资源文件
         *
         * @return
         */
        boolean emptySource();

        /**
         * 增加/减少当前编号
         *
         * @param isAdd true: +1， false: -1
         */
        void calculateMediaIndex(boolean isAdd);

        /**
         * 动作详情页
         */
        void showActionDetailPage();

        MediaPlayerArgs getCacheData();

        void setCacheData(CachePlayerStateInfo data);


    }
}