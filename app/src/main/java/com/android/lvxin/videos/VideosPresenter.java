package com.android.lvxin.videos;

import android.content.Context;

import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.data.source.MediasDataSource;
import com.android.lvxin.data.source.MediasRepository;
import com.android.lvxin.videoplayer.CombineFiles;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: VideosPresenter
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 16:35
 */
public class VideosPresenter implements VideosContract.Presenter {
    private static final String TAG = "VideosPresenter";
    private final MediasRepository videosRepository;
    private final VideosContract.View videosView;

    private List<VideoInfo> mVideoData = new ArrayList<>();

    public VideosPresenter(MediasRepository videosRepository, VideosContract.View videosView) {
        this.videosRepository = videosRepository;
        this.videosView = videosView;

        videosView.setPresenter(this);
    }

    @Override
    public void loadVideos() {
        videosRepository.getVideos(new MediasDataSource.LoadVideosCallback() {
            @Override
            public void onVideosLoaded(final List<VideoInfo> videos) {
                mVideoData = videos;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CombineFiles cf = new CombineFiles(videosView.getContext());
                        cf.start(videos);
                    }
                }).start();
                // show the videos to ui
                if (videos.isEmpty()) {
                    videosView.showNoVideos();
                } else {
                    videosView.showVideos(videos);
                }
            }

            @Override
            public void onDataNotAvailable() {
                videosView.showLoadingVideosError();
            }
        });
    }

    @Override
    public void openVideo(VideoInfo requestedVideo) {
        // TODO
        if (null != requestedVideo) {
        }
    }

    @Override
    public void openVideos(List<VideoInfo> requestedVideos) {
        videosView.openVideoDetailUi(mVideoData);
    }

    @Override
    public void start(Context context) {
        loadVideos();
    }

}
