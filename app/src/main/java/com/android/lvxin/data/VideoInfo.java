package com.android.lvxin.data;

import java.io.Serializable;

/**
 * @ClassName: VideoInfo
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 15:51
 */
public class VideoInfo implements Serializable {
    public int videoId;
    public String videoPath;
    public String videoName;
    public int videoSize;
    public long videoDuration;
    public long videoProgress;
    public int repeats;
    public int restTime;

    public VideoInfo(int videoId, String videoPath, String videoName, int videoSize, long videoDuration) {
        this.videoPath = videoPath;
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoDuration = videoDuration / 1000;
        this.videoSize = videoSize;
        this.videoProgress = 0;
        this.repeats = 2;
        this.restTime = 10;
    }
}
