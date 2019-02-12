package com.cfox.asymediademo.db.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class DatabaseControlHandler {

    private static final int INSERT_STACK_SIZE = 100;

    private static final int UPDATE_STACK_SIZE = 100;
    private static final int DELETE_STACK_SIZE = 100;


    public static final String KEY_FLAG           = "key_flag";
    public static final String KEY_VALUE          = "key_value";
    public static final int FLAG_INSERT_NOW       = 1;
    public static final int FLAG_INSERT_DELAY     = 2;
    public static final int FLAG_UPDATE_DELAY     = 3;
    public static final int FLAG_DELETE_DELAY     = 4;
    public static final int FLAG_FLUSH_DELAY      = 5;

    private Context mContext;
    private Handler mHandler;

    private List<MediaInfo> inserts = new ArrayList<>(INSERT_STACK_SIZE);
    private List<MediaInfo> updates = new ArrayList<>(UPDATE_STACK_SIZE);
    private List<MediaInfo> deletes = new ArrayList<>(DELETE_STACK_SIZE);

    public Handler getHandler () {
        return mHandler;
    }

    public DatabaseControlHandler(Context context) {
        mContext = context.getApplicationContext();
        HandlerThread thread = new HandlerThread("db_control_handler");
        thread.start();
        mHandler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                dispatchMsg((Bundle) msg.obj);
            }
        };
    }

    public void postMsg(Bundle bundle) {
        Message message = Message.obtain();
        message.obj = bundle;
        mHandler.sendMessage(message);

    }


    private void dispatchMsg(Bundle obj) {

        int flag = obj.getInt(KEY_FLAG, -1);
        MediaInfo bean = null;
        if (flag > 0) {
            bean = (MediaInfo) obj.get(KEY_VALUE);
        }

        switch (flag) {
            case FLAG_INSERT_NOW:
                insertNow(bean);
                break;

            case FLAG_INSERT_DELAY:
                inserts.add(bean);
                flushInsertDelay(false);
                break;

            case FLAG_UPDATE_DELAY:
                updates.add(bean);
                flushUpdateDelay(false);
                break;

            case FLAG_DELETE_DELAY:
                deletes.add(bean);
                flushDeleteDelay(false);
                break;

            case FLAG_FLUSH_DELAY:
                flushInsertDelay(true);
                flushUpdateDelay(true);
                flushDeleteDelay(true);
                break;
        }

    }

    private void flushDeleteDelay(boolean isFlush) {
        if (isFlush || deletes.size() >= DELETE_STACK_SIZE) {
            AsyConfig.getInstance().mUDatabaseControl.deleteForList(mContext, deletes);
            deletes.clear();
        }
    }

    private void flushUpdateDelay(boolean isFlush) {
        if (isFlush || updates.size() >= UPDATE_STACK_SIZE) {
            AsyConfig.getInstance().mUDatabaseControl.updateForList(mContext, updates);
            updates.clear();
        }
    }

    private void flushInsertDelay(boolean isFlush) {
        if (isFlush || inserts.size() >= INSERT_STACK_SIZE) {
            AsyConfig.getInstance().mUDatabaseControl.insertForList(mContext, inserts);
            inserts.clear();
        }
    }

    private void insertNow(MediaInfo bean) {
        if (bean == null) return;
        AsyConfig.getInstance().mUDatabaseControl.insertOrUpdate(mContext, bean);
    }
}
