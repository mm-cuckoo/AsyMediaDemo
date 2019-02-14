package com.cfox.asymediademo;

import android.app.Application;

import com.cfox.asymediademo.db.LocalDatabaseControl;
import com.cfox.asymediademo.db.LocalMediaInfo;
import com.cfox.asymedialib.AsyMediaDatabase;
import com.cfox.asymedialib.core.CheckRule;
import com.cfox.asymedialib.core.MediaInfoFactory;
import com.cfox.asymedialib.core.MediaInfo;


public class AppApplication extends Application {

    private AsyMediaDatabase amd;

    @Override
    public void onCreate() {
        super.onCreate();
//        AsyConfig.isDebug = true;
        amd = AsyMediaDatabase
                .create(this)
                .setMediaInfoFactory(new MediaInfoFactory<LocalMediaInfo>() {
                    @Override
                    public LocalMediaInfo createMediaInfo() {
                        return new LocalMediaInfo();
                    }
                })
                .setUDatabaseControl(new LocalDatabaseControl())
                .setCheckRule(new CheckRule<LocalMediaInfo>() {
                    @Override
                    public boolean checkUpdate(LocalMediaInfo localBean, MediaInfo mediaInfo) {
                        return false;
                    }
                })
                .setQueryOnceRowNumber(10)
                .setFilterMinImageSize(0)
                .build();

        amd.register();

    }

    public void asy() {
        amd.asyMedia();
    }
}
