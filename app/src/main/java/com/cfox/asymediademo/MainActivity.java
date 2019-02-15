package com.cfox.asymediademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cfox.asymedialib.core.CursorWrapper;
import com.cfox.asymedialib.core.MediaInfo;
import com.cfox.asymedialib.core.db.MediaDatabaseController;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void crateObj(View view) {
        ((AppApplication)getApplication()).asy();
    }

    public void showLocals(View view) {
        CursorWrapper wrapper;
//        LocalDatabaseController control = new LocalDatabaseController();
//        wrapper = control.baseQuery(this, null, null, null);

        MediaDatabaseController control = new MediaDatabaseController();
//        wrapper = control.queryImageAndVideo(this, 0, 1000);
//        wrapper = control.queryImageAndVideo(this, 31535, 0,31535, 0,0, 1000);
//        wrapper = control.queryImage(this ,0, 1000);
//        wrapper = control.queryImage(this, 31535, 0,0, 1000);
//        wrapper = control.queryVideo(this, 0, 1000);
        wrapper = control.queryVideo(this, 31535, 0,0, 1000);

        List<MediaInfo> infos =  control.wrapperToBeans(wrapper);
        for (MediaInfo info : infos) {
            Log.e(TAG, "media Info :" + info.toString());
        }

    }
}
