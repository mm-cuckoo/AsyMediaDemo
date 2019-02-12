package com.cfox.asymediademo.db;

import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.cfox.asymediademo.db.core.DatabaseProvider;

public class LocalMediaDatabaseProvider extends DatabaseProvider{
    @Override
    public void addURI(UriMatcher uriMatcher) {
        uriMatcher.addURI(IMediaDbData.AUTHORITY, IMediaDbData.TABLE_FILES, 0);
        uriMatcher.addURI(IMediaDbData.AUTHORITY, IMediaDbData.TABLE_VIDEO_PLAN, 1);
        uriMatcher.addURI(IMediaDbData.AUTHORITY, IMediaDbData.TABLE_IMAGE_PLAN, 2);
        uriMatcher.addURI(IMediaDbData.AUTHORITY, IMediaDbData.TABLE_VIDEO_GENERATE_PLAN, 3);
        uriMatcher.addURI(IMediaDbData.AUTHORITY, IMediaDbData.TABLE_VIDEO_GENERATE, 4);
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
            case 1:
                tableName = IMediaDbData.TABLE_VIDEO_PLAN;
                break;
            case 2:
                tableName = IMediaDbData.TABLE_IMAGE_PLAN;
                break;
            case 3:
                tableName = IMediaDbData.TABLE_VIDEO_GENERATE_PLAN;
                break;
            case 4:
                tableName = IMediaDbData.TABLE_VIDEO_GENERATE;
                break;
        }
        return tableName;
    }
}
