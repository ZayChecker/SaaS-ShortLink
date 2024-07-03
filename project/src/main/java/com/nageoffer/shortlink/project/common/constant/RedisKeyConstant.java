package com.nageoffer.shortlink.project.common.constant;

public class RedisKeyConstant {

    /**
     * 短链接跳转key
     */
    public static final String GOTO_SHORT_LINK_KEY = "short-link_goto_%s";

    public static final String LOCK_GOTO_SHORT_LINK_KEY = "short-link_lock_goto_%s";

    public static final String GOTO_IS_NULL_SHORT_LINK_KEY = "short-link_is-null_goto_%s";

    /**
     * 短链接修改 分组标识 锁前缀Key
     */
    public static final String LOCK_GID_UPDATE_KEY = "short-link_lock_update-gid_%s";

    /**
     * 短链接延迟队列消费统计Key
     */
    public static final String DELAY_QUEUE_STATS_KEY = "short-link_delay-queue:stats";
}
