package com.example.saul.utils;


import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class StaUtil {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmptyByString(String str) {
        return str == null || str.trim().length() <= 0;
    }

    /**
     * 判断字符串是否为空 为空则返回一个空字符串
     */
    public static String reEmptyString(String str) {
        if (isEmptyByString(str)) return "";
        return str;
    }

    /**
     * 判断对象是否为空
     */
    public static boolean isEmptyByObject(Object obj) {
        if (obj == null) return true;
        else if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        else if (obj instanceof Collection) return ((Collection) obj).isEmpty();
        else if (obj instanceof Map) return ((Map) obj).isEmpty();
        else if (obj.getClass().isArray()) return Array.getLength(obj) == 0;

        return false;
    }

    /**
     * 获取32位GUID
     *
     * @return
     */
    public static String getId() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            UUID uuid = UUID.randomUUID();
            String guidStr = uuid.toString();
            md.update(guidStr.getBytes(), 0, guidStr.length());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 处理字符串分割符 并且封装为list的资产id
     * 只支持英文的逗号分隔符
     */
    public static List changeList(String AssetListString) {
        AssetListString = AssetListString.endsWith(",") ? AssetListString.substring(0, AssetListString.length() - 1) : AssetListString;
        AssetListString = AssetListString.startsWith(",") ? AssetListString.substring(1, AssetListString.length()) : AssetListString;
        String[] ss = AssetListString.split(",");

        return Arrays.stream(ss).collect(Collectors.toList());
    }
}
