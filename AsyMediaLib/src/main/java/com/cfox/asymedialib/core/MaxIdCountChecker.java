package com.cfox.asymedialib.core;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MaxIdCountChecker {

    private static final String TAG = "MaxIdCountChecker";

    private static int mMaxId = -1;
    private static int mCount = -1;

    private static final Uri MEDIA_URI = MediaStore.Files.getContentUri("external");

    public static boolean check(Context context) {
        final String[] projection = {"MAX(_id), COUNT(*)"};
        final String where = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " +
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=?";

        final String[] args = {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};

        Cursor cursor = context.getContentResolver().query(MEDIA_URI, projection, where, args, null);
        if (cursor == null) {
            Log.e(TAG, "query " + MEDIA_URI + " cursor null.");
            return true;
        } else {
            try {
                if (cursor.moveToFirst()) {
                    int max_id = cursor.getInt(0);
                    int count = cursor.getInt(1);
//                    Log.d(TAG, "check: mMaxId:" + mMaxId + "  max_id:" + max_id +
//                            "  mCount:" + mCount + "  count:" + count);
                    boolean changed = max_id != mMaxId || count != mCount;
                    mMaxId = max_id;
                    mCount = count;
                    return changed;
                } else {
                    Log.e(TAG, "query " + MEDIA_URI + " cursor empty.");
                    return true;
                }
            } finally {
                cursor.close();
            }
        }
    }
}
