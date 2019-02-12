package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaTools {
    private static final String TAG = "MediaTools";

    public static final int sImageMinSize = 1024 * 1024;

    private static CursorWrapper baseQuery(Context context, String where ,
                                             String[] whereArgs, String sortOrder) {

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"), null,
                where, whereArgs, sortOrder);

        CursorWrapper cursorWrapper = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursorWrapper = CursorWrapper.create().setCursor(cursor);
        } else {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursorWrapper;
    }
    private static List<MediaInfo> wrapperBean(CursorWrapper cursorWrapper) {
        List<MediaInfo> list = new ArrayList<>();
        if (cursorWrapper == null || cursorWrapper.cursor == null) {
            return list;
        }
        try {
            while (cursorWrapper.cursor.moveToNext()) {
                MediaInfo mediaInfo = cursorToMediaBean(cursorWrapper.cursor, sImageMinSize);
                if (mediaInfo == null) {
                    continue;
                }
                list.add(mediaInfo);
            }
        } finally {
            cursorWrapper.close();
        }
        return list;
    }

    public static List<MediaInfo> getProviderMedia(Context context, int startId, int rowNum ) {
        return wrapperBean(getProviderMediaCursor(context, startId, rowNum));
    }

    public static CursorWrapper getProviderMediaCursor(Context context, int startId, int rowNum ) {
        String where = "(" + MediaStore.Images.Media.MIME_TYPE + " LIKE ? OR " +
                MediaStore.Video.Media.MIME_TYPE + " LIKE ? ) AND " +
                MediaStore.MediaColumns._ID + " >= ?";
        String[] whereArgs = {"image/%", "video/%", String.valueOf(startId)};
        String sortOrder = MediaStore.MediaColumns._ID + " ASC  LIMIT " + rowNum;
        return baseQuery(context, where, whereArgs, sortOrder);
    }


    public static List<MediaInfo> getProviderImages(Context context) {
        return wrapperBean(getProviderImagesCursor(context));
    }

    public static CursorWrapper getProviderImagesCursor(Context context) {
        String where = MediaStore.Images.Media.MIME_TYPE + " LIKE ? ";
        String[] whereArgs = {"image/%"};
        String sortOrder = MediaStore.MediaColumns._ID + " ASC";
        return baseQuery(context, where, whereArgs, sortOrder);
    }

    public static List<MediaInfo> getProviderVideos(Context context) {
        return wrapperBean(getProviderVideosCursor(context));
    }

    public static CursorWrapper getProviderVideosCursor(Context context) {
        String where = MediaStore.Video.Media.MIME_TYPE + " LIKE ? ";
        String[] whereArgs = {"video/%"};
        String sortOrder = MediaStore.MediaColumns._ID + " ASC";
        return baseQuery(context, where, whereArgs, sortOrder);
    }

    public static MediaInfo queryForId(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.MediaColumns._ID + " = ?",
                new String[]{String.valueOf(id)}, null);

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        MediaInfo mediaInfo;
        try {
            mediaInfo = cursorToMediaBean(cursor, sImageMinSize);
        } finally {
            cursor.close();
        }
        return mediaInfo;
    }


    public static MediaInfo getProviderVideoForId(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, MediaStore.MediaColumns._ID + " = ?",
                new String[]{String.valueOf(id)}, null);

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        MediaInfo mediaInfo;
        try {
            mediaInfo = cursorToMediaBean( cursor, 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mediaInfo;
    }

    public static int getProviderMediaCount(Context context) {
        final Uri uri = MediaStore.Files.getContentUri("external");
        final String[] projection = {"COUNT(*)"};
        final String where = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " +
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " +
                MediaStore.Files.FileColumns.SIZE + " > ?";

        final String[] args = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO), String.valueOf(sImageMinSize)};

        Cursor cursor = context.getContentResolver().query(uri, projection, where, args, null);
        int count = 0;
        if (cursor == null) {
            Log.e(TAG, "query " + uri + " cursor null.");
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
    public static MediaInfo cursorToMediaBean(Cursor cursor, int imageMinSize) {
        return cursorToMediaBean(cursor, null, imageMinSize);
    }

    public static MediaInfo cursorToMediaBean(Cursor cursor, MediaInfo bean) {
        return cursorToMediaBean(cursor, bean, 0);
    }

    public static MediaInfo cursorToMediaBean(Cursor cursor, MediaInfo bean, int imageMinSize) {
        if (cursor == null) return null;
        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
        if (type.toLowerCase().contains("image") && imageMinSize > size) {
            return null;
        }
        MediaInfo mediaInfo = bean != null ? bean : MediaInfo.MediaBeanFactory.create();
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
        Log.e(TAG, "getProviderMedia: " + mediaInfo.toString());
        return mediaInfo;
    }

}
