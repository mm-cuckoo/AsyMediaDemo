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

    public abstract CursorWrapper queryImageToCursor(Context context, int startId, int rowNum );
    public abstract CursorWrapper queryVideoToCursor(Context context, int startId, int rowNum );
    public abstract CursorWrapper queryImageAndVideoToCursor(Context context, int startId, int rowNum );
    public abstract int getMediaId(CursorWrapper cursor);

    public MediaEngine(Context mContext , int mediaType) {
        this.mContext = mContext;
        this.mMediaType = mediaType;
        this.mRow_num = AsyConfig.getInstance().mQueryOnceRowNumber;
    }

    public CursorWrapper getMeidaCursor() {
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
            if(AsyConfig.isDebug) {
                Log.d(TAG, "getMeidaCursor >>>> Media id : " + mMediaId);
            }
            return mCursorWrapper;
        }

        if (mCursorWrapper.cursor.getCount() < mRow_num) {
            closeCusor();
            return null;
        }

        if (mCursorWrapper != null) {
            closeCusor();
        }

        mCursorWrapper = dispatchCursorWrapper(mContext, mMediaId, mRow_num, mMediaType);
        if (mCursorWrapper == null) {
            closeCusor();
            return null;
        }
        return mCursorWrapper;
    }

    private CursorWrapper dispatchCursorWrapper(Context context, int startId, int rowNum , int type) {

        if(AsyConfig.isDebug) {
            Log.d(TAG, "dispatchCursorWrapper >>> startId:" + startId + " RowNum: " + rowNum +  "  type: " + type);
        }

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
