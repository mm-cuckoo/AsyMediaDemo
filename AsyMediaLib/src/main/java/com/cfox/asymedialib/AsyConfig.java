package com.cfox.asymedialib;

import android.content.Context;

import com.cfox.asymedialib.core.db.AbsMDatabaseControl;
import com.cfox.asymedialib.core.db.AbsUDatabaseControl;
import com.cfox.asymedialib.core.CheckRule;

public class AsyConfig {

    public static boolean isDebug = false;
    private static AsyConfig config = new AsyConfig();
    public Context mContext;
    public AbsMDatabaseControl mMDatabaseControl;
    public AbsUDatabaseControl mUDatabaseControl;
    public CheckRule checkRule;
    public int mQueryOnceRowNumber;
    public int mFilterMinImageSize;
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
                ", mCacheSizeInsert=" + mCacheSizeInsert +
                ", mCacheSizeUpdate=" + mCacheSizeUpdate +
                ", mCacheSizeDelete=" + mCacheSizeDelete +
                '}';
    }
}
