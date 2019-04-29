package com.cmy.knowapi.util;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * MD5加密
 */
public abstract class Md5Util {
    public static String toMd5(String oldPassword, String salt) {
        return String.valueOf(new SimpleHash("md5", oldPassword, salt, 2));
    }
}
