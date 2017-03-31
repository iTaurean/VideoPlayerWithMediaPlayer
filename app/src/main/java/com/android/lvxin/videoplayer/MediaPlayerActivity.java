package com.android.lvxin.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.lvxin.R;
import com.android.lvxin.base.BaseAppCompatActivity;
import com.android.lvxin.model.MediaPlayerArgs;
import com.android.lvxin.util.ViewUtils;

/**
 * @ClassName: MediaPlayerActivity
 * @Description: 视频播放页
 * @Author: lvxin
 * @Date: 7/19/16 21:46
 */
public class MediaPlayerActivity extends BaseAppCompatActivity {

    private static final String TAG = "MediaPlayerActivity";
    private static final String EXTRA_DATA = "extra_data";

    private MediaPlayerFragment videoPlayerFragment;
    private MediaPlayerContract.Presenter mPresenter;

    private MediaPlayerArgs mData;

    //    private BandConnectStatusBroadcast receiver;
    private IntentFilter intentFilter;

    /**
     * 跳转
     *
     * @param context 上下文
     * @param arg     参数对象
     */
    public static void start(Context context, MediaPlayerArgs arg) {
        Intent intent = new Intent(context, MediaPlayerActivity.class);
        intent.putExtra(EXTRA_DATA, arg);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media_player);

        mData = (MediaPlayerArgs) getIntent().getSerializableExtra(EXTRA_DATA);
        videoPlayerFragment = (MediaPlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (null == videoPlayerFragment) {
            videoPlayerFragment = MediaPlayerFragment.getInstance(mData);
            ViewUtils.addFragmentToActivity(getSupportFragmentManager(), videoPlayerFragment,
                    R.id.content_frame);
        }
        mPresenter = MediaPlayerPresenter.getInstance(videoPlayerFragment);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            final ConfirmTitleDialog dialog = new ConfirmTitleDialog(this,
//                    getString(R.string.dialog_over_plan_video_content), R.string.stage_over,
//                    R.string.btn_continue);
//            dialog.setLeftButtonListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                    dialog.dismiss();
//                }
//            });
//            dialog.setRightButtonListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
            Toast.makeText(this, "show a dialog to close this page.", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
