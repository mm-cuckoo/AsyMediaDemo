package com.cfox.asymedialib.core.db;

import android.content.Context;

import com.cfox.asymedialib.core.MediaInfo;

import java.util.List;

public abstract class AbsUDatabaseControl<T extends MediaInfo> extends AbsDatabaseControl<T> {
    public abstract void insertForList(Context context , List<MediaInfo> medias);
    public abstract void updateForList(Context context , List<MediaInfo> medias);
    public abstract void deleteForList(Context context , List<MediaInfo> medias);
    public abstract void insertOrUpdate(Context context , MediaInfo t);

}
