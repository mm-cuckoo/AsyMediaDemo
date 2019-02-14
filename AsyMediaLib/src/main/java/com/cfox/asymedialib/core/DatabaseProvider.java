package com.cfox.asymedialib.core;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public abstract class DatabaseProvider extends ContentProvider {
    private static final String TAG = "DatabaseProvider";

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase db;

    public abstract void addURI(UriMatcher uriMatcher);
    public abstract SQLiteOpenHelper getOpenHelper(Context context);
    public abstract String getTableName(UriMatcher uriMatcher,Uri uri);

    private String getTableName(Uri uri) {
        return getTableName(sUriMatcher, uri);
    }

    @Override
    public boolean onCreate() {
        addURI(sUriMatcher);
        SQLiteOpenHelper mHelper = getOpenHelper(getContext());
        db = mHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            Log.e(TAG, "query: table name is null");
            return null;
        }
        return db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            Log.e(TAG, "query: table name is null");
            return null;
        }
        long rawId = db.insert(tableName, null, values);
        return ContentUris.withAppendedId(uri, rawId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            Log.e(TAG, "query: table name is null");
            return 0;
        }
        return db.delete(tableName, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            Log.e(TAG, "query: table name is null");
            return 0;
        }
        return db.update(tableName, values, selection, selectionArgs);
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {

        ContentProviderResult[] results;
        synchronized (this) {
            try {
                db.beginTransaction();
                results = super.applyBatch(operations);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        return results;
    }

}
