package com.cfox.asymedialib.core.engine;

import android.content.Context;
import android.provider.MediaStore;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;

public class MediaProviderEngine extends MediaEngine {


    public MediaProviderEngine(Context mContext, int mediaType) {
        super(mContext, mediaType);
    }

    public static MediaProviderEngine build(Context context, int mediaType) {
        return new MediaProviderEngine(context, mediaType);
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
