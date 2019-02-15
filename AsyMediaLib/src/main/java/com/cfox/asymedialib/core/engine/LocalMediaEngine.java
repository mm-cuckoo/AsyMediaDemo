package com.cfox.asymedialib.core.engine;

import android.content.Context;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;

public class LocalMediaEngine extends MediaEngine {


    private LocalMediaEngine(Context mContext, int mediaType) {
        super(mContext, mediaType);
    }

    public static LocalMediaEngine build(Context context, int mediaType) {
        return new LocalMediaEngine(context, mediaType);
    }


    @Override
    public CursorWrapper queryImage(Context context, int startMediaId, int rowNum) {
        return AsyConfig.getInstance().mUDatabaseControl.queryImage(
                context,
                AsyConfig.getInstance().mFilterMinImageSize,
                AsyConfig.getInstance().mFilterMaxImageSize,
                startMediaId, rowNum);
    }

    @Override
    public CursorWrapper queryVideo(Context context, int startMediaId, int rowNum) {
        return AsyConfig.getInstance().mUDatabaseControl.queryVideo(
                context,
                AsyConfig.getInstance().mFilterMinVideoSize,
                AsyConfig.getInstance().mFilterMaxVideoSize,
                startMediaId, rowNum);
    }

    @Override
    public CursorWrapper queryImageAndVideo(Context context, int startMediaId, int rowNum) {
        return AsyConfig.getInstance().mUDatabaseControl.queryImageAndVideo(
                context,
                AsyConfig.getInstance().mFilterMinImageSize,
                AsyConfig.getInstance().mFilterMaxImageSize,
                AsyConfig.getInstance().mFilterMinVideoSize,
                AsyConfig.getInstance().mFilterMaxVideoSize,
                startMediaId, rowNum);
    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return AsyConfig.getInstance().mUDatabaseControl.getMediaId(cursorWrapper);
    }
}
