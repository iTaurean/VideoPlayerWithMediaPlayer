package com.android.lvxin.widget;

/**
 * @author BMR
 * @ClassName: CalendarHelper
 * @Description: 日期计算帮助类
 * @date 2016/2/26 16:08
 */
public class CalendarHelper {

    /**
     * a integer to xx:xx
     *
     * @param time
     * @return
     */
    public static String secToMinSec(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            second = time % 60;
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
            return timeStr;
        }
    }

    /**
     * 格式化int值
     *
     * @param i
     * @return
     */
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

}
