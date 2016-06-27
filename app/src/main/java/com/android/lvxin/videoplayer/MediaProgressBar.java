package com.android.lvxin.videoplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.android.lvxin.R;

/**
 * @ClassName: MediaProgressBGView
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/17/16 10:17
 */
public class MediaProgressBar extends View {

    private static final String TAG = "MediaProgressBar";

    private float maxValue; // 进度条的最大值
    private float currentValue; // 进度条当前值
    private int maxClips = 5; // 进度条平分的总段数
    private int currentClips = 1; // 当前播放的视频段位置
    private long currentDuration = 10; // 当前播放视频的总时长
    private float widthPerClip; // 每段进度条的长度
    private int currentPassedTimes = 0; // 当前视频播放的时间

    private Paint clipPaint;
    private Paint progressPaint;

    private int tipWidth; // 断点宽度

    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
            ++currentPassedTimes;
            currentValue += (widthPerClip / currentDuration);

            if (maxValue == 0 || currentValue < maxValue) {
                sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    public MediaProgressBar(Context context) {
        super(context);
        initView(context);
    }

    public MediaProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        clipPaint = new Paint();
        progressPaint = new Paint();
        tipWidth = dip2px(context, 5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        progressPaint.setColor(context.getResources().getColor(R.color.black_trans_85));
        canvas.drawRect(0, 0, getWidth(), getHeight(), progressPaint);

        progressPaint.setColor(context.getResources().getColor(R.color.theme_color));
        canvas.drawRect(0, 0, currentValue, getHeight(), progressPaint);

        clipPaint.setColor(context.getResources().getColor(R.color.white));

        for (int i = 0; i < maxClips; i++) {
            int left = (int) widthPerClip * (i + 1);
            int right = left + tipWidth;
            canvas.drawRect(left, 0, right, getHeight(), clipPaint);
        }
    }

    /**
     * 初始化并开始进度条
     *
     * @param maxClips
     * @param duration
     */
    public void onPrepare(final int maxClips, long duration) {
        this.maxClips = maxClips;
        this.currentDuration = duration;
        this.post(new Runnable() {
            @Override
            public void run() {
                maxValue = getWidth();
                widthPerClip = maxValue / maxClips;
                currentValue = (currentClips - 1) * widthPerClip;
            }
        });
    }

    /**
     * 延迟指定时间开始播放进度条
     *
     * @param delay 延迟毫秒数
     */
    public void onStartDelay(long delay) {
        handler.sendEmptyMessageDelayed(0, delay);
    }

    /**
     * 开始播放进度条
     */
    public void onStart() {
        handler.sendEmptyMessage(0);
    }


    /**
     * 开始已经准备就绪的下一个动作的进度条
     */
//    public void onStart() {
//        handler.sendEmptyMessageDelayed(0, 1000);
//    }

    /**
     * 更新进度条
     *
     * @param isNext
     * @param mediaDuration
     * @description 1. 进入休息页面时；2. 跳到下一个时；3. 跳到上一个时；这三种情况下修正进度条
     */
    public void onUpdate(boolean isNext, long mediaDuration) {
        handler.removeMessages(0);
        currentPassedTimes = 0;
        currentDuration = mediaDuration;
        currentClips = isNext ? currentClips + 1 : currentClips - 1;
        currentValue = (currentClips - 1) * widthPerClip;
        invalidate();
    }

    public void onPause() {
        if (null != handler) {
            handler.removeMessages(0);
        }

    }

    public void onResume() {
        if (null != handler) {
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    public void onStop() {
        currentPassedTimes = 0;
        currentClips = 0;
        if (null != handler) {
            handler.removeMessages(0);
        }
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    interface OnPreparedCallback {
        void onPrepared();
    }
}
