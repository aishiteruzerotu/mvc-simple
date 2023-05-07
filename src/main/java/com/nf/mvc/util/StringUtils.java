package com.nf.mvc.util;

import java.util.List;
import java.util.StringJoiner;

/**
 * 此类拷贝自spring的StringUtils类
 */
public abstract class StringUtils {
    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 把List字符串数据变成一个分隔符分隔的字符串，比如List集合中有a b c三条记录，<br/>
     * 而指定的分隔符是逗号，那么调用此方法之后生成的字符串是：a,b,c
     * @param stringList
     * @param delimiter
     * @return
     */
    public static String toCommaDelimitedString(List<String> stringList,String delimiter) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (String val : stringList) {
            if (val != null) {
                joiner.add(val);
            }
        }
        return joiner.toString();
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasText( String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIndex = 0;
        int endIndex = str.length() - 1;

        while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
            beginIndex++;
        }

        while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
            endIndex--;
        }

        return str.substring(beginIndex, endIndex + 1);
    }

    /**
     * 裁剪掉所有的空白字符，包含前部，中间，尾部的空白
     * @param str
     * @return
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
