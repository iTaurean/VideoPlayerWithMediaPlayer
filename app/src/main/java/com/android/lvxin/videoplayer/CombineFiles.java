package com.android.lvxin.videoplayer;

import android.content.Context;
import android.util.Log;

import com.android.lvxin.data.AudioInfo;
import com.android.lvxin.data.VideoInfo;
import com.android.lvxin.util.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: CombineFiles
 * @Description: TODO
 * @Author: lvxin
 * @Date: 7/1/16 18:49
 */
public class CombineFiles {
    private static final String TAG = "CombineFiles";

    private Context context;

    public CombineFiles(Context context) {
        this.context = context;
    }

    public void start(List<VideoInfo> data) {
        for (VideoInfo video : data) {
            long begintime = System.currentTimeMillis();
//            Log.i(TAG, "handleMessage: begin time=" + begintime);
            combineAudios(video.getAudios(), video.getFileName());
            video.setAudio(new AudioInfo(video.getFileName()));
            long endtime = System.currentTimeMillis();
//            Log.i(TAG, "handleMessage: end time=" + endtime);
//            Log.i(TAG, "handleMessage: 消耗时间=" + (endtime - begintime));
        }
        Log.i(TAG, "handleMessage: finish");
    }

    private void combineAudios(List<AudioInfo> audioList, String fileName) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            long start = System.currentTimeMillis();
            Log.i(TAG, "combineAudios: start time=" + start);

            fileName = Tools.getAbsolutePath(context) + "/" + fileName + ".mp3";
//            File outputFile = new File(fileName)
//            fos = new FileOutputStream(outputFile);



            for (AudioInfo audio : audioList) {
                String sourcePath = Tools.getAbsolutePath(context) + "/audios/" + audio.getFileName() + ".mp3";
                File file = new File(sourcePath);
                fis = new FileInputStream(file);
                int i;
                while ((i = fis.read()) != -1) {
//                    fos.write(i);
//                    fos.flush();
                }

            }
            Log.i(TAG, "combineAudios: end time=" + (System.currentTimeMillis() - start));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (null != fis) {
                    fis.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
