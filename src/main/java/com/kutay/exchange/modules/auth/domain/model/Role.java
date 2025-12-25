package com.kutay.exchange.modules.auth.domain.model;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static String getRoleHierarchy() {
        return ROLE_ADMIN.name() + " > " + ROLE_USER.name();
    }
}
