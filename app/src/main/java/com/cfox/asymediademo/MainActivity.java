package com.cfox.asymediademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cfox.asymediademo.db.LocalDatabaseControl;
import com.cfox.asymediademo.db.LocalMediaInfo;

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
        LocalDatabaseControl control = new LocalDatabaseControl();
        List<LocalMediaInfo> infos =  control.wrapperToBeans(control.baseQuery(this, null, null, null));
        for (LocalMediaInfo info : infos) {
            Log.e(TAG, "Local Info :" + info.toString());
        }

    }
}
