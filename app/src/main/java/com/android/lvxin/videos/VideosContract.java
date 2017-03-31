package com.android.lvxin.videos;

import android.content.Context;

import com.android.lvxin.BasePresenter;
import com.android.lvxin.BaseView;
import com.android.lvxin.data.VideoInfo;

import java.util.List;

/**
 * @ClassName: VideosContract
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 15:48
 */
public interface VideosContract {
    interface View extends BaseView<Presenter> {

        void showVideos(List<VideoInfo> videos);

        void showNoVideos();

        void openVideoDetailUi(List<VideoInfo> videos);

        void showLoadingVideosError();

        String getProjectDir();
        Context getContext();

    }

    interface Presenter extends BasePresenter {

        void loadVideos();

        void openVideo(VideoInfo requestedVideo);

        void openVideos(List<VideoInfo> requestedVideos);
    }
}
