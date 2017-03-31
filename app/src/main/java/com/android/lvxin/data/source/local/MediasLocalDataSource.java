package com.android.lvxin.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.android.lvxin.data.AudioInfo;
import com.android.lvxin.data.VideoInfo;
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
//        Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
// videoColumns, null, null, null);
//        int totalCount = cursor.getCount();
//        cursor.moveToFirst();
//        for (int i = 0; i < totalCount; i++) {
//            int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
//            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
//            String videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
//            int videoSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
//            VideoInfo item = new VideoInfo(videoId, videoPath, videoTitle, videoSize, duration);
//            videos.add(item);
//            cursor.moveToNext();
//        }
//        cursor.close();
        List<AudioInfo> audioInfoList = new ArrayList<>();
        AudioInfo audioInfo = new AudioInfo("misc_audio_1_1");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("audio_1_56");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_39");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_7");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_6");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_5");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_4");
        audioInfoList.add(audioInfo);

        VideoInfo videoInfo = new VideoInfo("video_intro_1_1", 9, "蛙泳划臂", 1, 1, 0, audioInfoList);
        videos.add(videoInfo);

        audioInfoList = new ArrayList<>();
        audioInfo = new AudioInfo("misc_audio_1_5");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_6");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_7");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_8");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_9");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_10");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_65");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_11");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_12");
        audioInfoList.add(audioInfo);

        videoInfo = new VideoInfo("video_unit_1_1", 3, "蛙泳划臂", 8, 1, 0, audioInfoList);
        videos.add(videoInfo);

        audioInfoList = new ArrayList<>();
        audioInfo = new AudioInfo("misc_audio_1_2");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("audio_1_53");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_44");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_7");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_6");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_5");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_4");
        audioInfoList.add(audioInfo);

        videoInfo = new VideoInfo("video_intro_2_1", 1, "蛙泳划臂", 8, 1, 0, audioInfoList);
        videos.add(videoInfo);

        audioInfoList = new ArrayList<>();
        audioInfo = new AudioInfo("misc_audio_1_5");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_6");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_7");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_8");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_9");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_10");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_11");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_12");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_13");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_14");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_15");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_16");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_17");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_18");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_19");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_20");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_21");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_22");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_23");
        audioInfoList.add(audioInfo);
        audioInfo = new AudioInfo("misc_audio_1_24");
        audioInfoList.add(audioInfo);

        videoInfo = new VideoInfo("video_unit_2_1", 1, "蛙泳划臂", 20, 1, 20, audioInfoList);
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

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, videoColumns,
                null, null, null);
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
