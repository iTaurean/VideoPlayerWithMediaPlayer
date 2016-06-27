package com.android.lvxin.videos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.lvxin.R;
import com.android.lvxin.data.source.MediasRepository;
import com.android.lvxin.data.source.local.MediasLocalDataSource;
import com.android.lvxin.data.source.remote.MediasRemoteDataSource;
import com.android.lvxin.util.ActivityUtils;

public class VideosActivity extends AppCompatActivity {

    private VideosPresenter mVideosPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);


        VideosFragment videosFragment = (VideosFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (null == videosFragment) {
            videosFragment = VideosFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), videosFragment, R.id.content_frame);
        }

        // Create the presenter
        MediasRepository videosRepository = MediasRepository.getInstance(
                MediasLocalDataSource.getInstance(this),
                MediasRemoteDataSource.getInstance());
        mVideosPresenter = new VideosPresenter(videosRepository, videosFragment);

    }
}
