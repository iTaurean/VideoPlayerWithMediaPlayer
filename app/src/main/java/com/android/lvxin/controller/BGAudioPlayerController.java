package com.android.lvxin.controller;

import android.content.Context;
import android.media.MediaPlayer;

import com.android.lvxin.util.FileUtils;
import com.android.lvxin.widget.HiiMediaPlayer;

/**
 * @ClassName: BGAudioPlayerController
 * @Description: 背景音乐相关的逻辑处理
 * @Author: lvxin
 * @Date: 7/4/16 14:31
 */
public class BGAudioPlayerController {
    private static BGAudioPlayerController INSTANCE;

    private HiiMediaPlayer player;
    private int playedPosition;

    private boolean isAvailableFile;

    private BGAudioPlayerController() {
        player = new HiiMediaPlayer();
    }

    public static BGAudioPlayerController getInstance() {
        INSTANCE = new BGAudioPlayerController();
        return INSTANCE;
    }

    /**
     * 准备资源
     *
     * @param dataSource
     */
    public void prepareAsync(String dataSource) {
        player.reset();
        player.setup(dataSource);
    }

    /**
     * 开始播放
     */
    public void start() {
        if (isAvailableFile) {
            player.start();
        }
    }

    /**
     * 准备播放资源并开始播放
     *
     * @param dataSource
     */
    public void prepareAndStart(String dataSource) {
        isAvailableFile = FileUtils.isExistFile(dataSource);
        player.reset();
        player.setLooping(true);
        if (isAvailableFile) {
            player.setupAsync(dataSource);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
        }
    }

    /**
     * 准备资源
     *
     * @param fileName
     */
    public void prepareAndStart(Context context, String fileName) {
        isAvailableFile = true;
        player.reset();
        player.setLooping(true);
        player.setupAssetsAsync(context, fileName + ".mp3");
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();
            }
        });
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isAvailableFile) {
            if (null != player && player.isPlaying()) {
                playedPosition = player.getCurrentPosition();
                player.pause();
            }
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        if (isAvailableFile) {
            if (null != player && !player.isPlaying()) {
                player.seekToStart(playedPosition);
            }
        }
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
}
