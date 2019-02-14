package com.cfox.asymedialib.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.cfox.asymedialib.core.engine.CheckEngine;
import com.cfox.asymedialib.core.engine.LocalMediaEngine;
import com.cfox.asymedialib.core.engine.MediaEngine;
import com.cfox.asymedialib.core.engine.MediaProviderEngine;

public class DatabaseSyncHandler {
    private static final String TAG = "DatabaseSyncHandler";

    public static final String SERVICE_KEY      = "service_key";
    public static final String VALUE_KEY        = "value_key";

    public static final int TYPE_START_APP      = 10;
    public static final int TYPE_FLAG           = 11;
    public static final int TYPE_INSERT_IMAGE   = 1;
    public static final int TYPE_INSERT_VIDEO   = 2;

    public static final int FLAG_UPDATE_IMAGE   = 1 << 0;
    public static final int FLAG_UPDATE_VIDEO   = 1 << 1;
    public static final int FLAG_DELETE         = 1 << 2;


    private Context mContext;
    private Handler mHandler;
    private CheckEngine mCheckEngine;

    public DatabaseSyncHandler(Context context) {
        mContext = context.getApplicationContext();
        mCheckEngine = new CheckEngine(context);
        HandlerThread thread = new HandlerThread("Sync_db_handler");
        thread.start();
        mHandler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                dispatchMsg((Bundle) msg.obj);
            }
        };
    }

    public Handler getHandler () {
        return mHandler;
    }

    public void postMsg(Bundle bundle) {
        Message message = Message.obtain();
        message.obj = bundle;
        mHandler.sendMessage(message);

    }

    private void dispatchMsg(Bundle bundle) {
        int key = bundle.getInt(SERVICE_KEY, TYPE_START_APP);
        long value = bundle.getLong(VALUE_KEY, -1);
        switch (key) {
            case TYPE_START_APP:
                /*
                * init maxId and count to memory
                * */
                MaxIdCountChecker.check(mContext);
                checkAllMedia();
                break;

            case TYPE_FLAG:
                if ((value & FLAG_DELETE) == FLAG_DELETE && MaxIdCountChecker.check(mContext)) {
                    checkAllMedia();
                } else {
                    if ((value & FLAG_UPDATE_IMAGE) == FLAG_UPDATE_IMAGE) {
                        checkImageMedia();
                    }

                    if ((value & FLAG_UPDATE_VIDEO) == FLAG_UPDATE_VIDEO) {
                        checkVideoMedia();
                    }
                }

                break;

            case TYPE_INSERT_IMAGE:
                if (value == -1) return;
                mCheckEngine.insertNow(value);

                break;

            case TYPE_INSERT_VIDEO:
                if (value == -1) return;
                mCheckEngine.insertNow(value);
                break;
        }
    }

    private void checkAllMedia(){
        LocalMediaEngine localMediaEngine = LocalMediaEngine.build(mContext, MediaEngine.TYPE_IMAGE_VIDEO);
        MediaProviderEngine mediaProviderEngine = MediaProviderEngine.build(mContext, MediaEngine.TYPE_IMAGE_VIDEO);
        mCheckEngine.checkCenter(localMediaEngine, mediaProviderEngine);
    }

    private void checkImageMedia() {
        LocalMediaEngine localMediaEngine = LocalMediaEngine.build(mContext, MediaEngine.TYPE_IMAGE);
        MediaProviderEngine mediaProviderEngine = MediaProviderEngine.build(mContext, MediaEngine.TYPE_IMAGE);
        mCheckEngine.checkCenter(localMediaEngine, mediaProviderEngine);
    }


    private void checkVideoMedia() {
        LocalMediaEngine localMediaEngine = LocalMediaEngine.build(mContext, MediaEngine.TYPE_VIDEO);
        MediaProviderEngine mediaProviderEngine = MediaProviderEngine.build(mContext, MediaEngine.TYPE_VIDEO);
        mCheckEngine.checkCenter(localMediaEngine, mediaProviderEngine);
    }
}
