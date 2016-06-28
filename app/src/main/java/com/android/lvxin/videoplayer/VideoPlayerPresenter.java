package com.android.lvxin.videoplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * @ClassName: VideoPlayerPresenter
 * @Description: 处理视频播放的业务逻辑
 * @Author: lvxin
 * @Date: 6/28/16 11:23
 */
public class VideoPlayerPresenter {
    private MediaPlayer mediaPlayer;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pause();
        }
    };
    private OnCompletionListener onCompletionListener;
    private OnPreparedListener onPreparedListener;

    public VideoPlayerPresenter() {
        mediaPlayer = new MediaPlayer();
    }

    /**
     * 初始化视频
     *
     * @param surfaceHolder
     * @return MediaPlayer Object
     */
    public VideoPlayerPresenter setup(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (null != onCompletionListener) {
                    onCompletionListener.onCompletion(mp);
                }
            }
        });

        return this;
    }

    /**
     * 准备播放的视频
     *
     * @param dataSource 视频的完整路径
     */
    public void prepareAsync(String dataSource) {
        try {
            mediaPlayer.setDataSource(dataSource);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (null != onPreparedListener) {
                        onPreparedListener.onPrepared(mp);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    public void start() {
        mediaPlayer.start();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 跳到指定位置并播放
     *
     * @param position 指定跳转的毫秒数
     */
    public void seekToStart(int position) {
        seekTo(position);
        start();
    }

    /**
     * 暂定在指定的位置
     *
     * @param position 指定跳转的毫秒数
     */
    public void pauseAndSeekTo(int position) {
        if (mediaPlayer.isPlaying()) {
            pause();
            seekTo(position);
        }
    }

    /**
     * 跳到指定位置并暂停
     *
     * @param position 指定跳转的毫秒数
     */
    public void seekToPause(int position) {
        seekToStart(position);
        handler.sendEmptyMessageDelayed(0, 100);
    }

    /**
     * 跳到指定位置
     *
     * @param position 指定跳转的毫秒数
     */
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    /**
     * 重置MediaPlayer
     */
    public void reset() {
        mediaPlayer.reset();
    }

    /**
     * 判断是否处于播放状态
     *
     * @return is playing or not
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 设置循环播放
     *
     * @param looping 是否循环播放
     */
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    /**
     * 释放MediaPlayer资源
     */
    public void release() {
        if (null != mediaPlayer) {
            mediaPlayer.release();
        }
        onPreparedListener = null;
        onCompletionListener = null;
        mediaPlayer = null;
    }

    /**
     * 播放完成监听
     *
     * @param listener 事件监听器
     */
    public void setOnCompletionListener(OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }

    /**
     * 准备就绪监听
     *
     * @param listener 事件监听器
     */
    public void setOnPreparedListener(OnPreparedListener listener) {
        this.onPreparedListener = listener;
    }

    /**
     * 获取视频当前播放位置
     *
     * @return 当前播放位置的毫秒数
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    interface OnCompletionListener {
        void onCompletion(MediaPlayer mp);
    }

    interface OnPreparedListener {
        void onPrepared(MediaPlayer mp);
    }

}
