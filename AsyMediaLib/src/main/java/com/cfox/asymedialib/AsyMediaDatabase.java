package com.cfox.asymedialib;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cfox.asymedialib.core.db.AbsUDatabaseController;
import com.cfox.asymedialib.core.CheckRule;
import com.cfox.asymedialib.core.DatabaseSyncHandler;
import com.cfox.asymedialib.core.MediaCheckRule;
import com.cfox.asymedialib.core.MediaObserver;
import com.cfox.asymedialib.core.db.MediaDatabaseController;

public class AsyMediaDatabase {
    private static final String TAG = "AsyMediaDatabase";
    private Context mContext;
    private DatabaseSyncHandler mDBSyncHandler;

    AsyMediaDatabase(Build build) {
        this.mContext = build.context;

        AsyConfig.Debug = build.mDebug;

        AsyConfig config = AsyConfig.getInstance();
        config.mContext = build.context;
        config.mMDatabaseControl = new MediaDatabaseController();
        config.mUDatabaseControl =  build.mUDatabaseControl;
        config.checkRule = build.mCheckRule != null ? build.mCheckRule : new MediaCheckRule();
        config.mFilterMinImageSize = build.mFilterMinImageSize;
        config.mFilterMaxImageSize = build.mFilterMaxImageSize;
        config.mFilterMinVideoSize = build.mFilterMinVideoSize;
        config.mFilterMaxVideoSize = build.mFilterMaxVideoSize;
        config.mQueryOnceRowNumber = build.mQueryOnceRowNumber;
        config.mCacheSizeInsert = build.mCacheSizeInsert;
        config.mCacheSizeUpdate = build.mCacheSizeUpdate;
        config.mCacheSizeDelete = build.mCacheSizeDelete;

        if(AsyConfig.Debug) {
            Log.d(TAG, "AsyMedia db lib config :" + config.toString());
        }

        mDBSyncHandler = new DatabaseSyncHandler(mContext);
        // 设置config , 启动成
    }

    public void asyMedia() {
        Bundle bundle = new Bundle();
        bundle.putInt(DatabaseSyncHandler.SERVICE_KEY, DatabaseSyncHandler.TYPE_START_APP);
        mDBSyncHandler.postMsg(bundle);
    }

    public void register() {
        MediaObserver.register(mContext, new MediaObserver(mDBSyncHandler));
    }

    public static class Build {
        private Context context;
        private AbsUDatabaseController mUDatabaseControl;
        private CheckRule mCheckRule;
        private boolean mDebug = false;
        private int mQueryOnceRowNumber = 1000;
        private int mFilterMinImageSize = 0;
        private int mFilterMaxImageSize = 0;
        private int mFilterMinVideoSize = 0;
        private int mFilterMaxVideoSize = 0;
        private int mCacheSizeInsert = 100;
        private int mCacheSizeUpdate = 100;
        private int mCacheSizeDelete = 100;

        public Build(Context context) {
            this.context = context.getApplicationContext();
        }

        public Build setUDatabaseControl(AbsUDatabaseController UDatabaseControl) {
            this.mUDatabaseControl = UDatabaseControl;
            return this;
        }

        public Build setCheckRule(CheckRule mCheckRule) {
            this.mCheckRule = mCheckRule;
            return this;
        }

        public Build setQueryOnceRowNumber(int mQueryOnceRowNumber) {
            this.mQueryOnceRowNumber = mQueryOnceRowNumber;
            return this;
        }

        public Build setFilterMinImageSize(int mFilterMinImageSize) {
            this.mFilterMinImageSize = mFilterMinImageSize;
            return this;
        }

        public Build setCacheSizeInsert(int mCacheSizeInsert) {
            this.mCacheSizeInsert = mCacheSizeInsert;
            return this;
        }

        public Build setCacheSizeUpdate(int mCacheSizeUpdate) {
            this.mCacheSizeUpdate = mCacheSizeUpdate;
            return this;
        }

        public Build setCacheSizeDelete(int mCacheSizeDelete) {
            this.mCacheSizeDelete = mCacheSizeDelete;
            return this;
        }

        public Build setFilterMaxImageSize(int mFilterMaxImageSize) {
            this.mFilterMaxImageSize = mFilterMaxImageSize;
            return this;
        }

        public Build setFilterMinVideoSize(int mFilterMinVideoSize) {
            this.mFilterMinVideoSize = mFilterMinVideoSize;
            return this;
        }

        public Build setFilterMaxVideoSize(int mFilterMaxVideoSize) {
            this.mFilterMaxVideoSize = mFilterMaxVideoSize;
            return this;
        }

        public Build openDebug(boolean mDebug) {
            this.mDebug = mDebug;
            return this;
        }

        public Build openDebug() {
            this.mDebug = true;
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
