package com.cfox.asymedialib.core.db;

import android.content.Context;

import com.cfox.asymedialib.core.CursorWrapper;
import com.cfox.asymedialib.core.MediaInfo;

import java.util.List;

public abstract class AbsUDatabaseController<T extends MediaInfo> extends AbsDatabaseController<T> {
    public abstract void insertForList(Context context , List<MediaInfo> medias);
    public abstract void updateForList(Context context , List<MediaInfo> medias);
    public abstract void deleteForList(Context context , List<MediaInfo> medias);
    public abstract void insert(Context context , MediaInfo t);

    @Override
    public CursorWrapper queryImageAndVideo(Context context, int imageMinSize, int imageMaxSize, int videoMinSize, int videMaxSzie, int startMediaId, int rowNum) {
        return queryImageAndVideo(context, startMediaId, rowNum);
    }

    @Override
    public CursorWrapper queryImage(Context context, int minSuze, int maxSize, int startMediaId, int rowNum) {
        return queryImage(context, startMediaId, rowNum);
    }

    @Override
    public CursorWrapper queryVideo(Context context, int minSuze, int maxSize, int startMediaId, int rowNum) {
        return queryVideo(context, startMediaId, rowNum);
    }
}
