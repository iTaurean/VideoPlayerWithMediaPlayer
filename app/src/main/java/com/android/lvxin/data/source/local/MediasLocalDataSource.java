package com.android.lvxin.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.data.AudioInfo;
import com.android.lvxin.data.source.MediasDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MediasLocalDataSource
 * @Description: TODO
 * @Author: lvxin
 * @Date: 6/12/16 15:57
 */
public class MediasLocalDataSource implements MediasDataSource {

    private static MediasLocalDataSource INSTANCE;

    private Context mContext;

    private MediasLocalDataSource(Context context) {
        mContext = context;
    }

    public static MediasLocalDataSource getInstance(Context context) {
        if (null == INSTANCE) {
            INSTANCE = new MediasLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getVideos(LoadVideosCallback callback) {
        List<VideoInfo> videos = getVideosFromStorage();
        if (videos.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onVideosLoaded(videos);
        }
    }

    @Override
    public void getAudios(LoadAudiosCallback callback) {
        List<AudioInfo> audioInfos = getAudiosFromStorage();
        if (audioInfos.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onAudiosLoaded(audioInfos);
        }
    }

    private List<VideoInfo> getVideosFromStorage() {
        List<VideoInfo> videos = new ArrayList<>();

//        String[] videoColumns = {
//                MediaStore.Video.Media._ID,
//                MediaStore.Video.Media.DATA,
//                MediaStore.Video.Media.TITLE,
//                MediaStore.Video.Media.SIZE,
//                MediaStore.Video.Media.DURATION,
//        };
//
//        Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
//        int totalCount = cursor.getCount();
//        cursor.moveToFirst();
//        for (int i = 0; i < totalCount; i++) {
//            int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
//            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//            long videoDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
//            String videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
//            int videoSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
//            VideoInfo item = new VideoInfo(videoId, videoPath, videoTitle, videoSize, videoDuration);
//            videos.add(item);
//            cursor.moveToNext();
//        }
//        cursor.close();
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.videoName = "video_unit_1_1.mp4";
        videoInfo.videoDuration = 3;
        videos.add(videoInfo);
         videoInfo = new VideoInfo();
        videoInfo.videoName = "video_unit_2_1.mp4";
        videoInfo.videoDuration = 1;
        videos.add(videoInfo);

        return videos;
    }

    private List<AudioInfo> getAudiosFromStorage() {
        List<AudioInfo> videos = new ArrayList<>();
        String[] videoColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
        };

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
        int totalCount = cursor.getCount();
        cursor.moveToFirst();
        for (int i = 0; i < totalCount; i++) {
            int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long videoDuration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            int videoSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            AudioInfo item = new AudioInfo(videoId, videoPath, videoTitle, videoSize, videoDuration);
            videos.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return videos;
    }
}
