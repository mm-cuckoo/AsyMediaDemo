package com.cfox.asymediademo;

import android.app.Application;
import android.util.Log;

import com.cfox.asymediademo.db.LocalDatabaseController;
import com.cfox.asymediademo.db.LocalMediaInfo;
import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.AsyMediaDatabase;
import com.cfox.asymedialib.core.CheckRule;
import com.cfox.asymedialib.core.MediaInfo;


public class AppApplication extends Application {
    private static final String TAG = "AppApplication";

    private AsyMediaDatabase amd;

    @Override
    public void onCreate() {
        super.onCreate();
        amd = AsyMediaDatabase
                .create(this)
                .setUDatabaseControl(new LocalDatabaseController())
                .setCheckRule(new CheckRule<LocalMediaInfo>() {
                    @Override
                    public boolean checkUpdate(LocalMediaInfo localBean, MediaInfo mediaInfo) {
                        Log.e(TAG, "checkUpdate: " +
                                "  local id:" + localBean.getMediaId() +
                                "  media id:" + mediaInfo.getMediaId() +
                                "  update: " + !localBean.getPath().equals(mediaInfo.getPath()));
                        return !localBean.getPath().equals(mediaInfo.getPath());
                    }
                })
                .setQueryOnceRowNumber(10)
                .setCacheSizeDelete(20)
                .setCacheSizeUpdate(30)
                .setCacheSizeInsert(40)
                .setFilterMinImageSize(39999)
                .setFilterMaxImageSize(500000)
                .setFilterMinVideoSize(1024)
                .setFilterMaxVideoSize(1024 * 100000)
                .build();

        amd.register();
    }

    public void asy() {
        amd.asyMedia();
    }
}
