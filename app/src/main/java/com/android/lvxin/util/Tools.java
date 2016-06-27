package com.android.lvxin.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.provider.MediaStore;
import android.view.Display;

import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.data.AudioInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Tools
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/14/16 14:21
 */
public class Tools {
    public static int getWindowWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static List<AudioInfo> generateAudioItems(Context context) {
        List<AudioInfo> items = new ArrayList<>();
        String[] videoColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
        int totalCount = cursor.getCount();
        // no video, then show the no video hint
        cursor.moveToFirst();
        for (int i = 0; i < totalCount; i++) {
            int audioId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String audioPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long audioDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String audioTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            int audioSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            AudioInfo item = new AudioInfo(audioId, audioPath, audioTitle, audioSize, audioDuration);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public static List<VideoInfo> generateVideoItems(Context context) {
        List<VideoInfo> items = new ArrayList<>();
        String[] videoColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
        int totalCount = cursor.getCount();
        // no video, then show the no video hint
        cursor.moveToFirst();
        for (int i = 0; i < totalCount; i++) {
            int audioId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String audioPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long audioDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String audioTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            int audioSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            VideoInfo item = new VideoInfo(audioId, audioPath, audioTitle, audioSize, audioDuration);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }
}
