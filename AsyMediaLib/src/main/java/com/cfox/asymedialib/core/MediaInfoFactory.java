package com.cfox.asymedialib.core;

public abstract class MediaInfoFactory<T extends MediaInfo> {
    public abstract T createMediaInfo();
}

