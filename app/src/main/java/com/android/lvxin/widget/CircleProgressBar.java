package com.android.lvxin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @ClassName: CircleProgressBar
 * @Description: 圆形进度条
 * @Author: lvxin
 * @Date: 6/22/16 14:10
 */
public class CircleProgressBar extends View {
    private static final String TAG = "CircleProgressBar";

    private static final int HANDLER_MESSAGE_ID = 20;
    private long handlerSendMessageTime;
    private long pauseTime;
    private int offset; // 圆形进度条与外边界的距离
    private int maxProgress = 100; // 最大值
    private int currentProgress = 0; // 当前进度
    private RectF oval;
    private Paint paint;
    private int progressStrokeWidth = 4; // 先宽度
    private int bgColor = 0; // 背景色
    private int ringColor; // 进度条背景色
    private int progressColor; // 进度条颜色
    private int textSize; // 文字字体大小
    private int textSize2;
    private int textColor; // 文字颜色
    private Context mContext;
    private TextFormat textFormat = TextFormat.CURRENT_PROGRESS;
    private boolean isDrawArc = true;
    private boolean isCountDown = false;
    private OnProgressUpdateListener onProgressUpdateListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            ++currentProgress;
            if (null != onProgressUpdateListener) {
                onProgressUpdateListener.onProgressUpdate(currentProgress);
            }
            if (currentProgress < maxProgress) {
                handlerSendMessageTime = System.currentTimeMillis();
                sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1000);
            }
        }
    };

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener onProgressUpdateListener) {
        this.onProgressUpdateListener = onProgressUpdateListener;
    }

    private void initView(Context context) {
        mContext = context;
        oval = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为抗锯齿
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    
        //如果为确定大小值，则圆的半径为宽度/2
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            radius = widthSize / 2;
            x = widthSize / 2;
            y = heightSize / 2;
            width = widthSize;
            height = heightSize;
        }
        //如果为wrap_content 那么View大小为圆的半径大小*2
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            width = (int) (radius * 2);
            height = (int) (radius * 2);
            x = radius;
            y = radius;
    
        }
        diameter = radius * 2;
        //设置视图的大小
        setMeasuredDimension(width, height);
    }
    */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height); // 直径
        int redious = diameter / 2; // 半径

        //绘制背景
        drawBackground(canvas, redious, redious, redious);
        // 绘制圆形进度条
        drawProgress(canvas, diameter);
        // 绘制文字
        drawText(canvas, redious);

    }

    /**
     * 绘制圆形背景
     *
     * @param canvas
     * @param cx      圆心x坐标
     * @param cy      圆心y坐标
     * @param redious 圆半径
     */
    private void drawBackground(Canvas canvas, int cx, int cy, int redious) {
        //如果没有给定背景颜色，就不绘制背景色
        if (0 != bgColor) {
            offset = dip2px(mContext, 5);
            paint.setColor(bgColor);
            canvas.drawCircle(cx, cy, redious, paint);
        } else {
            offset = 0;
        }
    }

    /**
     * 绘制圆形进度条
     *
     * @param canvas
     * @param diameter 圆环直径
     */
    private void drawProgress(Canvas canvas, int diameter) {
        // 绘制圆环背景
        paint.setColor(ringColor);
        paint.setStrokeWidth(progressStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        oval.left = offset + progressStrokeWidth / 2; // 左上角x
        oval.top = offset + progressStrokeWidth / 2; // 左上角y
        oval.right = diameter - progressStrokeWidth / 2 - offset; // 左下角x
        oval.bottom = diameter - progressStrokeWidth / 2 - offset; // 右下角y
        canvas.drawArc(oval, -90, 360, false, paint); // 绘制进度条背景

        if (isDrawArc) {
            // 绘制进度圆弧
            paint.setColor(progressColor);
            int currentValue;
            if (isCountDown) {
                currentValue = maxProgress - currentProgress;
            } else {
                currentValue = currentProgress;
            }
            float sweepAngle = ((float) currentValue / maxProgress) * 360;
            canvas.drawArc(oval, -90, sweepAngle, false, paint);
        }
    }

    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas, int redious) {
        int totalTextWidth;
        String numeratorText = String.valueOf(currentProgress);
        String denominatorText = "/" + String.valueOf(maxProgress);
        if (TextFormat.CURRENT_PER_SECOND_PROGRESS == textFormat) {
            denominatorText += "\"";
        }

        paint.setColor(textColor);
        paint.setStrokeWidth(1);
        paint.setTextSize(textSize);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL);

        int textWidth = (int) paint.measureText(numeratorText, 0, numeratorText.length());
        //文字下对齐
        int textY = redious + textSize / 3;

        if (TextFormat.CURRENT_WITH_TOTAL_PROGRESS == textFormat
                || TextFormat.CURRENT_PER_SECOND_PROGRESS == textFormat) {
            Paint paint2 = new Paint(paint);
            paint2.setTextSize(textSize2);
            int textWidth2 = (int) paint2.measureText(denominatorText, 0, denominatorText.length());
            totalTextWidth = textWidth + textWidth2;
            canvas.drawText(numeratorText, redious - totalTextWidth / 2, textY, paint);
            canvas.drawText(denominatorText, redious - totalTextWidth / 2 + textWidth, textY,
                    paint2);
        } else {
            canvas.drawText(numeratorText, redious - textWidth / 2, textY, paint);
        }
    }

    /**
     * 更新圆环进度条
     *
     * @param currentProgress 当前进度值
     */
    public void update(int currentProgress) {
        update(currentProgress, true);
    }

    /**
     * 更新圆环进度条
     *
     * @param currentProgress 当前进度值
     * @param isDrawArc       是否画当前进度
     */
    public void update(int currentProgress, boolean isDrawArc) {
        this.isDrawArc = isDrawArc;
        this.currentProgress = currentProgress;
        postInvalidate();
    }

    /**
     * 按秒更新圆环进度条
     */
    public void updatePerSecond(int currentProgress) {
        this.isDrawArc = true;
        textFormat = TextFormat.CURRENT_PER_SECOND_PROGRESS;
        if (0 == currentProgress) {
            this.currentProgress = 1;
        } else {
            this.currentProgress = currentProgress;
        }

        if(System.currentTimeMillis() - handlerSendMessageTime < 1000) {
            Log.i(TAG, "updatePerSecond: less 1000 millis");
            return;
        } else {
            handlerSendMessageTime = System.currentTimeMillis();
            if (null != handler) {
                handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1000);
            }
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        pauseTime = System.currentTimeMillis();
        handler.removeMessages(HANDLER_MESSAGE_ID);
    }

    /**
     * 继续
     */
    public void resume() {
        if (currentProgress < maxProgress) {
            long during = pauseTime - handlerSendMessageTime;
            if (during > 0 && during < 1000) {
                handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1000 - during);
            } else {
                handler.sendEmptyMessage(HANDLER_MESSAGE_ID);
            }
        }
    }

    public CircleProgressBar setBgColor(int bgColor) {
        this.bgColor = ContextCompat.getColor(mContext, bgColor);
        return this;
    }

    public CircleProgressBar setRingColor(int ringColor) {
        this.ringColor = ContextCompat.getColor(mContext, ringColor);
        return this;
    }

    public CircleProgressBar setProgressColor(int progressColor) {
        this.progressColor = ContextCompat.getColor(mContext, progressColor);
        return this;
    }

    public CircleProgressBar setTextColor(int textColor) {
        this.textColor = ContextCompat.getColor(mContext, textColor);
        return this;
    }

    public CircleProgressBar setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public CircleProgressBar setTextSize2(int textSize) {
        this.textSize2 = textSize;
        return this;
    }

    public CircleProgressBar setIsCountDown(boolean isCountDown) {
        this.isCountDown = isCountDown;
        return this;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public CircleProgressBar setTextFormat(TextFormat textFormat) {
        this.textFormat = textFormat;
        return this;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    /**
     * 重置
     */
    public void reset() {
        if (null != handler) {
            handler.removeMessages(HANDLER_MESSAGE_ID);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (null != handler) {
            handler.removeMessages(HANDLER_MESSAGE_ID);
        }
        handler = null;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    public enum TextFormat {
        CURRENT_PROGRESS,
        CURRENT_WITH_TOTAL_PROGRESS,
        CURRENT_PER_SECOND_PROGRESS
    }

    /**
     * 进度条更新监听
     */
    public interface OnProgressUpdateListener {
        /**
         * 处理更新监听事件
         *
         * @param progress 当前进度
         */
        void onProgressUpdate(int progress);
    }
}
