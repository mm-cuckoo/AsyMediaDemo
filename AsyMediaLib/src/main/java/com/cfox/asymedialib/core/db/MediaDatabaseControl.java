package com.cfox.asymedialib.core.db;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;
import com.cfox.asymedialib.core.MediaInfo;

import java.io.File;

public class MediaDatabaseControl extends AbsMDatabaseControl<MediaInfo> {
    private static final String TAG = "MediaDatabaseControl";
    @Override
    public CursorWrapper baseQuery(Context context, String where, String[] whereArgs, String sortOrder) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"), null,
                where, whereArgs, sortOrder);
        return cursorToWrapper(cursor);
    }

    @Override
    public CursorWrapper queryImageToCursor(Context context, int startId, int rowNum) {
        String where = MediaStore.Images.Media.MIME_TYPE + " LIKE ? ";
        String[] whereArgs = {"image/%"};
        String sortOrder = MediaStore.MediaColumns._ID + " ASC  LIMIT " + rowNum;
        return baseQuery(context, where, whereArgs, sortOrder);
    }

    @Override
    public CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum) {
        String where = MediaStore.Video.Media.MIME_TYPE + " LIKE ? ";
        String[] whereArgs = {"video/%"};
        String sortOrder = MediaStore.MediaColumns._ID + " ASC  LIMIT " + rowNum;
        return baseQuery(context, where, whereArgs, sortOrder);
    }

    @Override
    public CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum) {
        String where = "(" + MediaStore.Images.Media.MIME_TYPE + " LIKE ? OR " +
                MediaStore.Video.Media.MIME_TYPE + " LIKE ? ) AND " +
                MediaStore.MediaColumns._ID + " >= ?";
        String[] whereArgs = {"image/%", "video/%", String.valueOf(startId)};
        String sortOrder = MediaStore.MediaColumns._ID + " ASC  LIMIT " + rowNum;
        return baseQuery(context, where, whereArgs, sortOrder);
    }

    @Override
    public int getMediaId(CursorWrapper cursorWrapper) {
        return cursorWrapper.cursor.getInt(cursorWrapper.cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
    }

    @Override
    public MediaInfo createMediaInfo() {
        return new MediaInfo();
    }

    @Override
    public MediaInfo queryMeidaForId(Context context, long id) {
        String where = "(" + MediaStore.Images.Media.MIME_TYPE + " LIKE ? OR " +
                MediaStore.Video.Media.MIME_TYPE + " LIKE ? ) AND " +
                MediaStore.MediaColumns._ID + " = ?";
        String[] whereArgs = {"image/%", "video/%", String.valueOf(id)};
        CursorWrapper cursorWrapper = baseQuery(context, where, whereArgs, null);
        return cursorWrapper == null ? null : cursorToMediaBean(cursorWrapper.cursor, new MediaInfo(), 0);
    }

    @Override
    public MediaInfo cursorToMediaBean(Cursor cursor, MediaInfo mediaInfo, int minSize) {
        if (cursor == null || mediaInfo == null) return null;
        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));

        if(AsyConfig.isDebug) {
            Log.d(TAG, "cursorToMediaBean >>> type : " + type + " size:" + size );
        }

        if (type.toLowerCase().contains("image") && minSize > size) {
            return null;
        }

        mediaInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)));
        mediaInfo.setMediaId(mediaInfo.getId());
        mediaInfo.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
        mediaInfo.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
        mediaInfo.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
        mediaInfo.setTime(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)));
        mediaInfo.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH)));
        mediaInfo.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT)));
        if (type.toLowerCase().contains("video")) {
            mediaInfo.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
            mediaInfo.setResolution(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)));
            mediaInfo.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)));
        }
        mediaInfo.setSize(size);
        mediaInfo.setType(type);
        File file = new File(mediaInfo.getPath());
        String folder = file.getParent();
        folder = folder.substring(folder.lastIndexOf("/") + 1, folder.length());
        mediaInfo.setFolder(folder);
        if(AsyConfig.isDebug) {
            Log.d(TAG, "cursorToMediaBean >>> MediaInfo:"  + mediaInfo.toString());
        }
        return mediaInfo;
    }
}
