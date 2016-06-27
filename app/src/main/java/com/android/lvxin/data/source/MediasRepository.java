package com.android.lvxin.data.source;

/**
 * @ClassName: MediasRepository
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 16:03
 */
public class MediasRepository implements MediasDataSource {

    private static MediasRepository INSTANCE = null;

    private final MediasDataSource localDataSource;
    private final MediasDataSource remoteDataSource;

    public MediasRepository(MediasDataSource localDataSource, MediasDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

    }

    public static MediasRepository getInstance(MediasDataSource localDataSource, MediasDataSource remoteDataSource) {
        if (null == INSTANCE) {
            INSTANCE = new MediasRepository(localDataSource, remoteDataSource);
        }

        return INSTANCE;
    }

    public static void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public void getVideos(LoadVideosCallback callback) {
        if (null != callback) {
            localDataSource.getVideos(callback);
        }
    }

    @Override
    public void getAudios(LoadAudiosCallback callback) {
        if (null != callback) {
            localDataSource.getAudios(callback);
        }
    }

}