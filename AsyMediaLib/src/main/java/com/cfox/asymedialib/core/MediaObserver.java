package com.cfox.asymedialib.core;

import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.cfox.asymedialib.AsyConfig;


public class MediaObserver extends ContentObserver {
    private static final String TAG = "MediaObserver";
    private static final String IMAGE = "content://media/external/images/media";
    private static final String VIDEO = "content://media/external/video/media";

    private static final int TYPE_INSERT_IMAGE  = 1;
    private static final int TYPE_INSERT_VIDEO  = 2;
    private static final int TYPE_UPDATE_IMAGE  = 3;
    private static final int TYPE_UPDATE_VIDEO  = 4;
    private static final int TYPE_DELETE        = 5;



    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private int mFlag = 0;

    static {
        sUriMatcher.addURI("media", "external/images/media/#", TYPE_INSERT_IMAGE);//insert
        sUriMatcher.addURI("media", "external/video/media/#", TYPE_INSERT_VIDEO);// insert
        sUriMatcher.addURI("media", "external/images/media", TYPE_UPDATE_IMAGE);// update
        sUriMatcher.addURI("media", "external/video/media", TYPE_UPDATE_VIDEO);// update
        sUriMatcher.addURI("media", "external", TYPE_DELETE);// delete
    }

    private DatabaseSyncHandler mBDHandler;
    public MediaObserver( DatabaseSyncHandler handler) {
        super(null);
        mBDHandler = handler;
    }


    public static void register(Context context, MediaObserver mediaObserver) {
        context.getContentResolver().registerContentObserver(Uri.parse(IMAGE), true,
                mediaObserver);
        context.getContentResolver().registerContentObserver(Uri.parse(VIDEO), true,
                mediaObserver);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if(AsyConfig.Debug) {
            Log.d(TAG, "onChange: media provider uri:" + uri.toString());
        }
        int newPriority = getUriPriority(uri);
        if (newPriority == TYPE_INSERT_IMAGE || newPriority == TYPE_INSERT_VIDEO) {
            dispatchUri(uri);
            return;
        }

        startDelayUri ();

        if (newPriority == TYPE_DELETE) {
            mFlag = mFlag | DatabaseSyncHandler.FLAG_DELETE;
            return;
        }

        if (newPriority == TYPE_UPDATE_IMAGE) {
            mFlag = mFlag | DatabaseSyncHandler.FLAG_UPDATE_IMAGE;
            return;
        }

        if (newPriority == TYPE_UPDATE_VIDEO) {
            mFlag = mFlag | DatabaseSyncHandler.FLAG_UPDATE_VIDEO;
        }
    }

    private int getUriPriority(Uri uri) {
        if (uri == null ) return 0;
        return sUriMatcher.match(uri);
    }

    private synchronized void startDelayUri () {
        mBDHandler.getHandler().removeCallbacks(postRun);
        mBDHandler.getHandler().postDelayed(postRun, 1000);
    }

    private Runnable postRun = new Runnable() {
        @Override
        public void run() {
            if(AsyConfig.Debug) {
                Log.d(TAG, "run: delay running .....");
            }
            int tmpFlag = mFlag;
            mFlag = 0;
            postRun(DatabaseSyncHandler.TYPE_FLAG, tmpFlag);
        }
    };

    private void dispatchUri(Uri uri) {
        if (uri.toString().startsWith(IMAGE)) {
            insertImage(uri);
            return;
        }

        if (uri.toString().startsWith(VIDEO)) {
            insertVideo(uri);
        }
    }

    private void insertVideo(Uri uri) {
        long id = ContentUris.parseId(uri);
        postRun(DatabaseSyncHandler.TYPE_INSERT_VIDEO, id);
    }

    private void insertImage(Uri uri) {
        long id = ContentUris.parseId(uri);
        postRun(DatabaseSyncHandler.TYPE_INSERT_IMAGE, id);
    }

    private void postRun(int type, long value) {
        Bundle bundle = new Bundle();
        bundle.putInt(DatabaseSyncHandler.SERVICE_KEY, type);
        bundle.putLong(DatabaseSyncHandler.VALUE_KEY, value);
        mBDHandler.postMsg(bundle);
    }
}
