package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.database.Cursor;

public class LocalMediaEngine extends Engine {


    public LocalMediaEngine(Context mContext, int mediaType) {
        super(mContext, mediaType);
    }

    public static LocalMediaEngine build(Context context, int mediaType) {
        return new LocalMediaEngine(context, mediaType);
    }


    @Override
    public CursorWrapper queryImageToCursor(Context context, int startId, int rowNum) {
        return AsyConfig.getInstance().mUDatabaseControl.queryImageToCursor(context, startId, rowNum);
    }

    @Override
    public CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum) {
        return AsyConfig.getInstance().mUDatabaseControl.queryVideoToCursor(context, startId, rowNum);
    }

    @Override
    public CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum) {
        return AsyConfig.getInstance().mUDatabaseControl.queryImageAndVideoToCursor(context, startId, rowNum);
    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return AsyConfig.getInstance().mUDatabaseControl.getMediaId(cursorWrapper);
    }
}
