package com.android.lvxin.util;

/**
 * @ClassName: StringUtils
 * @Description: TODO
 * @Author: lvxin
 * @Date: 8/12/16 15:44
 */
public class StringUtils {
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str.trim()) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 获取小图地址
     *
     * @param url
     * @return 原图(_R)、缩略图(_M)、大图(_L)
     */
    public static String getSmallImageUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.replace("_X", "_M");
    }

    /**
     * 获取大图地址
     *
     * @param url
     * @return
     */
    public static String getLargeImageUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.replace("_X", "_L");
    }

    /**
     * 获取原图地址
     *
     * @param url
     * @return
     */
    public static String getOriginalImageUrl(String url) {
        if (url == null) {
            return "";
        }
        return url.replace("_X", "_R");
    }

    /**
     * 数字转换成字符创，小于10前面添加0
     *
     * @param value
     * @return
     */
    public static String intToString(int value) {
        return 10 > value ? ("0" + value) : String.valueOf(value);
    }

}
