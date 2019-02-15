package com.cfox.asymedialib.core.engine;

import android.content.Context;
import android.provider.MediaStore;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;

public class MediaProviderEngine extends MediaEngine {


    private MediaProviderEngine(Context mContext, int mediaType) {
        super(mContext, mediaType);
    }

    public static MediaProviderEngine build(Context context, int mediaType) {
        return new MediaProviderEngine(context, mediaType);
    }


    @Override
    public CursorWrapper queryImage(Context context, int startMediaId, int rowNum) {
        return AsyConfig.getInstance().mMDatabaseControl.queryImage(
                context,
                AsyConfig.getInstance().mFilterMinImageSize,
                AsyConfig.getInstance().mFilterMaxImageSize,
                startMediaId, rowNum);
    }

    @Override
    public CursorWrapper queryVideo(Context context, int startMediaId, int rowNum) {
        return AsyConfig.getInstance().mMDatabaseControl.queryVideo(
                context,
                AsyConfig.getInstance().mFilterMinVideoSize,
                AsyConfig.getInstance().mFilterMaxVideoSize,
                startMediaId, rowNum);
    }

    @Override
    public CursorWrapper queryImageAndVideo(Context context, int startMediaId, int rowNum) {
        return AsyConfig.getInstance().mMDatabaseControl.queryImageAndVideo(
                context,
                AsyConfig.getInstance().mFilterMinImageSize,
                AsyConfig.getInstance().mFilterMaxImageSize,
                AsyConfig.getInstance().mFilterMinVideoSize,
                AsyConfig.getInstance().mFilterMaxVideoSize,
                startMediaId, rowNum);
    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return cursorWrapper.cursor.getInt(cursorWrapper.cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
    }
}
