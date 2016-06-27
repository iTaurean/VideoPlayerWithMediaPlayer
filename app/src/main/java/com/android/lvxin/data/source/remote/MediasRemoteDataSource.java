package com.android.lvxin.data.source.remote;

import com.android.lvxin.data.source.MediasDataSource;

/**
 * @ClassName: MediasRemoteDataSource
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 16:04
 */
public class MediasRemoteDataSource implements MediasDataSource {

    public static MediasRemoteDataSource INSTANCE;

    private MediasRemoteDataSource() {
    }

    public static MediasRemoteDataSource getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new MediasRemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getVideos(LoadVideosCallback callback) {
        // TODO get video from server
    }

    @Override
    public void getAudios(LoadAudiosCallback callback) {

    }
}
