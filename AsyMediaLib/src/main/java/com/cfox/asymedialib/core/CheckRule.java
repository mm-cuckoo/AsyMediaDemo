package com.cfox.asymedialib.core;

/**
 * 检查规则
 * @param <T>
 */
public interface CheckRule<T extends MediaInfo> {
    /**
     * 检查是否需要更新
     * @param localBean
     * @param mediaInfo
     * @return
     */
    public boolean checkUpdate(T localBean, MediaInfo mediaInfo);
}
