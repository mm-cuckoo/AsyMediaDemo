package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsDatabaseControl<T extends MediaInfo> {
    public static final int sImageMinSize = 1024 * 1024;

    private  List<T> wrapperBean(CursorWrapper cursorWrapper) {
        List<T> list = new ArrayList<>();
        if (cursorWrapper == null || cursorWrapper.cursor == null) {
            return list;
        }
        try {
            cursorWrapper.cursor.moveToFirst();
            do {
                T t = cursorToMediaBean(cursorWrapper.cursor, sImageMinSize);
                if (t == null) {
                    continue;
                }
                list.add(t);
            } while (cursorWrapper.cursor.moveToNext());
        } finally {
            cursorWrapper.close();
        }
        return list;
    }

    public T cursorToMediaBean(Cursor cursor, int imageMinSize) {
        return cursorToMediaBean(cursor, null, imageMinSize);
    }

    public  T cursorToMediaBean(Cursor cursor, T t) {
        return cursorToMediaBean(cursor, t, 0);
    }

    public CursorWrapper cursorToWrapper(Cursor cursor) {
        CursorWrapper cursorWrapper = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursorWrapper = CursorWrapper.create().setCursor(cursor);
        } else {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursorWrapper;
    }

    public List<T> query(Context context, int startId, int rowNum ) {
        return wrapperBean(queryToCursorWrapper(context, startId, rowNum));
    }

    public abstract T cursorToMediaBean(Cursor cursor, T t, int minSize);
    public abstract CursorWrapper baseQuery(Context context, String where , String[] whereArgs, String sortOrder);
    public abstract CursorWrapper queryToCursorWrapper(Context context, int startId, int rowNum );
    public abstract CursorWrapper queryImageToCursor(Context context, int startId, int rowNum);
    public abstract CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum);
    public abstract CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum);
    public abstract int getMediaId(CursorWrapper cursorWrapper);

}
