package com.android.lvxin.controller;

import android.content.Context;
import android.media.MediaPlayer;

import com.android.lvxin.widget.HiiMediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: HeartRateAudioController
 * @Description: 心率值在不同区间不同音频的控制
 * @Author: lvxin
 * @Date: 7/7/16 09:49
 */
public class HeartRateAudioController {

    private static final String TAG = "RateAudioController";

    private static final String AUDIO_HEART_RATE_TIPS = "misc_audio_3_61";
    private static final String AUDIO_HEART_RATE_LOW = "misc_audio_3_62";
    private static final String AUDIO_HEART_RATE_HIGH = "misc_audio_3_63";

    private static HeartRateAudioController INSTANCE;

    private HiiMediaPlayer player;

    private Context context;

    private int lowest; // 有效心率区间最低值
    private int highest; // 有效心率区间最高值
    private int currentHeartRate;

    private int maxRateValue; // 运动过程中达到的最高心率
    private int minRateValue; // 运动过程中达到的最低心率
    private int totalRateValue; // 运动过程中的每次心率相加总和
    private int rateCount; // 心率计数器
    private long millisecondInAvailable; // 在有效心率范围内的有效运动时长(毫秒)
    private long beginMillisecond; // 在有效心率范围内的开始时间（毫秒）

    private long highestPlayTime; // 心率达到最高值语音播放时间
//    private long lowestPlayTime; // 心率达到最低值语音播放时间

    private boolean hasReachLowestRate; // 是否已经达到过最低心率

    private List<Integer> heartRates; // 记录每次更新的心跳

    private HeartRateAudioController(Context context, int lowest, int highest) {
        this.context = context;
        this.lowest = lowest;
        this.highest = highest;

        highestPlayTime = 0;
//        lowestPlayTime = 0;

        this.currentHeartRate = 0;
        this.maxRateValue = 0;
        this.minRateValue = 0;
        this.minRateValue = 0;
        this.rateCount = 0;
        this.millisecondInAvailable = 0;
        this.heartRates = new ArrayList<>();
        this.hasReachLowestRate = false;
    }

    public static HeartRateAudioController getInstance(Context context, int lowest, int highest) {
        INSTANCE = new HeartRateAudioController(context, lowest, highest);
        return INSTANCE;
    }

    /**
     * 播放当前心跳对应的音频
     *
     * @param currentHeartRate
     * @desc 1.实时心率首次达到适宜区间最低值；
     * 2.实时心率超过适宜区间最高值
     */
    public void play(int currentHeartRate) {
        this.currentHeartRate = currentHeartRate;

        calculateHeartRate();
        calculateMillisecondInAvailableRate();

        if (null != heartRates) {
            heartRates.add(currentHeartRate);
        }
        if (currentHeartRate >= highest) {
            long interval = System.currentTimeMillis() - highestPlayTime;
            if (highestPlayTime == 0 || interval >= 30000) {
                create(AUDIO_HEART_RATE_HIGH);
                highestPlayTime = System.currentTimeMillis();
            }
        } else if (currentHeartRate >= lowest && currentHeartRate < highest) {
            if (!hasReachLowestRate) {
                //达到有效心率音频只播放一次
                create(AUDIO_HEART_RATE_LOW);
                hasReachLowestRate = true;
            }
        }
    }

    /**
     * 播放当前心跳对应的音频
     *
     * @desc 1.视频播放5分钟内，实时心率未达到适宜区间的最低值；
     */
    public void play() {
        boolean up2Lowest = false;
        for (Integer value : heartRates) {
            if (value >= lowest) {
                up2Lowest = true;
                break;
            }
        }
        if (!up2Lowest) {
            create(AUDIO_HEART_RATE_TIPS);
        }
        heartRates.clear();
        heartRates = null;
    }

    /**
     * 创建资源
     * @param fileName
     */
    public void create(String fileName) {
        if (null == player) {
            player = new HiiMediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onRelease();
                }
            });
        } else {
            player.reset();
        }
        prepareAndStart(fileName);
    }

    /**
     * 准备资源并开始
     *
     * @param fileName
     */
    public void prepareAndStart(String fileName) {
        player.reset();
        player.setupAssetsAsync(context, fileName + ".mp3");
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });
    }

    /**
     * 开始
     */
    public void start() {
        if (null != player && !player.isPlaying()) {
            player.start();
        }
    }

    /**
     * 获取平均心率
     *
     * @return
     */
    public int getAvgHeartRate() {
        if (0 != rateCount) {
            return totalRateValue / rateCount;
        }
        return 0;
    }

    /**
     * 获取运动过程中达到的最高心率
     *
     * @return
     */
    public int getMaxRateValue() {
        return maxRateValue;
    }

    /**
     * 获取运动过程中达到的最低心率
     *
     * @return
     */
    public int getMinRateValue() {
        return minRateValue;
    }

    /**
     * 获取在心率范围内的运动时长
     *
     * @return 秒数
     */
    public int getSecondsInAvailable() {
        additionMillisecond();
        return (int) millisecondInAvailable / 1000;
    }

    private void calculateHeartRate() {
        if (currentHeartRate > 0) {
            maxRateValue = Math.max(maxRateValue, currentHeartRate);
            if (minRateValue == 0) {
                minRateValue = currentHeartRate;
            } else {
                minRateValue = Math.min(minRateValue, currentHeartRate);
            }
            ++rateCount;
            totalRateValue += currentHeartRate;
        }
    }

    /**
     * 计算在有效心率范围内的运动时长
     */
    private void calculateMillisecondInAvailableRate() {
        if (currentHeartRate >= lowest && currentHeartRate <= highest) {
            if (0 == beginMillisecond) {
                beginMillisecond = System.currentTimeMillis();
            }
        } else {
            additionMillisecond();
        }
    }

    /**
     * 累加有效运动时长
     */
    private void additionMillisecond() {
        if (0 != beginMillisecond) {
            millisecondInAvailable += (System.currentTimeMillis() - beginMillisecond);
            beginMillisecond = 0;
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
