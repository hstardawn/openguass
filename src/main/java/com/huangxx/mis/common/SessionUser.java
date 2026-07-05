package com.huangxx.mis.common;

public record SessionUser(
        String userId,
        String loginName,
        Role role,
        String refId,
        String displayName
) {
}
