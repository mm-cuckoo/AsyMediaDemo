package com.cfox.asymediademo.db.core;

public abstract class MediaFactory<T extends MediaInfo> {

    public CheckEngine checkEngine;

    public abstract T createLocalMediaInfo();

    MediaInfo createMediaInfo() {
        return new MediaInfo();
    }
}
