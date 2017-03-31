package com.android.lvxin.controller;

import android.content.Context;

import com.android.lvxin.widget.HiiMediaPlayer;

/**
 * @ClassName: LocalAudioController
 * @Description: TODO
 * @Author: lvxin
 * @Date: 7/6/16 16:05
 */
public class LocalAudioController {

    private static LocalAudioController INSTANCE;
    private HiiMediaPlayer player;
    private Context context;

    private LocalAudioController(Context context) {
        player = new HiiMediaPlayer();
        this.context = context;
    }

    public static LocalAudioController getInstance(Context context) {
        INSTANCE = new LocalAudioController(context);
        return INSTANCE;
    }

    /**
     * 准备资源
     *
     * @param fileName
     */
    public void prepare(String fileName) {
        player.reset();
        player.setupAssets(context, fileName + ".mp3");
    }

    /**
     * 开始播放
     */
    public void start() {
        if (null != player && !player.isPlaying()) {
            player.start();
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
