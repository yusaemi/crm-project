package com.sample.crm.util;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Strings. 2022/1/17 下午 11:53
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

    public static boolean equals(@Nullable String text1, @Nullable String text2) {
        if (text1 == null) return text2 == null;
        return text1.equals(text2);
    }

    public static boolean equalsAny(@Nullable String text1, @NonNull String... text2) {
        return Arrays.asList(text2).contains(text1);
    }

    public static boolean equalsAny(@Nullable String text1, @NonNull List<String> text2s) {
        return text2s.contains(text1);
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
     * 阿拉伯數字整數金額轉中文數字金額(省略單位，支援單位長度到億)
     *
     * @param amount amount
     * @return String
     */
    public static String arabicToKanji(BigDecimal amount) {

        char[] kanjiNums = "零壹貳參肆伍陸柒捌玖".toCharArray();
        String[] u1 = {"", "拾", "佰", "仟"};
        String[] u2 = {"", "萬", "億"};

        String amountStr = TypesUtil.parseStr(amount);

        if (isBlank(amountStr) || !amountStr.matches("^(0|[1-9]\\d{0,11})$")) {
            throw new IllegalArgumentException("參數錯誤。");
        }

        if (equals("0", amountStr)) {
            return "零";
        }

        StringBuilder result = new StringBuilder();
        // 從個位數開始轉換
        for (int i = amountStr.length() - 1, j = 0; i >= 0; i--, j++) {
            char n = amountStr.charAt(i);
            if (n == '0') {
                // 當n為0且0的右邊一位不是0時，append零
                if (i < amountStr.length() - 1 && amountStr.charAt(i + 1) != '0') {
                    result.append(kanjiNums[0]);
                }
                // append萬或億
                if (j % 4 == 0
                        && (i > 0 && amountStr.charAt(i - 1) != '0'
                        || i > 1 && amountStr.charAt(i - 2) != '0'
                        || i > 2 && amountStr.charAt(i - 3) != '0')) {
                    result.append(u2[j / 4]);
                }
            } else {
                if (j % 4 == 0) {
                    result.append(u2[j / 4]); // append萬或億
                }
                result.append(u1[j % 4]); // append拾、佰或仟
                result.append(kanjiNums[n - '0']); // append數字
            }
        }

        return result.reverse().toString();
    }

}
