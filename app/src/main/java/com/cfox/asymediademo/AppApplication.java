package com.cfox.asymediademo;

import android.app.Application;

import com.cfox.asymediademo.db.LocalDatabaseControl;
import com.cfox.asymediademo.db.LocalMediaInfo;
import com.cfox.asymediademo.db.core.AsyMediaDatabase;
import com.cfox.asymediademo.db.core.CheckRule;
import com.cfox.asymediademo.db.core.MediaFactory;
import com.cfox.asymediademo.db.core.MediaInfo;


public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AsyMediaDatabase amd = AsyMediaDatabase
                .create(this)
                .setMediaFactory(new MediaFactory<LocalMediaInfo>() {
                    @Override
                    public LocalMediaInfo createLocalMediaInfo() {
                        return new LocalMediaInfo();
                    }
                })
                .setUDatabaseControl(new LocalDatabaseControl())
                .setCheckRule(new CheckRule<LocalMediaInfo>() {
                    @Override
                    public boolean checkUpdate(LocalMediaInfo localBean, MediaInfo mediaInfo) {
                        return false;
                    }
                }).build();

        amd.asyMedia();
        amd.register();

    }
}
