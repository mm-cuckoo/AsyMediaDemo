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

    }

    private void createView(SQLiteDatabase db) {
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
