package com.cfox.asymedialib.core.db;

import android.content.Context;

import com.cfox.asymedialib.core.MediaInfo;

public abstract class AbsMDatabaseControl<T extends MediaInfo> extends AbsDatabaseControl<T> {

    public abstract T queryMeidaForId(Context context, long id);
}
