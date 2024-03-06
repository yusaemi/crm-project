package com.sample.crm.util;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * StringUtil. 2022/1/17 下午 11:53
 *
 * @author Sero
 * @version 1.0.0
 **/
public final class StringUtil {

    private StringUtil() {}

    public static final String EMPTY = "";

    public static byte[] bytes(String text) {
        return text.getBytes(UTF_8);
    }

    public static String format(String pattern, Object... params) {
        return MessageFormatter.arrayFormat(pattern, params).getMessage();
    }

    public static int compare(@Nullable String text1, @Nullable String text2) {
        if (text1 == null && text2 == null) return 0;
        if (text1 != null && text2 == null) return 1;
        if (text1 == null) return -1;
        return text1.compareTo(text2);
    }

    public static boolean isBlank(@Nullable String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isNotBlank(@Nullable String text) {
        return !isBlank(text);
    }

    public static String defaultBlank(@Nullable String text, @Nullable String defaultStr) {
        return isBlank(text) ? defaultStr : text;
    }

    public static boolean isAnyBlank(String... text) {
        return Arrays.stream(text).anyMatch(StringUtil::isBlank);
    }

    public static boolean isAnyNotBlank(String... text) {
        return Arrays.stream(text).anyMatch(StringUtil::isNotBlank);
    }

    public static boolean equals(@Nullable String text1, @Nullable String text2) {
        if (text1 == null) return text2 == null;
        return text1.equals(text2);
    }

    public static boolean equalsIgnoreCase(@Nullable String text1, @Nullable String text2) {
        if (text1 == null) return text2 == null;
        return text1.equalsIgnoreCase(text2);
    }

    public static boolean equalsAnyIgnoreCase(@Nullable String text1, @NonNull String... text2) {
        return Arrays.stream(text2).anyMatch(text -> text.equalsIgnoreCase(text1));
    }

    public static boolean equalsAny(@Nullable String text1, @NonNull String... text2) {
        return Arrays.asList(text2).contains(text1);
    }

    public static boolean equalsAny(@Nullable String text1, @NonNull List<String> text2s) {
        return text2s.contains(text1);
    }

    public static <T> T equalsTernary(String str1, String str2, T flagTrue, T flagFalse) {
        return equals(str1, str2) ? flagTrue : flagFalse;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte hashByte : bytes) {
            int intVal = 0xff & hashByte;
            if (intVal < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(intVal));
        }
        return sb.toString();
    }

    /**
     * 對Log Forging攻擊做字元轉換
     *
     * @param value 字串
     * @return 過濾會引發log forging的特殊字元
     */
    public static String cleanLogForging(String value) {
        if (isBlank(value)) return value;
        return value.replace("\r", "").replace("\n", "").replace("%0d", "").replace("%0a", "");
    }

    /**
     * 字串格式判斷
     *
     * @param str   str
     * @param regex regex
     * @return boolean
     */
    public static boolean matches(String str, String regex) {
        if (isBlank(str)) return false;
        return str.matches(regex);
    }

    /**
     * 將字串轉成List
     *
     * @param data data
     * @param regex regex
     * @return List<String>
     */
    public static List<String> transToList(String data, String regex) {
        List<String> list = ListUtil.newArrayList();

        if (isBlank(data)) {
            return list;
        }

        String[] temp = data.split(regex);
        return Arrays.stream(temp).map(String::trim).toList();
    }

    /**
     * 擷取長度
     *
     * @param str    str
     * @param length length
     * @return String
     */
    public static String subStr(String str, int length) {
        if (isBlank(str) || str.length() <= length) return str;
        return str.substring(0, length);
    }

    /**
     * 指定位置插入指定字串
     *
     * @param str      str
     * @param position position
     * @return String
     */
    public static String insert(String str, int position, String insertStr) {
        if (isBlank(str)) return str;
        StringBuilder sb = new StringBuilder(str);
        return sb.insert(position, insertStr).toString();
    }

    /**
     * 取得隨機長度亂數大寫英數字串
     *
     * @param length length
     * @return String
     */
    public static String getRandomStr(int length) {
        SecureRandom sr = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        while (sb.length() != length) {
            int r = sr.nextInt(91);
            if (r >= 58 && r <= 64 || r < 50 || r == 73 || r == 79) {
                continue;
            }
            sb.append((char) r);
        }
        return sb.toString();
    }

    /**
     * 取得隨機長度亂數數字串
     *
     * @param length length
     * @return String
     */
    public static String getRandomNumStr(int length) {
        SecureRandom sr = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        while (sb.length() != length) {
            int r = sr.nextInt(10);
            sb.append(r);
        }
        return sb.toString();
    }

    /**
     * 取得指定字串中間的文本
     *
     * @param str 處理文本
     * @param open 擷取起頭(不含，且第一位符合的位置)
     * @param close 擷取結尾(不含，且第一位符合的位置)
     * @return 擷取文本
     */
    public static String getTextBetween2String(String str, String open, String close) {
        if (isAnyBlank(str, open, close)) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != -1) {
            final int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    public static String snakeToUpperCamel(String snake) {
        List<String> parts = transToList(snake, "_");
        return parts.stream()
                .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    public static String snakeToLowerCamel(String snake) {
        List<String> parts = transToList(snake, "_");
        return IntStream.range(0, parts.size())
                .mapToObj(index -> {
                    String part = parts.get(index);
                    return index == 0 ? part.toLowerCase() : part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
                })
                .collect(Collectors.joining());
    }

    public static String camelToUpperSnake(String camel) {
        if (isBlank(camel)) {
            return camel;
        }
        StringBuilder snakeCase = new StringBuilder();
        snakeCase.append(Character.toUpperCase(camel.charAt(0)));
        for (int i = 1; i < camel.length(); i++) {
            char ch = camel.charAt(i);
            if (Character.isUpperCase(ch)) {
                snakeCase.append('_');
                snakeCase.append(ch);
            } else {
                snakeCase.append(Character.toUpperCase(ch));
            }
        }
        return snakeCase.toString();
    }

    public static String camelToLowerSnake(String camel) {
        if (isBlank(camel)) {
            return camel;
        }
        StringBuilder snakeCase = new StringBuilder();
        snakeCase.append(Character.toLowerCase(camel.charAt(0)));
        for (int i = 1; i < camel.length(); i++) {
            char ch = camel.charAt(i);
            if (Character.isUpperCase(ch)) {
                snakeCase.append('_');
                snakeCase.append(Character.toLowerCase(ch));
            } else {
                snakeCase.append(ch);
            }
        }
        return snakeCase.toString();
    }

    public static <T> T getGuardedAccess(char[] chars, Function<char[], T> function) {
        T result = function.apply(chars);
        Arrays.fill(chars, '\u0000');
        return result;
    }

    public static void guardedAccess(char[] chars, Consumer<char[]> consumer) {
        consumer.accept(chars);
        Arrays.fill(chars, '\u0000');
    }

}
