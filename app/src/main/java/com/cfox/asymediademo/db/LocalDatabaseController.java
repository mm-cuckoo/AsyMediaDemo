package com.cfox.asymediademo.db;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.cfox.asymedialib.core.MediaInfo;
import com.cfox.asymedialib.core.db.AbsUDatabaseController;
import com.cfox.asymedialib.core.CursorWrapper;

import java.util.ArrayList;
import java.util.List;


public class LocalDatabaseController extends AbsUDatabaseController<LocalMediaInfo> {
    private static final String TAG = "LocalDatabaseController";

    private static Uri mFileUri = Uri.parse("content://" + IMediaDbData.AUTHORITY + "/" + IMediaDbData.TABLE_FILES);

    private CursorWrapper baseQuery(Context context, String where, String[] whereArgs, String sortOrder) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(mFileUri, new String[]{IMediaDbData.MEDIA_ID, IMediaDbData.FILE_PATH},
                where, whereArgs, sortOrder);
        return cursorToWrapper(cursor);
    }

    @Override
    public CursorWrapper queryImage(Context context, int startMediaId, int rowNum) {
        String where = IMediaDbData.TYPE + " = ? AND " + IMediaDbData.MEDIA_ID + " >= ?";
        String[] whereArgs = new String[]{"1", String.valueOf(startMediaId)};
        String sortOrder = IMediaDbData.ID + " ASC  LIMIT " + rowNum;
        return baseQuery(context, where, whereArgs, sortOrder);
    }

    @Override
    public CursorWrapper queryVideo(Context context, int startMediaId, int rowNum) {
        String where = IMediaDbData.TYPE + " = ? AND " + IMediaDbData.MEDIA_ID + " >= ?";
        String[] whereArgs = new String[]{"0", String.valueOf(startMediaId)};
        String sortOrder = IMediaDbData.ID + " ASC  LIMIT " + rowNum;
        return baseQuery(context, where, whereArgs, sortOrder);
    }

    @Override
    public CursorWrapper queryImageAndVideo(Context context, int startMediaId, int rowNum) {
        String where = IMediaDbData.MEDIA_ID + " >= ?";
        String[] whereArgs = {String.valueOf(startMediaId)};
        String sortOrder = IMediaDbData.MEDIA_ID + " ASC  LIMIT " + rowNum;

        return baseQuery(context, where, whereArgs, sortOrder);
    }

    @Override
    public void insertForList(Context context, List<MediaInfo> medias) {
        if (medias.size() == 0) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(medias.size());
        for (MediaInfo bean : medias) {
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

    @Override
    public void updateForList(Context context, List<MediaInfo> medias) {
        if (medias.size() == 0) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(medias.size());
        for (MediaInfo bean : medias) {
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

    @Override
    public void deleteForList(Context context, List<MediaInfo> medias) {
        if (medias == null || medias.size() == 0) return;
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(medias.size());
        for (MediaInfo bean : medias) {
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

    @Override
    public void insert(Context context, MediaInfo mediaInfo) {
        boolean isInsert;
        Log.d(TAG, "insert: id:" + mediaInfo.getId() + "  path:" + mediaInfo.getPath());
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

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return cursorWrapper.cursor.getInt(cursorWrapper.cursor.getColumnIndexOrThrow(IMediaDbData.MEDIA_ID));
    }

    @Override
    public LocalMediaInfo createMediaInfo() {
        return new LocalMediaInfo();
    }

    @Override
    public LocalMediaInfo cursorToMediaInfo(Cursor cursor, LocalMediaInfo localMediaInfo) {
        if (cursor == null || localMediaInfo == null) return null;
        int mediaId = cursor.getInt(cursor.getColumnIndex(IMediaDbData.MEDIA_ID));
        String filePath = cursor.getString(cursor.getColumnIndex(IMediaDbData.FILE_PATH));
        localMediaInfo.setMediaId(mediaId);
        localMediaInfo.setPath(filePath);
        return localMediaInfo;
    }
}
