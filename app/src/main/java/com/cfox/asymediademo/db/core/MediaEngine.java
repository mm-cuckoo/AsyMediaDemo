package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MediaEngine extends Engine {


    public MediaEngine(Context mContext, int mediaType) {
        super(mContext, mediaType);
    }

    public static MediaEngine build(Context context, int mediaType) {
        return new MediaEngine(context, mediaType);
    }


    @Override
    public CursorWrapper queryImageToCursor(Context context, int startId, int rowNum) {
        return AsyConfig.getInstance().mMDatabaseControl.queryImageToCursor(context, startId, rowNum);
    }

    @Override
    public CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum) {
        return AsyConfig.getInstance().mMDatabaseControl.queryVideoToCursor(context, startId, rowNum);
    }

    @Override
    public CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum) {
        return AsyConfig.getInstance().mMDatabaseControl.queryImageAndVideoToCursor(context, startId, rowNum);
    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return cursorWrapper.cursor.getInt(cursorWrapper.cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
    }
}
