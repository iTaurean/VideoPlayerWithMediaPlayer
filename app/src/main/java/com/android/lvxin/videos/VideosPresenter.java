package com.android.lvxin.videos;

import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.data.source.MediasDataSource;
import com.android.lvxin.data.source.MediasRepository;

import java.util.List;

/**
 * @ClassName: VideosPresenter
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 16:35
 */
public class VideosPresenter implements VideosContract.Presenter {
    private final MediasRepository videosRepository;
    private final VideosContract.View videosView;

    public VideosPresenter(MediasRepository videosRepository, VideosContract.View videosView) {
        this.videosRepository = videosRepository;
        this.videosView = videosView;

        videosView.setPresenter(this);
    }

    @Override
    public void loadVideos() {
        videosRepository.getVideos(new MediasDataSource.LoadVideosCallback() {
            @Override
            public void onVideosLoaded(List<VideoInfo> videos) {
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
        if (null != requestedVideos) {
            videosView.openVideoDetailUi(requestedVideos);

        }
    }

    @Override
    public void start() {
        loadVideos();
    }
}
