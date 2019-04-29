package com.cmy.knowapi.util;

import java.util.UUID;

/**
 * Shiro生成随机盐工具
 */
public abstract class ShiroSaltUtil {
    public static String createSale() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
