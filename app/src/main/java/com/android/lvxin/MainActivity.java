package com.android.lvxin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.lvxin.model.GetPlanDetailAck;
import com.android.lvxin.model.MediaPlayerArgs;
import com.android.lvxin.util.FileUtils;
import com.android.lvxin.videoplayer.MediaPlayerActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_enter_media_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonFileName = "plan_2.json";
                String jsonString = FileUtils.getFromAssets(MainActivity.this, jsonFileName);
                Gson gson = new GsonBuilder().create();
                GetPlanDetailAck data0 = gson.fromJson(jsonString, GetPlanDetailAck.class);
                GetPlanDetailAck.PlanDetailInfo data = data0.data;

                MediaPlayerArgs arg = new MediaPlayerArgs();
                arg.setSchemeId(data.getId());
                arg.setTotalDuration(data.getMoveTime());
                arg.setVideos(data.getPlanVideoList());
                arg.setBgAudioFileName(data.getBgMusicUrl());
                arg.setCoast(data.getCoast());
                MediaPlayerActivity.start(MainActivity.this, arg);

            }
        });
    }
}
