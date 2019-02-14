package com.cfox.asymedialib;

import android.content.Context;

import com.cfox.asymedialib.core.db.AbsMDatabaseControl;
import com.cfox.asymedialib.core.db.AbsUDatabaseControl;
import com.cfox.asymedialib.core.CheckRule;
import com.cfox.asymedialib.core.MediaInfoFactory;

public class AsyConfig {

    public static boolean isDebug = false;

    private static AsyConfig config = new AsyConfig();
    public Context mContext;
    public MediaInfoFactory mMediaInfoFactory;
    public MediaInfoFactory mLocalMediaInfoFactory;
    public AbsMDatabaseControl mMDatabaseControl;
    public AbsUDatabaseControl mUDatabaseControl;
    public CheckRule checkRule;
    public int mQueryOnceRowNumber = 1000;
    public int mFilterMinImageSize;

    public static AsyConfig getInstance() {
        return config;
    }
}
