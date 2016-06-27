package com.android.lvxin.videoplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @ClassName: CircleProgressBar
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/22/16 14:10
 */
public class CircleProgressBar extends View {

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

    private TextFormat textFormat = TextFormat.TEXT_FORMAT_CURRENT_PROGRESS;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        oval = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为抗锯齿
    }

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

        // 绘制进度圆弧
        paint.setColor(progressColor);
        float sweepAngle = ((float) currentProgress / maxProgress) * 360;
        canvas.drawArc(oval, -90, sweepAngle, false, paint);
    }

    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas, int redious) {
        int totalTextWidth;
        String numeratorText = String.valueOf(currentProgress);
        String denominatorText = "/" + String.valueOf(maxProgress);

        paint.setColor(textColor);
        paint.setStrokeWidth(1);
        paint.setTextSize(textSize);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL);

        int textWidth = (int) paint.measureText(numeratorText, 0, numeratorText.length());
        //文字下对齐
        int textY = redious + textSize / 3;

        if (TextFormat.TEXT_FORMAT_CURRENT_WITH_TOTAL_PROGRESS == textFormat) {
            Paint paint2 = new Paint(paint);
            paint2.setTextSize(textSize2);
            int textWidth2 = (int) paint2.measureText(denominatorText, 0, denominatorText.length());
            totalTextWidth = textWidth + textWidth2;
            canvas.drawText(numeratorText, redious - totalTextWidth / 2, textY, paint);
            canvas.drawText(denominatorText, redious - totalTextWidth / 2 + textWidth, textY, paint2);
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
        this.currentProgress = currentProgress;
        postInvalidate();
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

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public CircleProgressBar setTextFormat(TextFormat textFormat) {
        this.textFormat = textFormat;
        return this;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    enum TextFormat {
        TEXT_FORMAT_CURRENT_PROGRESS, TEXT_FORMAT_CURRENT_WITH_TOTAL_PROGRESS
    }
}
