package com.cfox.asymediademo.db.core;

import android.content.Context;

import java.util.List;

public abstract class AbsUDatabaseControl<T extends MediaInfo> extends AbsDatabaseControl<T> {
    public abstract void insertForList(Context context , List<T> medias);
    public abstract void updateForList(Context context , List<T> medias);
    public abstract void deleteForList(Context context , List<T> medias);
    public abstract void insertOrUpdate(Context context , T t);

}
