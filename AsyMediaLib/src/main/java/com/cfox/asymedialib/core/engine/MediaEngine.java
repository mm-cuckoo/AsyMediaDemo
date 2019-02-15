package com.cfox.asymedialib.core.engine;

import android.content.Context;
import android.util.Log;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;

public abstract class MediaEngine {
    private final String TAG = getClass().getSimpleName();

    public static final int TYPE_IMAGE_VIDEO   = 0;
    public static final int TYPE_IMAGE         = 1;
    public static final int TYPE_VIDEO         = 2;

    private Context mContext;
    private CursorWrapper mCursorWrapper;
    private int mMediaId = 0;
    private int mMediaType = 0;
    private  int mRow_num = 0;

    public abstract CursorWrapper queryImage(Context context, int startMediaId, int rowNum );
    public abstract CursorWrapper queryVideo(Context context, int startMediaId, int rowNum );
    public abstract CursorWrapper queryImageAndVideo(Context context, int startMediaId, int rowNum );
    public abstract int getMediaId(CursorWrapper cursor);

    MediaEngine(Context mContext, int mediaType) {
        this.mContext = mContext;
        this.mMediaType = mediaType;
        this.mRow_num = AsyConfig.getInstance().mQueryOnceRowNumber;
    }

    CursorWrapper getMediaCursor() {
        if (mCursorWrapper == null) {
            mCursorWrapper = dispatchCursorWrapper(mContext, mMediaId, mRow_num, mMediaType);
            if (mCursorWrapper == null) {
                return null;
            } else {
                return mCursorWrapper;
            }
        }

        if (mCursorWrapper.cursor.moveToNext()) {
            mMediaId = mediaId(mCursorWrapper) + 1;// move to next index
            if(AsyConfig.Debug) {
                Log.d(TAG, "getMediaCursor >>>> Media id : " + mMediaId);
            }
            return mCursorWrapper;
        }

        if (mCursorWrapper.cursor.getCount() < mRow_num) {
            closeCursor();
            return null;
        }

        if (mCursorWrapper != null) {
            closeCursor();
        }

        mCursorWrapper = dispatchCursorWrapper(mContext, mMediaId, mRow_num, mMediaType);
        if (mCursorWrapper == null) {
            closeCursor();
            return null;
        }
        return mCursorWrapper;
    }

    private CursorWrapper dispatchCursorWrapper(Context context, int startMediaId, int rowNum , int type) {

        if(AsyConfig.Debug) {
            Log.d(TAG, "dispatchCursorWrapper >>> startMediaId:" + startMediaId + " RowNum: " + rowNum +  "  type: " + type);
        }

        CursorWrapper cursorWrapper;
        switch (type) {
            case TYPE_IMAGE_VIDEO:
                cursorWrapper = queryImageAndVideo(context, startMediaId, rowNum);
                break;
            case TYPE_IMAGE:
                cursorWrapper = queryImage(context, startMediaId, rowNum);
                break;

            case TYPE_VIDEO:
                cursorWrapper = queryVideo(context, startMediaId, rowNum);
                break;
            default:
                cursorWrapper = queryImageAndVideo(context, startMediaId, rowNum);
        }
        if (cursorWrapper != null) {
            cursorWrapper.cursor.moveToFirst();
        } else {
            Log.i(TAG, "dispatchCursorWrapper: cursorWrapper is null ....");
        }
        return cursorWrapper;

    }

    private int mediaId(CursorWrapper cursorWrapper) {
        if (cursorWrapper == null) return -1;
        return getMediaId(cursorWrapper);
    }

    private void closeCursor() {
        if (mCursorWrapper != null) {
            mCursorWrapper.close();
            mCursorWrapper = null;
        }
    }

}
