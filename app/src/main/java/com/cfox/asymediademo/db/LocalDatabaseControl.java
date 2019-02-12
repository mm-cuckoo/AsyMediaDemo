package com.cfox.asymediademo.db;

import android.content.Context;
import android.database.Cursor;

import com.cfox.asymediademo.db.core.AbsUDatabaseControl;
import com.cfox.asymediademo.db.core.CursorWrapper;

import java.util.List;


public class LocalDatabaseControl extends AbsUDatabaseControl<LocalMediaInfo> {
    @Override
    public LocalMediaInfo cursorToMediaBean(Cursor cursor, LocalMediaInfo localMediaInfo, int minSize) {
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
    public void insertForList(Context context, List<LocalMediaInfo> medias) {

    }

    @Override
    public void updateForList(Context context, List<LocalMediaInfo> medias) {

    }

    @Override
    public void deleteForList(Context context, List<LocalMediaInfo> medias) {

    }

    @Override
    public void insertOrUpdate(Context context, LocalMediaInfo localMediaInfo) {

    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return cursorWrapper.cursor.getInt(cursorWrapper.cursor.getColumnIndexOrThrow(IMediaDbData.MEDIA_ID));
    }
}
