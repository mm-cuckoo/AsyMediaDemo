package com.cfox.asymedialib.core.engine;

import android.content.Context;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;

public class LocalMediaEngine extends MediaEngine {


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
