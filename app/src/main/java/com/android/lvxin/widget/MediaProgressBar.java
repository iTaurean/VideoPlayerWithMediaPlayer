package com.android.lvxin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.lvxin.R;

/**
 * @ClassName: MediaProgressBar
 * @Description: 视频播放进度条
 * @Author: lvxin
 * @Date: 6/17/16 10:17
 */
public class MediaProgressBar extends View {

    private static final String TAG = "MediaProgressBar";

    private float maxValue; // 进度条的最大值
    private float currentValue; // 进度条当前值
    private float savedCurrentValue; // 暂停时的进度值
    private int maxClips = 5; // 进度条平分的总段数
    private int currentClips = 0; // 当前播放的视频段位置
    private long currentDuration = 10; // 当前播放视频的总时长
    private float widthPerClip; // 每段进度条的长度

    private Paint clipPaint;
    private Paint progressPaint;

    private int tipWidth; // 断点宽度

    private long latestSendMillins = 0;

    private Context context;
    private OnProgressUpdateListener onProgressUpdateListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: ");
            Log.i(TAG, "handleMessage: time=" + System.currentTimeMillis());
            if (System.currentTimeMillis() - latestSendMillins < 1000) {
                Log.i(TAG, "handleMessage: less 1000 millis");
                return;
            }else {
                latestSendMillins = System.currentTimeMillis();
                invalidate();
                currentValue += (widthPerClip / currentDuration);
                float currentMaxValue = currentClips * widthPerClip;
                currentValue = Math.min(currentMaxValue, currentValue);
                if (null != onProgressUpdateListener) {
                    onProgressUpdateListener.onUpdate(currentClips, currentValue);
                }

                if (maxValue == 0 || currentValue < maxValue || currentValue <= currentMaxValue) {
                    sendEmptyMessageDelayed(0, 1000);
                }
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

    public void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        this.onProgressUpdateListener = listener;
    }

    private void initView(Context context) {
        this.context = context;
        clipPaint = new Paint();
        progressPaint = new Paint();
        tipWidth = dip2px(context, 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        savedCurrentValue = currentValue = Math.max(currentValue, savedCurrentValue);
        progressPaint.setColor(ContextCompat.getColor(context, R.color.progress_bg_color));
        canvas.drawRect(0, 0, getWidth(), getHeight(), progressPaint);

        progressPaint.setColor(ContextCompat.getColor(context, R.color.theme_color));
        canvas.drawRect(0, 0, currentValue, getHeight(), progressPaint);

        clipPaint.setColor(ContextCompat.getColor(context, android.R.color.white));

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
     */
    public void onPrepare(final int maxClips) {
        this.maxClips = maxClips;
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
        Log.i(TAG, "onUpdate: ");
        savedCurrentValue = 0;
        handler.removeMessages(0);
        currentDuration = mediaDuration;
        currentClips = isNext ? currentClips + 1 : currentClips - 1;
        currentValue = (currentClips - 1) * widthPerClip;
        invalidate();
    }

    /**
     * 更新
     *
     * @param currentClips
     * @param currentValue
     * @param mediaDuration
     */
    public void onUpdate(int currentClips, float currentValue, long mediaDuration) {
        Log.i(TAG, "onUpdate: ");
        savedCurrentValue = 0;
        handler.removeMessages(0);
        this.currentDuration = mediaDuration;
        if (currentClips > 1) {
            this.currentClips = currentClips;
        } else {
            this.currentClips = 1;
        }
        this.currentValue = currentValue;

        postInvalidate();
    }

    /**
     * 暂停
     */
    public void onPause() {
        Log.i(TAG, "onPause: ");
        if (null != handler) {
            handler.removeMessages(0);
        }
        savedCurrentValue = currentValue;
    }

    /**
     * 暂停
     *
     * @param position
     */
    public void onPause(float position) {
        Log.i(TAG, "onPause: ");
        if (null != handler) {
            handler.removeMessages(0);
        }
        float pausePosition = (currentClips - 1) * widthPerClip
                + (widthPerClip / currentDuration) * position;
        savedCurrentValue = Math.min(currentValue, pausePosition);
    }

    /**
     * 继续
     */
    public void onResume() {
        Log.i(TAG, "onResume: ");
        currentValue = savedCurrentValue;
        if (null != handler) {
            handler.sendEmptyMessage(0);
        }
    }

    /**
     * 停止
     */
    public void onStop() {
        Log.i(TAG, "onStop: ");
        currentClips = 0;
        if (null != handler) {
            handler.removeMessages(0);
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 更新进度条监听事件
     */
    public interface OnProgressUpdateListener {
        /**
         * 更新事件
         *
         * @param currentClip
         * @param currentValue
         */
        void onUpdate(int currentClip, float currentValue);
    }
}
