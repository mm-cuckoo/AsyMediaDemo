package com.cfox.asymedialib.core.db;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;
import com.cfox.asymedialib.core.MediaInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaDatabaseController extends AbsMDatabaseController<MediaInfo> {
    private static final String TAG = "MediaDatabaseController";

    private static final int QUERY_TYPE_IMAGE_VIDEO  = 0;
    private static final int QUERY_TYPE_IMAGE        = 1;
    private static final int QUERY_TYPE_VIDEO        = 2;


    private CursorWrapper baseQuery(Context context, String where, String[] whereArgs, String sortOrder) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"), null,
                where, whereArgs, sortOrder);
        return cursorToWrapper(cursor);
    }

    @Override
    public CursorWrapper queryImage(Context context, int startMediaId, int rowNum) {
        return baseQueryImageAndVideoToCursor(context, startMediaId, rowNum,  QUERY_TYPE_IMAGE);
    }

    @Override
    public CursorWrapper queryVideo(Context context, int startMediaId, int rowNum) {
        return baseQueryImageAndVideoToCursor(context, startMediaId, rowNum,  QUERY_TYPE_VIDEO);
    }

    @Override
    public CursorWrapper queryImageAndVideo(Context context, int startMediaId, int rowNum) {
        return baseQueryImageAndVideoToCursor(context, startMediaId, rowNum,  QUERY_TYPE_IMAGE_VIDEO);
    }

    @Override
    public CursorWrapper queryImage(Context context, int minSize, int maxSize, int startMediaId, int rowNum) {
        return baseQueryImageAndVideoToCursor(context,
                minSize, maxSize, 0, 0, startMediaId, rowNum,  QUERY_TYPE_IMAGE);
    }

    @Override
    public CursorWrapper queryVideo(Context context, int minSize, int maxSize, int startMediaId, int rowNum) {
        return baseQueryImageAndVideoToCursor(context,
                0, 0, minSize, maxSize, startMediaId, rowNum,  QUERY_TYPE_VIDEO);
    }

    @Override
    public CursorWrapper queryImageAndVideo(Context context,
                                            int imageMinSize, int imageMaxSize,
                                            int videoMinSize, int videMaxSzie,
                                            int startMediaId, int rowNum) {
        return baseQueryImageAndVideoToCursor(context,
                imageMinSize, imageMaxSize, videoMinSize, videMaxSzie, startMediaId, rowNum,  QUERY_TYPE_IMAGE_VIDEO);
    }

    private CursorWrapper baseQueryImageAndVideoToCursor(Context context, int startMediaId, int rowNum, int queryType) {
        return baseQueryImageAndVideoToCursor(context,
                0, 0, 0, 0, startMediaId, rowNum,  queryType);
    }

    private CursorWrapper baseQueryImageAndVideoToCursor(Context context,
                                                    int imageMinSize, int imageMaxSize,
                                                    int videoMinSize, int videMaxSzie,
                                                    int startMediaId, int rowNum, int queryType) {


        StringBuilder whereBuilder = new StringBuilder();
        whereBuilder.append(MediaStore.MediaColumns._ID).append(" >= ? ").append(" AND ").append(" ( ");

        List<String> args = new ArrayList<>();
        args.add(String.valueOf(startMediaId));

        switch (queryType) {
            case QUERY_TYPE_IMAGE_VIDEO:// all
                whereBuilder.append(" ( ").append(MediaStore.MediaColumns.MIME_TYPE).append(" LIKE ? ").append(" AND ");
                args.add("image/%");

                whereBuilder.append(MediaStore.MediaColumns.SIZE).append(" >= ? ");
                args.add(String.valueOf(imageMinSize));

                if (imageMaxSize > 0) {
                    whereBuilder.append(" AND ").append(MediaStore.MediaColumns.SIZE).append(" < ? ");
                    args.add(String.valueOf(imageMaxSize));
                }

                whereBuilder.append(" ) OR (").append(MediaStore.MediaColumns.MIME_TYPE).append(" LIKE ? ").append(" AND ");
                args.add("video/%");

                whereBuilder.append(MediaStore.MediaColumns.SIZE).append(" >= ? ");
                args.add(String.valueOf(videoMinSize));

                if (videMaxSzie > 0) {
                    whereBuilder.append(" AND ").append(MediaStore.MediaColumns.SIZE).append(" < ? ");
                    args.add(String.valueOf(videMaxSzie));
                }
                whereBuilder.append(" )");

                break;

            case QUERY_TYPE_IMAGE: // image
                whereBuilder.append(MediaStore.MediaColumns.MIME_TYPE).append(" LIKE ? ").append(" AND ");
                args.add("image/%");

                whereBuilder.append(MediaStore.MediaColumns.SIZE).append(" >= ? ");
                args.add(String.valueOf(imageMinSize));

                if (imageMaxSize > 0) {
                    whereBuilder.append(" AND ").append(MediaStore.MediaColumns.SIZE).append(" < ? ");
                    args.add(String.valueOf(imageMaxSize));
                }
                break;

            case QUERY_TYPE_VIDEO: //video
                whereBuilder.append(MediaStore.MediaColumns.MIME_TYPE).append(" LIKE ? ").append(" AND ");
                args.add("video/%");

                whereBuilder.append(MediaStore.MediaColumns.SIZE).append(" >= ? ");
                args.add(String.valueOf(videoMinSize));

                if (videMaxSzie > 0) {
                    whereBuilder.append(" AND ").append(MediaStore.MediaColumns.SIZE).append(" < ? ");
                    args.add(String.valueOf(videMaxSzie));
                }
                break;
        }
        whereBuilder.append(")");
        String where = whereBuilder.toString();

        String[] whereArgsBuffer = new String[args.size()];
        String[] whereArgs = args.toArray(whereArgsBuffer);

        String sortOrder = MediaStore.MediaColumns._ID + " ASC  LIMIT " + rowNum;

        if(AsyConfig.DEBUG_INFO) {
            Log.d(TAG, "queryImageAndVideo : \n where:" + where  + " \n whereArgs:" + Arrays.toString(whereArgs) + "\n sortOrder:" + sortOrder);
        }

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
        return cursorWrapper == null ? null : cursorToMediaInfo(cursorWrapper.cursor, new MediaInfo());
    }

    @Override
    public MediaInfo cursorToMediaInfo(Cursor cursor, MediaInfo mediaInfo) {
        if (cursor == null || mediaInfo == null) return null;
        String type = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));

        if(AsyConfig.Debug) {
            Log.d(TAG, "cursorToMediaInfo >>> type : " + type );
        }

        mediaInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)));
        mediaInfo.setMediaId(mediaInfo.getId());
        mediaInfo.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)));
        mediaInfo.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
        mediaInfo.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
        mediaInfo.setTime(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)));
        mediaInfo.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH)));
        mediaInfo.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT)));
        mediaInfo.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
        if (type.toLowerCase().contains("video")) {
            mediaInfo.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
            mediaInfo.setResolution(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)));
            mediaInfo.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)));
        }
        mediaInfo.setType(type);
        File file = new File(mediaInfo.getPath());
        String folder = file.getParent();
        folder = folder.substring(folder.lastIndexOf("/") + 1, folder.length());
        mediaInfo.setFolder(folder);
        if(AsyConfig.DEBUG_INFO) {
            Log.d(TAG, "cursorToMediaInfo >>> MediaInfo:"  + mediaInfo.toString());
        }
        return mediaInfo;
    }
}
