package com.cfox.asymediademo;

import android.app.Application;
import android.util.Log;

import com.cfox.asymediademo.db.LocalDatabaseControl;
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
        AsyConfig.isDebug = true;
        amd = AsyMediaDatabase
                .create(this)
                .setUDatabaseControl(new LocalDatabaseControl())
                .setCheckRule(new CheckRule<LocalMediaInfo>() {
                    @Override
                    public boolean checkUpdate(LocalMediaInfo localBean, MediaInfo mediaInfo) {
                        Log.e(TAG, "checkUpdate: " +
                                "  local id:" + localBean.getMediaId() +
                                "  media id:" + mediaInfo.getMediaId()  +
                                "  update: " + !localBean.getPath().equals(mediaInfo.getPath()));
                        return !localBean.getPath().equals(mediaInfo.getPath());
                    }
                })
                .setQueryOnceRowNumber(10)
                .setFilterMinImageSize(3000)
                .setCacheSizeDelete(20)
                .setCacheSizeUpdate(30)
                .setCacheSizeInsert(40)
                .build();

        amd.register();
    }

    public void asy() {
        amd.asyMedia();
    }
}
