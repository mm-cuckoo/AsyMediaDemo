package com.cfox.asymedialib.core.db;

import android.content.Context;
import android.database.Cursor;

import com.cfox.asymedialib.core.CursorWrapper;
import com.cfox.asymedialib.core.MediaInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsDatabaseController<T extends MediaInfo> {

    public List<T> wrapperToBeans(CursorWrapper cursorWrapper) {
        List<T> list = new ArrayList<>();
        if (cursorWrapper == null) {
            return list;
        }
        try {
            do {
                T t = cursorToMediaInfo(cursorWrapper.cursor);
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

    private T cursorToMediaInfo(Cursor cursor) {
        return cursorToMediaInfo(cursor, createMediaInfo());
    }

    protected CursorWrapper cursorToWrapper(Cursor cursor) {
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

    public abstract T cursorToMediaInfo(Cursor cursor, T t);
    public abstract CursorWrapper queryImage(Context context, int startMediaId, int rowNum);
    public abstract CursorWrapper queryVideo(Context context, int startMediaId, int rowNum);
    public abstract CursorWrapper queryImageAndVideo(Context context, int startMediaId, int rowNum);
    public abstract CursorWrapper queryImage(Context context, int minSize, int maxSize, int startMediaId, int rowNum);
    public abstract CursorWrapper queryVideo(Context context, int minSize, int maxSize, int startMediaId, int rowNum);
    public abstract CursorWrapper queryImageAndVideo(Context context, int imageMinSize,
                                                     int imageMaxSize, int videoMinSize,
                                                     int videoMaxSzie , int startMediaId, int rowNum);

    public abstract int getMediaId(CursorWrapper cursorWrapper);
    public abstract T createMediaInfo();

}
