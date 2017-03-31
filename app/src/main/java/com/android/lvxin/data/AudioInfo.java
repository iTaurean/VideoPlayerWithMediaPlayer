package com.android.lvxin.data;

import java.io.Serializable;

/**
 * @ClassName: AudioInfo
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/16/16 10:38
 */
public class AudioInfo implements Serializable {
    public int id;
    public String path;
    public int size;
    public long audioProgress;
    private String fileName;
    private long duration;
    private int repeats;

    public AudioInfo() {
    }

    public AudioInfo(String fileName) {
        this.fileName = fileName;
    }

    public AudioInfo(int id, String path, String fileName, int size, long audioDuration) {
        this.path = path;
        this.id = id;
        this.fileName = fileName;
        this.duration = audioDuration;
        this.size = size;
        this.audioProgress = 0;
        this.repeats = 2;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }
}
