package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MediaDatabaseControl extends AbsMDatabaseControl<MediaInfo> {
    @Override
    public MediaInfo cursorToMediaBean(Cursor cursor, MediaInfo mediaInfo, int minSize) {
        return null;
    }

    @Override
    public CursorWrapper baseQuery(Context context, String where, String[] whereArgs, String sortOrder) {
        return null;
    }

    @Override
    public CursorWrapper queryToCursorWrapper(Context context, int startId, int rowNum) {
        return null;
    }

    @Override
    public CursorWrapper queryImageToCursor(Context context, int startId, int rowNum) {
        return null;
    }

    @Override
    public CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum) {
        return null;
    }

    @Override
    public CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum) {
        return null;
    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return cursorWrapper.cursor.getInt(cursorWrapper.cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
    }
}
