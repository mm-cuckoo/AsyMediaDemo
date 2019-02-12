package com.cfox.asymediademo.db;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper implements IMediaDbData {

    private static final String DB_NAME = "fve_db";

    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, getDatabaseVersion(context));
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTable(db);
        createView(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int fromVersion, int toVersion) {

    }


    private void createAllTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_FILES + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                FILE_PATH + " TEXT NOT NULL, " +
                MEDIA_ID + "  INTEGER UNIQUE NOT NULL," +
                TYPE + " INTEGER NOT NULL," +
                HIDDEN + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_VIDEO_PLAN + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                FILE_ID + " INTEGER NOT NULL," +
                START_TIME + " INTEGER NOT NULL," +
                STOP_TIME + " NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_IMAGE_PLAN + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                FILE_ID + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_VIDEO_GENERATE_PLAN + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                GENERATE_ID + " INTEGER NOT NULL," +
                PLAN_ID + " INTEGER NOT NULL, " +
                PRIORITY + " INTEGER NOT NULL," +
                TYPE + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_VIDEO_GENERATE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                NAME + " INTEGER NOT NULL, " +
                CRATE_TIME + " INTEGER NOT NULL)");
    }

    private void createView(SQLiteDatabase db) {
        db.execSQL("CREATE VIEW video_plan_view AS SELECT vp.start_time , vp.stop_time , f.file_path , " +
                "f.type FROM files f, video_plan vp WHERE f.id = vp.file_id");
    }

    public static int getDatabaseVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("couldn't get version code for " + context);
        }
    }
}
