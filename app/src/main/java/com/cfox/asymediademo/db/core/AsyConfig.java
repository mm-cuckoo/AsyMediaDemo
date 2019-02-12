package com.cfox.asymediademo.db.core;

public class AsyConfig {

    // media database tools  , media factory  , check update
    private static AsyConfig config = new AsyConfig();
    public MediaFactory mMediaFactory;
    public AbsMDatabaseControl mMDatabaseControl;
    public AbsUDatabaseControl mUDatabaseControl;
    public CheckRule checkRule;
    public CheckEngine checkEngine;

    public static AsyConfig getInstance() {
        return config;
    }


}
