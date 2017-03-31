package com.android.lvxin.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: VideoInfo
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 15:51
 */
public class VideoInfo implements Serializable {
    private int videoId;
    private String name;
    private String videoPath;
    private String fileName;
    private int videoSize;
    private long duration;
    private long videoProgress;
    private int repeats = 5;
    private int restTime = 10;
    private int type = 0; // 按秒数或者按次数
    private List<AudioInfo> audios;
    private AudioInfo audio;

    public VideoInfo() {

    }

    public VideoInfo(int videoId, String videoPath, String fileName, int videoSize, long duration) {
        this.videoPath = videoPath;
        this.videoId = videoId;
        this.fileName = fileName;
        this.duration = duration;
        this.videoSize = videoSize;
        this.videoProgress = 0;
        this.repeats = 5;
        this.restTime = 10;
    }

    public VideoInfo(String fileName, long duration, String name, int repeats, int type, int restTime, List<AudioInfo> audios) {
        this.fileName = fileName;
        this.duration = duration;
        this.restTime = restTime;
        this.repeats = repeats;
        this.type = type;
        this.name = name;
        this.audios = audios;
    }

    public AudioInfo getAudio() {
        return audio;
    }

    public void setAudio(AudioInfo audio) {
        this.audio = audio;
    }

    public int getAudioSize() {
        if (null == audios || audios.isEmpty()) {
            return 0;
        } else {
            return audios.size();
        }
    }

    public List<String> getAudioPathList(String absolutePath) {
        if (null == audios || audios.isEmpty()) return new ArrayList<>(0);
        List<String> data = new ArrayList<>(getAudioSize());
        for (AudioInfo audio : audios) {
            data.add(absolutePath + "/audios/" + audio.getFileName() + ".mp3");
        }
        return data;
    }

    public AudioInfo getAudio(int index) {
        if (getAudioSize() > index) {
            return audios.get(index);
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(int videoSize) {
        this.videoSize = videoSize;
    }

    public long getTotalDuration() {
        return duration * repeats;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getVideoProgress() {
        return videoProgress;
    }

    public void setVideoProgress(long videoProgress) {
        this.videoProgress = videoProgress;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<AudioInfo> getAudios() {
        return audios;
    }

    public void setAudios(List<AudioInfo> audios) {
        this.audios = audios;
    }
}
