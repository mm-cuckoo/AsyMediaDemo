package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.os.Bundle;

public class CheckEngine {
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

    public void checkCenter(LocalMediaEngine localEngine, MediaEngine mediaEngine) {
        mLocalCursor = localEngine.getMeidaCursor();// if count 0 , return null
        mMediaCursor = mediaEngine.getMeidaCursor();

        if (mLocalCursor != null) {
            mLocalMediaInfo = mAsyConfig.mMediaFactory.createLocalMediaInfo();
            LocalMediaTools.cursorToMediaBean(mLocalCursor.cursor, mLocalMediaInfo);
        }
        if (mMediaCursor != null){
            mMediaInfo = mAsyConfig.mMediaFactory.createMediaInfo();
            MediaTools.cursorToMediaBean(mMediaCursor.cursor, mMediaInfo);
        }

        while (true) {
            switch (checkCursor(mLocalMediaInfo, mMediaInfo)) {
                case TYPE_MOVE_ALL:
                    mediaCursorNext(mediaEngine);
                    localCursorNext(localEngine);
                    break;

                case TYPE_MEDIA_NEXT:
                case TYPE_LOCAL_NO_NEXT:
                    mediaCursorNext(mediaEngine);
                    break;

                case TYPE_LOCAL_NEXT:
                case TYPE_MEDIA_NO_NEXT:
                    localCursorNext(localEngine);
                    break;
            }
        }
    }

    private int checkCursor(MediaInfo localBean, MediaInfo mediaInfo) {
        int localMediaId;
        int mediaId;
        if (localBean == null && mediaInfo == null) {
            return TYPE_NO;
        } else if (localBean == null) {
            localMediaId = -1;
            mediaId = mediaInfo.getMediaId();
        } else if (mediaInfo == null) {
            mediaId = -1;
            localMediaId = localBean.getMediaId();
        } else {
            localMediaId = localBean.getMediaId();
            mediaId = mediaInfo.getMediaId();
        }

        if (localMediaId == mediaId) {
            //check update
            if (!checkUpdate(localBean, mediaInfo)) {
                postBundle(mediaInfo, DatabaseControlHandler.FLAG_UPDATE_DELAY);
            }
            return TYPE_MOVE_ALL;
        }
        if (mediaId == -1) {
            return TYPE_MEDIA_NO_NEXT;
        }

        if (localMediaId == -1) {
            postBundle(mediaInfo, DatabaseControlHandler.FLAG_INSERT_DELAY);
            return TYPE_LOCAL_NO_NEXT;
        }

        if (localMediaId < mediaId) {
            //delete form local
            postBundle(localBean, DatabaseControlHandler.FLAG_DELETE_DELAY);
            return TYPE_MEDIA_NEXT;
        }
        // insert media to local
        // local id = -1 or local id > media id

        postBundle(mediaInfo, DatabaseControlHandler.FLAG_INSERT_DELAY);
        return TYPE_LOCAL_NEXT;

    }

    private void postBundle(MediaInfo bean, int flag) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(DatabaseControlHandler.KEY_VALUE, bean);
        bundle.getInt(DatabaseControlHandler.KEY_FLAG, flag);
        mControlHandler.postMsg(bundle);
    }

    private void mediaCursorNext(MediaEngine mediaEngine) {
        mMediaCursor = mediaEngine.getMeidaCursor();
        if (mMediaCursor == null) {
            mMediaInfo = null;
            return;
        }
        MediaTools.cursorToMediaBean(mMediaCursor.cursor, mMediaInfo);
    }

    private void localCursorNext(LocalMediaEngine localMediaEngine) {
        mLocalCursor = localMediaEngine.getMeidaCursor();
        if (mLocalCursor == null) {
            mLocalMediaInfo = null;
            return;
        }
        LocalMediaTools.cursorToMediaBean(mLocalCursor.cursor, mLocalMediaInfo);
    }

    private boolean checkUpdate(MediaInfo localBean, MediaInfo mediaInfo) {
        return AsyConfig.getInstance().checkRule.checkUpdate(localBean, mediaInfo);
        //AsyMediaDatabase.getInstance(mContext).checkUpdate(localBean, mediaInfo);
    }
}
