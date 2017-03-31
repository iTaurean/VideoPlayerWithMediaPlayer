package com.android.lvxin.controller;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import com.android.lvxin.base.BaseApplication;
import com.android.lvxin.listener.OnCompletedListener;
import com.android.lvxin.listener.OnErrorListener;
import com.android.lvxin.listener.OnPreparedListener;
import com.android.lvxin.model.GetPlanDetailAck;
import com.android.lvxin.widget.HiiMediaPlayer;

/**
 * @ClassName: VideoPlayerController
 * @Description: TODO
 * @Author: lvxin
 * @Date: 7/4/16 08:56
 */
public class VideoPlayerController {
    private static final String TAG = "VideoPlayerController";
    private static VideoPlayerController INSTANCE;

    private HiiMediaPlayer player;

    private GetPlanDetailAck.PlanVideoInfo video;
    private int playedPosition;

    private OnPreparedListener onPreparedListener;
    private OnCompletedListener onCompletedListener;
    private OnErrorListener onErrorListener;

    private int schemeId;
    private boolean isIntro;

    private VideoPlayerController() {
        player = new HiiMediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onPlayCompleted(mp);
                if (null != onCompletedListener) {
                    onCompletedListener.onCompleted();
                }
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (null != onPreparedListener) {
                    onPreparedListener.onPrepared();
                }
            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (null != onErrorListener) {
                    onErrorListener.onError(what, extra);
                }
                return false;
            }
        });
    }

    public static VideoPlayerController getInstance() {
        INSTANCE = new VideoPlayerController();
        return INSTANCE;
    }

    /**
     * 更新数据
     *
     * @param schemeId
     */
    public void updateData(int schemeId) {
        this.schemeId = schemeId;
    }

    public void setDisplay(SurfaceHolder surfaceHolder) {
        player.setDisplay(surfaceHolder);
    }

    public void setOnCompletedListener(OnCompletedListener listener) {
        this.onCompletedListener = listener;
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        this.onPreparedListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        this.onErrorListener = listener;
    }

    /**
     * @param videoInfo
     */
    public void updateData(GetPlanDetailAck.PlanVideoInfo videoInfo) {
        this.video = videoInfo;
        playedPosition = 0;
    }

    /**
     * 更新数据
     *
     * @param videoInfo
     * @param isIntro
     */
    public void updateData(GetPlanDetailAck.PlanVideoInfo videoInfo, boolean isIntro) {
        this.video = videoInfo;
        this.isIntro = isIntro;
        playedPosition = 0;
    }

    /**
     * 更新数据
     *
     * @param videoInfo
     * @param playedPosition
     * @param isIntro
     */
    public void updateData(GetPlanDetailAck.PlanVideoInfo videoInfo, int playedPosition,
                           boolean isIntro) {
        this.video = videoInfo;
        this.isIntro = isIntro;
        this.playedPosition = playedPosition;
    }

    /**
     * 准备资源
     */
    public void prepareAsync() {
        player.reset();
//        String dataSource;
//        if (isIntro) {
//            dataSource = getPath(video.getIntroName());
//        } else {
//            dataSource = getPath(video.getUnitName());
//        }
//        if (FileUtils.isExistFile(dataSource)) {
//            player.setupAsync(dataSource);
        player.setupAssetsAsync(BaseApplication.getInstance().getApplicationContext(),
                schemeId + "/" + (isIntro ? video.getIntroName() : video.getUnitName()) + ".mp4");
//        }
    }

    private void onPlayCompleted(MediaPlayer mp) {
    }

    /**
     * 开始
     */
    public void start() {
        player.start();
    }

    /**
     * 快进
     *
     * @param position
     */
    public void seekTo(int position) {
        player.seekTo(position);
    }

    /**
     * 快进到指定位置开始
     *
     * @param position
     */
    public void seekToStart(int position) {
        player.seekTo(position);
        player.start();
    }

    /**
     * 快进到指定位置开始
     */
    public void seekToStart() {
        if (0 != playedPosition) {
            player.seekTo(playedPosition);
        }
        player.start();
    }

    /**
     * 继续
     */
    public void resume() {
        // 继续播放音频
        if (null != player) {
            if (0 == playedPosition) {
                player.start();
            } else {
                if (!player.isPlaying()) {
                    player.seekToStart(playedPosition);
                }
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (null != player) {
            if (player.isPlaying()) {
                playedPosition = player.getCurrentPosition();
                player.pause();
            }
        }
    }

    /**
     * 继续播放
     */
    public void resumeAndStart() {
        player.seekToStart(playedPosition);
    }

    /**
     * 暂停
     */
    public void resumeAndPause() {
        player.seekToPause(playedPosition);
    }

    /**
     * 准备资源并暂停
     */
    public void prepareAndRest() {
        player.start();
        player.pauseAndSeekTo(0);
    }

    public boolean isPlaying() {
        if (null != player) {
            return player.isPlaying();
        } else {
            return false;
        }
    }

    public int getCurrentPosition() {
        if (null != player) {
            return player.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public int getPausePosition() {
        return playedPosition;
    }

    public void setPausePosition(int pausePosition) {
        this.playedPosition = pausePosition;
    }

    /**
     * 重置
     */
    public void reset() {
        if (null != player) {
            player.reset();
        }
    }

    /**
     * 释放资源
     */
    public void onRelease() {
        if (null != player) {
            player.release();
        }
        player = null;

    }

//    private String getPath(String fileName) {
//        Log.i(TAG, "getPath: video dir:" + BaseApplication.getVideoDir() + schemeId + "/" + fileName + ".mp4");
//        return BaseApplication.getVideoDir() + schemeId + "/" + fileName + ".mp4";
//    }
}
