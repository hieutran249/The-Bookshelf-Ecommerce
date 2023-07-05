package com.hieutt.ecommerceweb.utils;

public class Constants {
    public static final String[] WHITELIST_ENDPOINTS = {
            "/register/**",
            "/forgot-password/**",
            "/css/**",
            "/js/**",
            "/img/**",
            "/vendor/**",
            "/scss/**"
    };
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
}
