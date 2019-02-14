package com.cfox.asymediademo.db;

import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.cfox.asymedialib.core.DatabaseProvider;


public class LocalMediaDatabaseProvider extends DatabaseProvider {
    @Override
    public void addURI(UriMatcher uriMatcher) {
        uriMatcher.addURI(IMediaDbData.AUTHORITY, IMediaDbData.TABLE_FILES, 0);
    }

    @Override
    public SQLiteOpenHelper getOpenHelper(Context context) {
        return new DatabaseHelper(context);
    }

    @Override
    public String getTableName(UriMatcher uriMatcher, Uri uri) {
        int uriKey = uriMatcher.match(uri);
        String tableName = null;
        switch (uriKey) {
            case 0:
                tableName = IMediaDbData.TABLE_FILES;
                break;
        }
        return tableName;
    }
}
