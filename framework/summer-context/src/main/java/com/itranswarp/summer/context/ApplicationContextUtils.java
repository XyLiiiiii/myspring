package com.itranswarp.summer.context;

import java.util.Objects;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
//ApplicationContextUtils 的工具类，用于管理和访问 ApplicationContext 实例。
public class ApplicationContextUtils {

    private static ApplicationContext applicationContext = null;

    @Nonnull
    public static ApplicationContext getRequiredApplicationContext() {
        return Objects.requireNonNull(getApplicationContext(), "ApplicationContext is not set.");
    }

    @Nullable
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    static void setApplicationContext(ApplicationContext ctx) {
        applicationContext = ctx;
    }
}
