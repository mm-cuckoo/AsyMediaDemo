package com.cfox.asymedialib;

import android.content.Context;

import com.cfox.asymedialib.core.db.AbsMDatabaseController;
import com.cfox.asymedialib.core.db.AbsUDatabaseController;
import com.cfox.asymedialib.core.CheckRule;

public class AsyConfig {

    public final static boolean DEBUG_INFO = false;
    public static boolean Debug = false;

    private static AsyConfig config = new AsyConfig();
    public Context mContext;
    public AbsMDatabaseController mMDatabaseControl;
    public AbsUDatabaseController mUDatabaseControl;
    public CheckRule checkRule;
    public int mQueryOnceRowNumber;
    public int mFilterMinImageSize;
    public int mFilterMaxImageSize;
    public int mFilterMinVideoSize;
    public int mFilterMaxVideoSize;
    public int mCacheSizeInsert;
    public int mCacheSizeUpdate;
    public int mCacheSizeDelete;

    public static AsyConfig getInstance() {
        return config;
    }

    @Override
    public String toString() {
        return "AsyConfig{" +
                "mQueryOnceRowNumber=" + mQueryOnceRowNumber +
                ", mFilterMinImageSize=" + mFilterMinImageSize +
                ", mFilterMaxImageSize=" + mFilterMaxImageSize +
                ", mFilterMinVideoSize=" + mFilterMinVideoSize +
                ", mFilterMaxVideoSize=" + mFilterMaxVideoSize +
                ", mCacheSizeInsert=" + mCacheSizeInsert +
                ", mCacheSizeUpdate=" + mCacheSizeUpdate +
                ", mCacheSizeDelete=" + mCacheSizeDelete +
                '}';
    }
}
