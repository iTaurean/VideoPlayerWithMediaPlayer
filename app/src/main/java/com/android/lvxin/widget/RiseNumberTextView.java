package com.android.lvxin.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @ClassName: RiseNumberTextView
 * @Description: TODO
 * @Author: lvxin
 * @Date: 09/10/2016 15:43
 */
public class RiseNumberTextView extends TextView implements RiseNumberBase {

    final static int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE};
    private static final int STOPPED = 0;
    private static final int RUNNING = 1;
    private int mPlayingState = STOPPED;
    private float number;
    private float fromNumber;
    private long duration = 1000;
    /**
     * 1.int 2.float
     */
    private int numberType = 1;
    private boolean flags = true;
    private EndListener mEndListener = null;

    public RiseNumberTextView(Context context) {
        super(context);
    }

    public RiseNumberTextView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public RiseNumberTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    static int sizeOfInt(int x) {
        for (int i = 0; ; i++) {
            if (x <= SIZE_TABLE[i]) {
                return i + 1;
            }
        }
    }

    public boolean isRunning() {
        return (mPlayingState == RUNNING);
    }

    private void runFloat() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromNumber, number);
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (flags) {
                    setText(format(",##0.00").format(Double.parseDouble(valueAnimator.getAnimatedValue().toString())) + "");
                    if (valueAnimator.getAnimatedValue().toString().equalsIgnoreCase(number + "")) {
                        setText(format(",##0.00").format(Double.parseDouble(number + "")));
                    }
                } else {
                    setText(format("##0.00").format(Double.parseDouble(valueAnimator.getAnimatedValue().toString())) + "");
                    if (valueAnimator.getAnimatedValue().toString().equalsIgnoreCase(number + "")) {
                        setText(format("##0.00").format(Double.parseDouble(number + "")));
                    }
                }
                if (valueAnimator.getAnimatedFraction() >= 1) {
                    mPlayingState = STOPPED;
                    if (mEndListener != null) {
                        mEndListener.onEndFinish();
                    }
                }
            }
        });
        valueAnimator.start();
    }

    private void runInt() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber, (int) number);
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setText(valueAnimator.getAnimatedValue().toString());
                if (valueAnimator.getAnimatedFraction() >= 1) {
                    mPlayingState = STOPPED;
                    if (mEndListener != null) {
                        mEndListener.onEndFinish();
                    }
                }
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void start() {

        if (!isRunning()) {
            mPlayingState = RUNNING;
            if (numberType == 1) {
                runInt();
            } else {
                runFloat();
            }
        }
    }

    @Override
    public RiseNumberTextView withNumber(float number, boolean flag) {

        this.number = number;
        this.flags = flag;
        numberType = 2;
        fromNumber = 0;

        return this;
    }

    @Override
    public RiseNumberTextView withNumber(float number) {
        System.out.println(number);
        this.number = number;
        numberType = 2;
        fromNumber = 0;

        return this;
    }

    @Override
    public RiseNumberTextView withNumber(int number) {
        this.number = number;
        numberType = 1;
        fromNumber = 0;

        return this;
    }

    @Override
    public RiseNumberTextView setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public void setOnEnd(EndListener callback) {
        mEndListener = callback;
    }

    private DecimalFormat format(String pattern) {
        DecimalFormat dfs = null;
        if (dfs == null) {
            dfs = new DecimalFormat();
        }
        dfs.setRoundingMode(RoundingMode.FLOOR);
        dfs.applyPattern(pattern);
        return dfs;
    }

    /**
     *
     */
    public interface EndListener {
        /**
         *
         */
        void onEndFinish();
    }
}
