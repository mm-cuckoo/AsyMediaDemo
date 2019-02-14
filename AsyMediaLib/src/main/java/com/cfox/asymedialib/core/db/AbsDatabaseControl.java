package com.cfox.asymedialib.core.db;

import android.content.Context;
import android.database.Cursor;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;
import com.cfox.asymedialib.core.MediaInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsDatabaseControl<T extends MediaInfo> {

    public List<T> wrapperToBeans(CursorWrapper cursorWrapper) {
        List<T> list = new ArrayList<>();
        if (cursorWrapper == null) {
            return list;
        }
        try {
            do {
                T t = cursorToMediaBean(cursorWrapper.cursor, AsyConfig.getInstance().mFilterMinImageSize);
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
        return cursorToMediaBean(cursor, createMediaInfo(), imageMinSize);
    }

    public  T cursorToMediaBean(Cursor cursor, T t) {
        return cursorToMediaBean(cursor, t, 0);
    }

    public CursorWrapper cursorToWrapper(Cursor cursor) {
        CursorWrapper cursorWrapper = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursorWrapper = CursorWrapper.create(cursor);
        } else {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursorWrapper;
    }

    public abstract T cursorToMediaBean(Cursor cursor, T t, int minSize);
    public abstract CursorWrapper baseQuery(Context context, String where , String[] whereArgs, String sortOrder);
    public abstract CursorWrapper queryImageToCursor(Context context, int startId, int rowNum);
    public abstract CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum);
    public abstract CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum);
    public abstract int getMediaId(CursorWrapper cursorWrapper);
    public abstract T createMediaInfo();

}
