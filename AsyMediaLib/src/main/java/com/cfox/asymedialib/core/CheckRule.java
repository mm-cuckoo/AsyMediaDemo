package com.cfox.asymedialib.core;

public interface CheckRule<T extends MediaInfo> {
    public boolean checkUpdate(T localBean, MediaInfo mediaInfo);
}
