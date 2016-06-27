package com.android.lvxin.videos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.lvxin.R;
import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.util.InsetDecoration;
import com.android.lvxin.util.Tools;
import com.android.lvxin.videoplayer.MediaPlayerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: VideosFragment
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 16:34
 */
public class VideosFragment extends Fragment implements VideosContract.View {

    private VideosContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private StaggeredAdapter mAdapter;
    private int mItemMargin;
    private StaggeredAdapter.OnStaggeredAdapterListener mOnStaggeredAdapterListener = new StaggeredAdapter.OnStaggeredAdapterListener() {
        @Override
        public void onStaggeredAdapterInformation() {
            mRecyclerView.setVisibility(View.GONE);
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO
            VideoInfo videoInfo = mAdapter.getVideoItemAtPosition(position);
            List<VideoInfo> videoInfos = new ArrayList<>();
            videoInfos.add(videoInfo);
            if (mAdapter.getItemCount() > 1) {
                if (0 == position) {
                    videoInfos.add(mAdapter.getVideoItemAtPosition(position + 1));
                } else {
                    videoInfos.add(mAdapter.getVideoItemAtPosition(0));
                }
            }
            mPresenter.openVideos(videoInfos);
        }
    };

    public VideosFragment() {
    }

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_margin);
        mAdapter = new StaggeredAdapter(getContext(), (Tools.getWindowWidth(getActivity()) - 6 * mItemMargin) / 2, mOnStaggeredAdapterListener);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragemtn_videos, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.videos_list_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new InsetDecoration(getContext(), mItemMargin));
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showVideos(List<VideoInfo> videos) {

    }

    @Override
    public void showNoVideos() {

    }

    @Override
    public void openVideoDetailUi(List<VideoInfo> videos) {
        MediaPlayerActivity.start(getContext(), videos);
    }

    @Override
    public void setPresenter(VideosContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingVideosError() {

    }

}
