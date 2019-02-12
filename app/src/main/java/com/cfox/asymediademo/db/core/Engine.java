package com.cfox.asymediademo.db.core;

import android.content.Context;

public abstract class Engine {
    private static final String TAG = "Engine";

    public static final int TYPE_IMAGE_VIDEO   = 0;
    public static final int TYPE_IMAGE         = 1;
    public static final int TYPE_VIDEO         = 2;

    private static final int ROW_NUM = 20;


    private Context mContext;
    private CursorWrapper mCursorWrapper;
    private int mMediaId = 0;
    private int mMediaType = 0;

    public abstract CursorWrapper queryImageToCursor(Context context, int startId, int rowNum );
    public abstract CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum );
    public abstract CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum );
    public abstract int getMediaId(CursorWrapper cursor);

    public Engine(Context mContext , int mediaType) {
        this.mContext = mContext;
        this.mMediaType = mediaType;
    }

    public CursorWrapper getMeidaCursor() {
        if (mCursorWrapper == null) {
            mCursorWrapper = dispatchCursor(mContext, mMediaId, ROW_NUM, mMediaType);
            if (mCursorWrapper == null) {
                return null;
            } else {
                return mCursorWrapper;
            }
        }

        if (mCursorWrapper.cursor.moveToNext()) {
            mMediaId = mediaId(mCursorWrapper) + 1;// move to next index
            return mCursorWrapper;
        }

        if (mCursorWrapper.cursor.getCount() < ROW_NUM) {
            closeCusor();
            return null;
        }

        if (mCursorWrapper != null) {
            closeCusor();
        }

        mCursorWrapper = dispatchCursor(mContext, mMediaId, ROW_NUM, mMediaType);
        if (mCursorWrapper == null) {
            closeCusor();
            return null;
        }
        return mCursorWrapper;
    }

    private CursorWrapper dispatchCursor(Context context, int startId, int rowNum , int type) {
        CursorWrapper cursorWrapper;
        switch (type) {
            case TYPE_IMAGE_VIDEO:
                cursorWrapper = queryImageAndVideoToCursor(context, startId, rowNum);
                break;
            case TYPE_IMAGE:
                cursorWrapper = queryImageToCursor(context, startId, rowNum);
                break;

            case TYPE_VIDEO:
                cursorWrapper = queryVideoToCursor(context, startId, rowNum);
                break;
            default:
                cursorWrapper = queryImageAndVideoToCursor(context, startId, rowNum);
        }
        if (cursorWrapper != null) {
            cursorWrapper.cursor.moveToFirst();
        }
        return cursorWrapper;

    }

    private int mediaId(CursorWrapper cursorWrapper) {
        if (cursorWrapper == null) return -1;
        return getMediaId(cursorWrapper);
    }

    private void closeCusor() {
        if (mCursorWrapper != null) {
            mCursorWrapper.close();
            mCursorWrapper = null;
        }
    }

}
