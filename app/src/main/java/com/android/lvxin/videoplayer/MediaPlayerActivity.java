package com.android.lvxin.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.android.lvxin.R;
import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.data.source.MediasRepository;
import com.android.lvxin.data.source.local.MediasLocalDataSource;
import com.android.lvxin.data.source.remote.MediasRemoteDataSource;
import com.android.lvxin.util.ActivityUtils;

import java.io.Serializable;
import java.util.List;

public class MediaPlayerActivity extends AppCompatActivity {
    private static final String TAG = "MediaPlayerActivity";
    private static final String EXTRA_VIDEOS = "extra_videos";
    private MediaPlayerContract.Presenter mPresenter;

    public static void start(Context context, List<VideoInfo> videoInfos) {
        Intent intent = new Intent(context, MediaPlayerActivity.class);
        intent.putExtra(EXTRA_VIDEOS, (Serializable) videoInfos);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media_player);

        List<VideoInfo> videoInfos = (List<VideoInfo>) getIntent().getSerializableExtra(EXTRA_VIDEOS);

        MediaPlayerFragment videoPlayerFragment = (MediaPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (null == videoPlayerFragment) {
            videoPlayerFragment = MediaPlayerFragment.getInstance(videoInfos);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    videoPlayerFragment,
                    R.id.content_frame);
        }

        MediasRepository repository = new MediasRepository(
                MediasLocalDataSource.getInstance(this),
                MediasRemoteDataSource.getInstance());
        mPresenter = new MediaPlayerPresenter(repository, videoPlayerFragment);
    }

}
