package com.android.lvxin.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;

import com.android.lvxin.listener.OnCompletedListener;
import com.android.lvxin.widget.HiiMediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AudioPlayerController
 * @Description: 播放音频的逻辑处理
 * @Author: lvxin
 * @Date: 7/4/16 08:56
 */
public class AudioPlayerController {
    private static final String TAG = "AudioPlayerController";
    private static String CALLBACK_VALUE = "misc_audio_1_4";

    private static AudioPlayerController INSTANCE;

    private List<String> data;
    private int playedPosition;
    private int currentIndex;
    private boolean needCallback;
    private List<HiiMediaPlayer> playerList = new ArrayList<>();
    private int currentPlayedIndex;

    private int schemeId;
    private Context context;

    private boolean isContainsAllAudios = true; // 需要的音频目录中是否都存在

    private OnCompletedListener onCompletedListener;

    private AudioPlayerController(Context context) {
        this.context = context;
    }

    public static AudioPlayerController getInstance(Context context) {
        INSTANCE = new AudioPlayerController(context);
        return INSTANCE;
    }

    /**
     * @param schemeId
     */
    public void updateData(int schemeId) {
        this.schemeId = schemeId;
    }

    /**
     * @param data
     * @param needCallback
     */
    public void update(List<String> data, boolean needCallback) {
        this.needCallback = needCallback;
        playedPosition = 0;
        reset();
        this.currentIndex = 0;
        onRelease();
        playerList.clear();
        if (null == data || data.isEmpty()) {
            this.data = new ArrayList<>(0);
        } else {
            this.data = data;
            setup();
        }
    }

    /**
     * @param data
     * @param index
     * @param needCallback
     */
    public void update(List<String> data, int index, boolean needCallback) {
        this.needCallback = needCallback;
        this.currentIndex = index;
        reset();
        onRelease();
        playerList.clear();
        if (null == data || data.isEmpty()) {
            this.data = new ArrayList<>(0);
        } else {
            this.data = data;
            setup();
        }
    }

    /**
     * @param data
     * @param index
     * @param position
     * @param needCallback
     */
    public void update(List<String> data, int index, int position, boolean needCallback) {
        this.needCallback = needCallback;
        playedPosition = position;
        this.currentIndex = index;
        reset();
        onRelease();
        playerList.clear();
        if (null == data || data.isEmpty()) {
            this.data = new ArrayList<>(0);
        } else {
            this.data = data;
            setup();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setup() {
        isContainsAllAudios = true;
        playerList = new ArrayList<>(data.size());
        initAudio(data.get(0));

        try {
            for (int i = 1; i < data.size(); i++) {
                initAudio(data.get(i));
                if (isContainsAllAudios && i < playerList.size()) {
                    if (null != playerList.get(i - 1) && null != playerList.get(i)) {
                        playerList.get(i - 1).setNextMediaPlayer(playerList.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAudio(String fileName) {
        if (isContainsAllAudios) {
            HiiMediaPlayer mediaPlayer = new HiiMediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onPlayCompleted();
                }
            });

            if (fileName.startsWith("misc_")) {
                mediaPlayer.setupAssets(context, fileName + ".mp3");
                playerList.add(mediaPlayer);
            } else {
//                String dataSource = getPath(fileName);
//                // 判断对应的音频文件是否存在
//                if (FileUtils.isExistFile(dataSource)) {
//                    mediaPlayer.setup(dataSource);
//                    playerList.add(mediaPlayer);
//                } else {
//                    isContainsAllAudios = false;
//                }

                mediaPlayer.setupAssets(context, schemeId + "/" + fileName + ".mp3");
                playerList.add(mediaPlayer);
            }
        } else {
            playerList.clear();
        }
    }

    private void onPlayCompleted() {
        if (needCallback) {
            if ((currentIndex >= playerList.size() - 1) && null != onCompletedListener) {
                onCompletedListener.onCompleted();
            }
            ++currentIndex;
        }
    }

    /**
     *
     */
    public void start() {
        if (null != playerList && !playerList.isEmpty()) {
            playerList.get(0).start();
        }
    }

    /**
     * @param index
     */
    public void start(int index) {
        if (null != playerList && !playerList.isEmpty() && playerList.size() > index) {
            playerList.get(index).start();
        }
    }

    /**
     * @param index
     * @param position
     */
    public void seekToStart(int index, int position) {
        if (null != playerList && !playerList.isEmpty() && playerList.size() > index) {
            playerList.get(index).seekTo(position);
            playerList.get(index).start();
        }
    }

    /**
     *
     */
    public void pause() {
        for (int i = 0; i < playerList.size(); i++) {
            HiiMediaPlayer player = playerList.get(i);
            if (null != player && player.isPlaying()) {
                playedPosition = player.getCurrentPosition();
                player.pause();
                currentPlayedIndex = i;
            }
        }
    }

    /**
     *
     */
    public void resumeAndStart() {
        if (currentPlayedIndex < playerList.size()) {
            if (!playerList.get(currentPlayedIndex).isPlaying()) {
                playerList.get(currentPlayedIndex).seekToStart(playedPosition);
            }
        }
    }

    /**
     *
     */
    public void resumeAndPause() {
        if (currentPlayedIndex < playerList.size()) {
            playerList.get(currentPlayedIndex).seekTo(playedPosition);
        }
    }

    /**
     *
     */
    public void reset() {
        if (null != playerList) {
            for (HiiMediaPlayer obj : playerList) {
                if (null != obj) {
                    obj.reset();
                }
            }
        }
    }

    public int getCurrentIndex() {
        return currentPlayedIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentPlayedIndex = index;
    }

    public int getPausePosition() {
        return playedPosition;
    }

    public void setPausePosition(int position) {
        this.playedPosition = position;
    }

    /**
     *
     */
    public void onRelease() {
        try {
            if (null != playerList) {
                for (int i = playerList.size() - 1; i >= 0; i--) {
                    if (null != playerList.get(i)) {
                        playerList.get(i).release();
                    }
                }
                playerList.clear();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void setOnCompletedListener(OnCompletedListener listener) {
        this.onCompletedListener = listener;
    }

    /**
     * 获取音频完整路径
     *
     * @param fileName
     * @return
     */
//    private String getPath(String fileName) {
//        return BaseApplication.getVideoDir() + schemeId + "/" + fileName + ".mp3";
//    }
}
