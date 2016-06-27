package com.android.lvxin.data.source;

import com.android.lvxin.data.AudioInfo;
import com.android.lvxin.data.VideoInfo;

import java.util.List;


/**
 * @ClassName: MediasDataSource
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 15:58
 */
public interface MediasDataSource {

    void getVideos(LoadVideosCallback callback);

    void getAudios(LoadAudiosCallback callback);

    interface LoadVideosCallback {
        void onVideosLoaded(List<VideoInfo> videos);

        void onDataNotAvailable();
    }

    interface LoadAudiosCallback {
        void onAudiosLoaded(List<AudioInfo> audios);

        void onDataNotAvailable();
    }
}
