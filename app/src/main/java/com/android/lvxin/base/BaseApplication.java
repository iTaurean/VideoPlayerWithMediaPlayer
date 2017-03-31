package com.android.lvxin.base;

import android.app.Application;
import android.content.ComponentName;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * 全局Application
 * Created by xuyun on 2016/11/2.
 */
public class BaseApplication extends Application {

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static BaseApplication sInstance;
//    private static String appDir;
//    private static String videoDir;
    private static int mScreenWidth = -1;
    private static int mScreenHeight = -1;
    private final Handler appHandler = new Handler();
    private Toast toast;

    /**
     * Gets instance.
     *
     * @return ApplicationController singleton instance
     */
    public static synchronized BaseApplication getInstance() {
        return sInstance;
    }

    /**
     * Gets app dir.
     *
     * @return the app dir
     */
//    public static String getAppDir() {
//        return appDir;
//    }

    /**
     * Gets video dir.
     *
     * @return the video dir
     */
//    public static String getVideoDir() {
//        return videoDir;
//    }

    /**
     * Gets screen width.
     *
     * @return the screen width
     */
    public static int getmScreenWidth() {
        return mScreenWidth;
    }

    /**
     * Gets screen height.
     *
     * @return the screen height
     */
    public static int getmScreenHeight() {
        return mScreenHeight;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        initCommon();
    }


    private void initCommon() {
//        appDir = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/";
//        videoDir = appDir + "video/";


        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = Math.min(dm.widthPixels, dm.heightPixels);
        mScreenHeight = Math.max(dm.widthPixels, dm.heightPixels);

    }

    /**
     * Gets app handler.
     *
     * @return the app handler
     */
    public Handler getAppHandler() {
        return appHandler;
    }

    /**
     * Showtoast.
     *
     * @param content the content
     */
    public void showtoast(String content) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            final String text = content;
            getAppHandler().post(new Runnable() {
                @Override
                public void run() {
                    showToast(text);
                }
            });
        } else {
            showToast(content);
        }
    }

    /**
     * Showtoast.
     *
     * @param contentId the content id
     */
    public void showtoast(int contentId) {
        showtoast(getString(contentId));
    }

    private void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }


    /**
     * The constant ACTION_NOTIFICATION_LISTENER_SETTINGS.
     */
    public static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
