package com.cfox.asymediademo.db.core;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.cfox.asymediademo.db.IMediaDbData;

import java.util.ArrayList;
import java.util.List;

public class LocalMediaTools {

    private static final String TAG = "LocalMediaTools";

    private static Uri mFileUri = Uri.parse("content://" + IMediaDbData.AUTHORITY + "/" + IMediaDbData.TABLE_FILES);

    public static List<MediaInfo> queryAllImages(Context context) {
        String where = IMediaDbData.TYPE + " = ?";
        String[] whereArgs = new String[]{"1"};
        String sortOrder = IMediaDbData.ID + " ASC";
        return wrapperBean(queryLocalMediaBase(context, where, whereArgs, sortOrder));
    }

    public static List<MediaInfo> queryAllVideos(Context context) {
        String where = IMediaDbData.TYPE + " = ?";
        String[] whereArgs = new String[]{"0"};
        String sortOrder = IMediaDbData.ID + " ASC";
        return wrapperBean(queryLocalMediaBase(context, where, whereArgs, sortOrder));
    }


    public static List<MediaInfo> queryAllLocalMedias(Context context) {
        return wrapperBean(queryLocalMediaBase(context, null, null, null));
    }

    public static List<MediaInfo> queryLocalMedias(Context context, int startId, int rowNum) {
        return wrapperBean(queryLocalMediasCursor(context, startId, rowNum));
    }

    public static CursorWrapper queryLocalMediasCursor(Context context, int startId, int rowNum) {
        String where = IMediaDbData.MEDIA_ID + " >= ?";
        String[] whereArg = new String[]{String.valueOf(startId)};
        String sortOrder = IMediaDbData.MEDIA_ID + " ASC  LIMIT " + rowNum;
        return queryLocalMediaBase(context, where, whereArg, sortOrder);
    }

    private static CursorWrapper queryLocalMediaBase(Context context, String where, String[] whereArgs, String sortOrder) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(mFileUri, new String[]{IMediaDbData.MEDIA_ID, IMediaDbData.FILE_PATH},
                where, whereArgs, sortOrder);
        return CursorWrapper.create().setCursor(cursor);
    }

    private static List<MediaInfo> wrapperBean(CursorWrapper cursorWrapper) {
        List<MediaInfo> beans = null;
        if (cursorWrapper == null || cursorWrapper.cursor == null) {
            return null;
        }
        try {
            beans = new ArrayList<>();
            cursorWrapper.cursor.moveToFirst();
            do {
                MediaInfo mediaInfo = cursorToMediaBean(cursorWrapper.cursor, null);
                beans.add(mediaInfo);
            }
            while (cursorWrapper.cursor.moveToNext());
        } finally {
            cursorWrapper.close();
        }
        return beans;
    }

    public static MediaInfo cursorToMediaBean(Cursor cursor, MediaInfo bean) {
        if (cursor == null) return null;
        MediaInfo mediaInfo = bean != null ? bean : MediaInfo.MediaBeanFactory.create();
        int mediaId = cursor.getInt(cursor.getColumnIndex(IMediaDbData.MEDIA_ID));
        String filePath = cursor.getString(cursor.getColumnIndex(IMediaDbData.FILE_PATH));
        mediaInfo.setMediaId(mediaId);
        mediaInfo.setPath(filePath);
        return mediaInfo;
    }

    public static void insertMedias(Context context, List<MediaInfo> files) {
        if (files.size() == 0) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(files.size());
        for (MediaInfo bean : files) {
//            Log.d(TAG, "insertMedias: id: " + bean.getId() + "  path:" + bean.getPath());
            operations.add(ContentProviderOperation.newInsert(mFileUri)
                    .withValue(IMediaDbData.MEDIA_ID, bean.getId())
                    .withValue(IMediaDbData.FILE_PATH, bean.getPath())
                    .withValue(IMediaDbData.TYPE, bean.getType().startsWith("video") ? 0 : 1)
                    .withValue(IMediaDbData.HIDDEN, 0)
                    .build());

        }
        try {
            resolver.applyBatch(IMediaDbData.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    public static void updateMedias(Context context, List<MediaInfo> files) {
        if (files.size() == 0) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(files.size());
        for (MediaInfo bean : files) {
//            Log.d(TAG, "updateMedias: name:" + bean.getName());
            operations.add(ContentProviderOperation.newUpdate(mFileUri)
                    .withSelection(IMediaDbData.MEDIA_ID + " = ?", new String[]{String.valueOf(bean.getId())})
                    .withValue(IMediaDbData.FILE_PATH, bean.getPath())
                    .build());

        }
        try {
            resolver.applyBatch(IMediaDbData.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMedias(Context context, List<MediaInfo> beans) {
        if (beans == null || beans.size() == 0) return;
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(beans.size());
        for (MediaInfo bean : beans) {
            Log.d(TAG, "deleteMedias: path:" + bean.getPath());
            operations.add(ContentProviderOperation.newDelete(mFileUri)
                    .withSelection(IMediaDbData.MEDIA_ID + "=?", new String[]{String.valueOf(bean.getMediaId())})
                    .build());
        }
        try {
            resolver.applyBatch(IMediaDbData.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }


    public static void insertOrUpdate(Context context, MediaInfo mediaInfo) {
        boolean isInsert;
//        Log.d(TAG, "insertOrUpdate: id:" + mediaInfo.getId() + "  path:" + mediaInfo.getPath());
        String[] mediaId = new String[]{String.valueOf(mediaInfo.getId())};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(mFileUri, null, IMediaDbData.MEDIA_ID + " = ?",
                mediaId,null);

        if (cursor == null || cursor.getCount() == 0) {
            isInsert = true;
        } else {
            isInsert = false;
        }

        ContentValues values = new ContentValues();
        values.put(IMediaDbData.FILE_PATH, mediaInfo.getPath());
        values.put(IMediaDbData.TYPE, mediaInfo.getType().startsWith("video") ? 0 : 1);

        if (isInsert) {
            values.put(IMediaDbData.MEDIA_ID, mediaInfo.getId());
            values.put(IMediaDbData.HIDDEN, 0);
            resolver.insert(mFileUri, values);
        } else {
            resolver.update(mFileUri, values, IMediaDbData.MEDIA_ID + " = ? ", mediaId);
        }
    }

    public static int getLocalDatabaseMediaCount(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(mFileUri, new String[]{" COUNT(*) "},
                null, null, null, null);

        int count = 0;
        if (cursor == null) {
            Log.e(TAG, "query " + mFileUri + " cursor null.");
        } else {
            try {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return count;
    }
}
