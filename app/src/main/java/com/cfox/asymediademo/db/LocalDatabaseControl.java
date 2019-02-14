package com.cfox.asymediademo.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cfox.asymedialib.core.MediaInfo;
import com.cfox.asymedialib.core.db.AbsUDatabaseControl;
import com.cfox.asymedialib.core.CursorWrapper;

import java.util.List;


public class LocalDatabaseControl extends AbsUDatabaseControl<LocalMediaInfo> {
    private static final String TAG = "LocalDatabaseControl";
    @Override
    public LocalMediaInfo cursorToMediaBean(Cursor cursor, LocalMediaInfo localMediaInfo, int minSize) {
        return null;
    }

    @Override
    public CursorWrapper baseQuery(Context context, String where, String[] whereArgs, String sortOrder) {
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
    public void insertForList(Context context, List<MediaInfo> medias) {
        for (MediaInfo info: medias) {
            Log.e(TAG, "insertForList: info:" + info);
        }

    }

    @Override
    public void updateForList(Context context, List<MediaInfo> medias) {

    }

    @Override
    public void deleteForList(Context context, List<MediaInfo> medias) {

    }

    @Override
    public void insertOrUpdate(Context context, MediaInfo mediaInfo) {
        Log.e(TAG, "insertOrUpdate: " + mediaInfo);

    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return cursorWrapper.cursor.getInt(cursorWrapper.cursor.getColumnIndexOrThrow(IMediaDbData.MEDIA_ID));
    }
}
