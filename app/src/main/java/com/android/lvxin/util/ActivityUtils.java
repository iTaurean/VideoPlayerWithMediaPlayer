package com.android.lvxin.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @ClassName: ActivityUtils
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 17:08
 */
public class ActivityUtils {
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int fragmentId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragmentId, fragment);
        transaction.commit();
    }
}
