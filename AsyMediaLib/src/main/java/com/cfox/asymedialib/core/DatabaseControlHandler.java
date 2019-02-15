package com.cfox.asymedialib.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.cfox.asymedialib.AsyConfig;

import java.util.ArrayList;
import java.util.List;

public class DatabaseControlHandler {
    private static final String TAG = "DatabaseControlHandler";

    public static final String KEY_FLAG           = "key_flag";


    public static final String KEY_VALUE          = "key_value";
    public static final int FLAG_FLUSH_DELAY      = 0;
    public static final int FLAG_INSERT_NOW       = 1;
    public static final int FLAG_INSERT_DELAY     = 2;
    public static final int FLAG_UPDATE_DELAY     = 3;
    public static final int FLAG_DELETE_DELAY     = 4;

    private Context mContext;
    private Handler mHandler;

    private int mInsertStackSize;
    private int mUpdateStackSize;
    private int mDeleteStackSize;

    private List<MediaInfo> mInserts = new ArrayList<>();
    private List<MediaInfo> mUpdates = new ArrayList<>();
    private List<MediaInfo> mDeletes = new ArrayList<>();

    public Handler getHandler () {
        return mHandler;
    }

    public DatabaseControlHandler(Context context) {
        mContext = context.getApplicationContext();
        initCacheSize();
        HandlerThread thread = new HandlerThread("db_control_handler");
        thread.start();
        mHandler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                dispatchMsg((Bundle) msg.obj);
            }
        };
    }

    private void initCacheSize() {
        mInsertStackSize = AsyConfig.getInstance().mCacheSizeInsert;
        mUpdateStackSize = AsyConfig.getInstance().mCacheSizeUpdate;
        mDeleteStackSize = AsyConfig.getInstance().mCacheSizeDelete;
    }

    public void postMsg(Bundle bundle) {
        Message message = Message.obtain();
        message.obj = bundle;
        mHandler.sendMessage(message);

    }


    private void dispatchMsg(Bundle obj) {

        int flag = obj.getInt(KEY_FLAG, -1);
        if(AsyConfig.Debug) {
            Log.d(TAG, "get flag:" + flag);
        }
        MediaInfo info = null;
        if (flag > 0) {
            info = obj.getParcelable(KEY_VALUE);
            if(AsyConfig.Debug) {
                Log.d(TAG, "get info:" + info);
            }
        }

        switch (flag) {
            case FLAG_INSERT_NOW:
                insertNow(info);
                break;

            case FLAG_INSERT_DELAY:
                mInserts.add(info);
                flushInsertDelay(false);
                break;

            case FLAG_UPDATE_DELAY:
                mUpdates.add(info);
                flushUpdateDelay(false);
                break;

            case FLAG_DELETE_DELAY:
                mDeletes.add(info);
                flushDeleteDelay(false);
                break;

            case FLAG_FLUSH_DELAY:
                if(AsyConfig.Debug) {
                    Log.d(TAG, ">>>>>>: flush delay data ");
                }

                flushInsertDelay(true);
                flushUpdateDelay(true);
                flushDeleteDelay(true);
                break;
        }

    }

    private void flushDeleteDelay(boolean isFlush) {
        if(AsyConfig.Debug) {
            Log.d(TAG, "delete size:" + mDeletes.size());
        }

        if (AsyConfig.DEBUG_INFO) {
            Log.d(TAG, "delete info:" + mDeletes.toString());
        }

        if (mDeletes.size() > 0 && (isFlush || mDeletes.size() >= mDeleteStackSize)) {
            AsyConfig.getInstance().mUDatabaseControl.deleteForList(mContext, mDeletes);
            mDeletes.clear();
        }
    }

    private void flushUpdateDelay(boolean isFlush) {
        if(AsyConfig.Debug) {
            Log.d(TAG, "update size:" + mUpdates.size());
        }

        if (AsyConfig.DEBUG_INFO) {
            Log.d(TAG, "update info:" + mUpdates.toString());
        }

        if (mUpdates.size() > 0 && (isFlush || mUpdates.size() >= mUpdateStackSize)) {
            AsyConfig.getInstance().mUDatabaseControl.updateForList(mContext, mUpdates);
            mUpdates.clear();
        }
    }

    private void flushInsertDelay(boolean isFlush) {
        if(AsyConfig.Debug) {
            Log.d(TAG, "insert size:" + mInserts.size());
        }

        if (AsyConfig.DEBUG_INFO) {
            Log.d(TAG, "insert info:" + mInserts.toString());
        }
        if (mInserts.size() > 0 && (isFlush || mInserts.size() >= mInsertStackSize)) {
            AsyConfig.getInstance().mUDatabaseControl.insertForList(mContext, mInserts);
            mInserts.clear();
        }
    }

    private void insertNow(MediaInfo bean) {
        if (bean == null) return;
        AsyConfig.getInstance().mUDatabaseControl.insert(mContext, bean);
    }
}
