package com.cfox.asymedialib.core;

public interface CheckRule<T extends MediaInfo> {
    boolean checkUpdate(T localBean, MediaInfo mediaInfo);
}
