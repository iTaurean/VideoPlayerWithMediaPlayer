package com.android.lvxin.data;

import java.io.Serializable;

/**
 * @ClassName: AudioInfo
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/16/16 10:38
 */
public class AudioInfo implements Serializable {
    public int audioId;
    public String audioPath;
    public String audioName;
    public int audioSize;
    public long audioDuration;
    public long audioProgress;
    public int repeats;

    public AudioInfo(int audioId, String audioPath, String audioName, int audioSize, long audioDuration) {
        this.audioPath = audioPath;
        this.audioId = audioId;
        this.audioName = audioName;
        this.audioDuration = audioDuration;
        this.audioSize = audioSize;
        this.audioProgress = 0;
        this.repeats = 2;
    }
}
