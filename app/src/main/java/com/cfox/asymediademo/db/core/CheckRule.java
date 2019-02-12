package com.cfox.asymediademo.db.core;


public interface CheckRule<T extends MediaInfo> {
    public boolean checkUpdate(T localBean, MediaInfo mediaInfo);
}
