package com.android.lvxin.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;

/**
 * @ClassName: ViewUtils
 * @Description: activity与fragment关联的工具类
 * @Author: lvxin
 * @Date: 6/12/16 17:08
 */
public class ViewUtils {

    private static long lastClickTime;

    /**
     * 添加Fragment到Activity
     *
     * @param fragmentManager
     * @param fragment
     * @param fragmentId
     */
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int fragmentId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragmentId, fragment);
        transaction.commit();
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * 将dip/dp值转换为px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue（DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 判断连续点击方法
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


}
