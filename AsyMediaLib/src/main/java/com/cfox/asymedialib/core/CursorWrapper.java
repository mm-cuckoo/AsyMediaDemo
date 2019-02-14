package com.cfox.asymedialib.core;

import android.database.Cursor;

public class CursorWrapper {
    public Cursor cursor;
    public void close() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public CursorWrapper(Cursor cursor) {
        this.cursor = cursor;
        this.cursor.moveToFirst();
    }

    public static CursorWrapper create(Cursor cursor) {
        return new CursorWrapper(cursor);
    }
}
