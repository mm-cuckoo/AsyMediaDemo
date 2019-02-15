package com.cfox.asymedialib.core.engine;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cfox.asymedialib.AsyConfig;
import com.cfox.asymedialib.core.CursorWrapper;
import com.cfox.asymedialib.core.DatabaseControlHandler;
import com.cfox.asymedialib.core.MediaInfo;

public class CheckEngine {

    private static final String TAG = "CheckEngine";

    private static final int TYPE_NO            = 0;
    private static final int TYPE_MOVE_ALL      = 1;
    private static final int TYPE_LOCAL_NEXT    = 2;
    private static final int TYPE_MEDIA_NEXT    = 3;
    private static final int TYPE_LOCAL_NO_NEXT = 4;
    private static final int TYPE_MEDIA_NO_NEXT = 5;


    private DatabaseControlHandler mControlHandler;

    private MediaInfo mMediaInfo;
    private MediaInfo mLocalMediaInfo;

    private CursorWrapper mLocalCursor;
    private CursorWrapper mMediaCursor;
    private AsyConfig mAsyConfig;

    public CheckEngine(Context context) {
        mAsyConfig = AsyConfig.getInstance();
        mControlHandler = new DatabaseControlHandler(context);
    }

    public void checkCenter(LocalMediaEngine localEngine, MediaProviderEngine mediaProviderEngine) {
        mLocalCursor = localEngine.getMediaCursor();// if count 0 , return null
        mMediaCursor = mediaProviderEngine.getMediaCursor();

        if (mLocalCursor != null) {
            mLocalMediaInfo = mAsyConfig.mUDatabaseControl.cursorToMediaInfo(mLocalCursor.cursor,
                    mAsyConfig.mUDatabaseControl.createMediaInfo());
        }
        if (mMediaCursor != null){
            mMediaInfo = mAsyConfig.mMDatabaseControl.cursorToMediaInfo(mMediaCursor.cursor,
                    mAsyConfig.mMDatabaseControl.createMediaInfo());
        }

        while (true) {
            switch (checkCursor(mLocalMediaInfo, mMediaInfo)) {
                case TYPE_MOVE_ALL:
                    mediaCursorNext(mediaProviderEngine);
                    localCursorNext(localEngine);
                    break;

                case TYPE_MEDIA_NEXT:
                case TYPE_LOCAL_NO_NEXT:
                    mediaCursorNext(mediaProviderEngine);
                    break;

                case TYPE_LOCAL_NEXT:
                case TYPE_MEDIA_NO_NEXT:
                    localCursorNext(localEngine);
                    break;
                case TYPE_NO:
                    postBundle(null,DatabaseControlHandler.FLAG_FLUSH_DELAY);
                    return;
            }
        }
    }

    private int checkCursor(MediaInfo localInfo, MediaInfo mediaInfo) {
        int localMediaId;
        int mediaId;
        if (localInfo == null && mediaInfo == null) {
            return TYPE_NO;
        } else if (localInfo == null) {
            localMediaId = -1;
            mediaId = mediaInfo.getMediaId();
        } else if (mediaInfo == null) {
            mediaId = -1;
            localMediaId = localInfo.getMediaId();
        } else {
            localMediaId = localInfo.getMediaId();
            mediaId = mediaInfo.getMediaId();
        }

        if(AsyConfig.Debug) {
            Log.d(TAG, "checkCursor: media id:" + mediaId);
            Log.d(TAG, "checkCursor: local media id:" + localMediaId);
        }

        if (AsyConfig.DEBUG_INFO) {
            Log.e(TAG, "checkCursor: \n localInfo:" + localInfo  + "\n mediaInfo:" + mediaInfo);
        }

        if (localMediaId == mediaId) {
            //check update
            if (checkUpdate(localInfo, mediaInfo)) {
                postBundle(mediaInfo, DatabaseControlHandler.FLAG_UPDATE_DELAY);
            }
            return TYPE_MOVE_ALL;
        }
        if (mediaId == -1) {
            postBundle(localInfo, DatabaseControlHandler.FLAG_DELETE_DELAY);
            return TYPE_MEDIA_NO_NEXT;
        }

        if (localMediaId == -1) {
            postBundle(mediaInfo, DatabaseControlHandler.FLAG_INSERT_DELAY);
            return TYPE_LOCAL_NO_NEXT;
        }

        if (localMediaId < mediaId) {
            //delete form local
            postBundle(localInfo, DatabaseControlHandler.FLAG_DELETE_DELAY);
            return TYPE_LOCAL_NEXT;
        }
        // insert media to local
        // local id = -1 or local id > media id

        postBundle(mediaInfo, DatabaseControlHandler.FLAG_INSERT_DELAY);
        return TYPE_MEDIA_NEXT;

    }

    private void postBundle(MediaInfo bean, int flag) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DatabaseControlHandler.KEY_VALUE, bean);
        bundle.putInt(DatabaseControlHandler.KEY_FLAG, flag);
        mControlHandler.postMsg(bundle);
    }

    private void mediaCursorNext(MediaProviderEngine mediaProviderEngine) {
        mMediaCursor = mediaProviderEngine.getMediaCursor();
        if (mMediaCursor == null) {
            mMediaInfo = null;
            return;
        }
        mMediaInfo = mAsyConfig.mMDatabaseControl.cursorToMediaInfo(mMediaCursor.cursor,
                mAsyConfig.mMDatabaseControl.createMediaInfo());
    }

    private void localCursorNext(LocalMediaEngine localMediaEngine) {
        mLocalCursor = localMediaEngine.getMediaCursor();
        if (mLocalCursor == null) {
            mLocalMediaInfo = null;
            return;
        }
        mLocalMediaInfo = mAsyConfig.mUDatabaseControl.cursorToMediaInfo(mLocalCursor.cursor,
                mAsyConfig.mUDatabaseControl.createMediaInfo());
    }

    private boolean checkUpdate(MediaInfo localBean, MediaInfo mediaInfo) {
        return mAsyConfig.checkRule.checkUpdate(localBean, mediaInfo);
    }

    public void insertNow(long mediaId) {
        if(AsyConfig.Debug) {
            Log.d(TAG, "insert media id:" + mediaId);
        }
        MediaInfo mediaInfo = AsyConfig.getInstance().mMDatabaseControl.queryMeidaForId(mAsyConfig.mContext, mediaId);
        if (mediaInfo == null) return;
        postBundle(mediaInfo, DatabaseControlHandler.FLAG_INSERT_NOW);
    }
}
