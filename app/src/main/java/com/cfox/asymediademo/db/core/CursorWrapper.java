package com.cfox.asymediademo.db.core;

import android.database.Cursor;

public class CursorWrapper {
    public Cursor cursor;
    public void close() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public CursorWrapper setCursor(Cursor cursor) {
        this.cursor = cursor;
        return this;
    }
    public static CursorWrapper create() {
        return new CursorWrapper();
    }
}
