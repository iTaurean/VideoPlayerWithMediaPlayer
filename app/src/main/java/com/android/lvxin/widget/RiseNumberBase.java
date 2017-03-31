package com.android.lvxin.widget;

/**
 * @ClassName: RiseNumberBase
 * @Description: TODO
 * @Author: lvxin
 * @Date: 09/10/2016 15:42
 */
public interface RiseNumberBase {
    /**
     *
     */
    void start();

    /**
     * @param number
     * @return
     */
    RiseNumberTextView withNumber(float number);

    /**
     * @param number
     * @param flag
     * @return
     */
    RiseNumberTextView withNumber(float number, boolean flag);

    /**
     * @param number
     * @return
     */
    RiseNumberTextView withNumber(int number);

    /**
     * @param duration
     * @return
     */
    RiseNumberTextView setDuration(long duration);

    /**
     * @param callback
     */
    void setOnEnd(RiseNumberTextView.EndListener callback);
}
