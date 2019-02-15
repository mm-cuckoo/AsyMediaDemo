package com.cfox.asymedialib.core.db;

import android.content.Context;

import com.cfox.asymedialib.core.MediaInfo;

public abstract class AbsMDatabaseController<T extends MediaInfo> extends AbsDatabaseController<T> {

    public abstract T queryMeidaForId(Context context, long id);
}
