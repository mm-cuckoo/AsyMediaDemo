package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.os.Bundle;

public class AsyMediaDatabase {
    public Context sContext;
    private DatabaseSyncHandler mDBSyncHandler;

    public AsyMediaDatabase(Build build) {
        this.sContext = build.context;
        AsyConfig config = AsyConfig.getInstance();
        config.mMediaFactory = build.mMediaFactory != null ? build.mMediaFactory : new MediaFactory() {
            @Override
            public MediaInfo createLocalMediaInfo() {
                return new MediaInfo();
            }
        };
        config.mMDatabaseControl = new MediaDatabaseControl();
        config.mUDatabaseControl =  build.mUDatabaseControl;
        config.checkRule = build.mCheckRule != null ? build.mCheckRule : new MediaCheckRule();

        mDBSyncHandler = new DatabaseSyncHandler(sContext);
        // 设置config , 启动成
    }

    public void asyMedia() {
        Bundle bundle = new Bundle();
        bundle.putInt(DatabaseSyncHandler.SERVICE_KEY, DatabaseSyncHandler.TYPE_START_APP);
        mDBSyncHandler.postMsg(bundle);
    }

    public void register() {
        MediaObserver.register(sContext, new MediaObserver(mDBSyncHandler));
    }

    public static class Build {
        private Context context;
        private MediaFactory mMediaFactory;
        private AbsUDatabaseControl mUDatabaseControl;
        private CheckRule mCheckRule;

        public Build(Context context) {
            this.context = context.getApplicationContext();
        }

        public Build setMediaFactory(MediaFactory mediaFactory) {
            this.mMediaFactory = mediaFactory;
            return this;
        }

        public Build setUDatabaseControl(AbsUDatabaseControl UDatabaseControl) {
            this.mUDatabaseControl = UDatabaseControl;
            return this;
        }

        public Build setCheckRule(CheckRule mCheckRule) {
            this.mCheckRule = mCheckRule;
            return this;
        }

        public AsyMediaDatabase build() {
            return new AsyMediaDatabase(this);
        }
    }

    public static Build create(Context context) {
        return new Build(context);
    }
}
