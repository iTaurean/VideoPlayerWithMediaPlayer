package com.android.lvxin.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * @ClassName: HiiMediaPlayer
 * @Description: 扩展系统MediaPlayer的自定义类
 * @Author: lvxin
 * @Date: 6/14/16 15:22
 */
public class HiiMediaPlayer extends MediaPlayer {

    private static final String TAG = "HiiMediaPlayer";

    private static final int HANDLER_MESSAGE_ID = 32;

    private PlayerHandler handler;

    public HiiMediaPlayer() {
        super();
        handler = new PlayerHandler(this);
    }

    /**
     * 准备播放的视频
     *
     * @param dataSource 视频的完整路径
     */
    public void setupAsync(String dataSource) {
        try {
            setDataSource(dataSource);
            prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备播放的视频
     *
     * @param dataSource 视频的完整路径
     */
    public void setup(String dataSource) {
        try {
            setDataSource(dataSource);
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化本地多媒体文件
     *
     * @param context
     * @param fileName
     */
    public void setupLocalData(Context context, String fileName) {
        create(context, getResIdByName(context, fileName));
    }

    /**
     * 初始化多媒体文件
     *
     * @param context
     * @param fileName
     */
    public void setupAssets(Context context, String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
            setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            prepare();
            assetFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化多媒体文件
     *
     * @param context
     * @param fileName
     */
    public void setupAssetsAsync(Context context, String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
            setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            prepareAsync();
            assetFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        if (isPlaying()) {
            super.pause();
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
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 10);
    }

    private int getResIdByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "raw", context.getPackageName());
    }

    static class PlayerHandler extends Handler {
        WeakReference<HiiMediaPlayer> mPlayer;

        PlayerHandler(HiiMediaPlayer player) {
            mPlayer = new WeakReference<>(player);
        }

        @Override
        public void handleMessage(Message msg) {
            HiiMediaPlayer player = mPlayer.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_MESSAGE_ID:
                    if (player.isPlaying()) {
                        player.pause();
                    }
                    break;

                default:
                    break;
            }

        }
    }
}
