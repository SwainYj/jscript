package swain.utils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StringUtils {
    public static final String DEFAULT_LANGUAGE = "zh_CN";

    private static final Long LONG = 4294967295L;

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return 空：true、非空：false
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }

        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isValue(String str) {
        return !isEmpty(str);
    }

    public static String hasValue(String value) {
        return isEmpty(value) ? "" : value;
    }

    public static String avoidNull(String value) {
        if (null != value) {
            return value;
        }

        return "";
    }

    public static boolean isNumber(String str) {
        if (null == str) {
            return false;
        }

        return str.matches("\\d+");
    }

    public static boolean isEmptyWihthoutTrim(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isUintNumber(String str) {
        try {
            long uInt = Long.parseLong(str);
            if (LONG < uInt) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 截取字符串
     * @param errCode
     * @param length
     * @param isRight
     * @return
     */
    public static String truncateString(String errCode, int length, boolean isRight) {
        if (!isEmpty(errCode)) {
            if (length >= errCode.length()) {
                return errCode;
            } else {
                String subString = "";
                if (isRight) {
                    subString = errCode.substring(0, length);
                    return subString;
                } else {
                    subString = errCode.substring(errCode.length() - length, errCode.length());
                    return subString;
                }
            }
        }
        return errCode;
    }

    public static String formatNumber(String formatStr, int length, boolean leftPadding,
                                      char paddingCharacter) {
        StringBuilder result = new StringBuilder(formatStr);
        String paddingStr = String.valueOf(paddingCharacter);

        while (result.length() < length) {
            if (leftPadding) {
                result = (new StringBuilder(paddingStr)).append(result);
            } else {
                result.append(paddingCharacter);
            }
        }

        return result.toString();
    }

    public static Map<String, String> parseString(String content, String level1SplitPatten,
                                                  String level2SplitPatten) {
        Map<String, String> result = new HashMap<String, String>();
        if (isEmpty(content)) {
            return result;
        }

        String[] tempArray = content.split(level1SplitPatten);
        String[] tempArray4Item;
        for (String item : tempArray) {
            tempArray4Item = item.trim().split(level2SplitPatten);
            if (tempArray4Item.length > 1) {
                result.put(tempArray4Item[0].trim(), tempArray4Item[1].trim());
            }
        }

        return result;
    }

    public static String escapeXMLSymbols(String xmlContent) {
        if (isEmpty(xmlContent)) {
            return xmlContent;
        }

        xmlContent = xmlContent.replaceAll("&", "&amp;");
        xmlContent = xmlContent.replaceAll("<", "&lt;");
        xmlContent = xmlContent.replaceAll(">", "&gt;");
        xmlContent = xmlContent.replaceAll("\"", "&quot;");
        xmlContent = xmlContent.replaceAll("'", "&apos;");

        return xmlContent;
    }

    public static String unescapeXML(String xmlContent) {
        if (!isEmpty(xmlContent)) {
            xmlContent = xmlContent.replaceAll("&amp;", "&")
                    .replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">")
                    .replaceAll("&quot;", "\"")
                    .replaceAll("&apos;", "'");
        }

        return xmlContent;
    }

    public static String upperCaseFirstLetter(String srcStr) {
        if (isEmpty(srcStr)) {
            return srcStr;
        }

        return Character.toUpperCase(srcStr.charAt(0)) + srcStr.substring(1);
    }

    public static String lowerCaseFirstLetter(String srcStr) {
        if (isEmpty(srcStr)) {
            return srcStr;
        }

        return Character.toLowerCase(srcStr.charAt(0)) + srcStr.substring(1);
    }

    /**
     * 匿名化处理
     *
     * @param str
     * @return
     */
    public static String anonymous(String str) {
        if (str.length() <= 6) {
            str = "******";
        } else {
            int firstLength = (str.length() - 6) / 2 + (str.length() - 6) % 2;
            if (firstLength < 4) {
                str = str.substring(0, firstLength) + "******" + str.substring(firstLength + 6, str.length());
            } else {
                str = str.substring(0, 4) + "******" + str.substring(str.length() - 4, str.length());
            }
        }
        return str;
    }

    public static String anonymousName(String str) {
        if (isEmpty(str)) {
            return "";
        }

        if (str.length() == 1) {
            str = "*";
        } else if (str.length() > 6) {
            str = str.substring(0, 1) + "******";
        } else {
            // findbugs SBSC_USE_STRINGBUFFER_CONCATENATION
            StringBuffer star = new StringBuffer(1024);
            //            String star = "";
            for (int i = 0; i < str.length() - 1; i++) {
                star.append("*");
                //                star += "*";
            }
            str = str.substring(0, 1) + star.toString();
            //            str = str.substring(0, 1) + star;
        }
        return str;
    }

    /**
     * 归一化语言
     *
     * @param rawLanguage 未归一化语言
     * @return 归一化后的语言，如果格式不满足要求，默认返回'zh_CN'
     */
    public static String normalizeLanguage(String rawLanguage) {
        return normalizeLanguage(rawLanguage, DEFAULT_LANGUAGE);
    }

    /**
     * 归一化语言
     *
     * @param rawLanguage 语言入参，例如：
     *                    <ul>
     *                    <li>zh-cn
     *                    <li>ZH-CN
     *                    <li>zh-CN
     *                    <li>zh_cn
     *                    <li>ZH_CN
     *                    <li>zh_CN
     *                    <li>zh
     *                    <li>ZH
     *                    <li>空字符串
     *                    <li>其他不符合以上写法的字符串
     *                    </ul>
     * @return {小写双拉丁字母}_{大写双拉丁字母}：
     * <ul>
     * <li>zh-cn->zh_CN
     * <li>ZH-CN->zh_CN
     * <li>zh-CN->zh_CN
     * <li>zh_cn->zh_CN
     * <li>ZH_CN->zh_CN
     * <li>zh_CN->zh_CN
     * <li>zh->zh_CN
     * <li>ZH->zh_CN
     * <li>空字符串->zh_CN（默认值）
     * <li>其他不符合以上写法的字符串->zh_CN（默认值）
     * </ul>
     */
    public static String normalizeLanguage(String rawLanguage, String defaultLanguage) {
        if (isEmpty(rawLanguage)) {
            if (!isEmpty(defaultLanguage)) {
                return defaultLanguage;
            }

            return DEFAULT_LANGUAGE;
        }

        rawLanguage = rawLanguage.trim();

        if (rawLanguage.length() == 2) {
            if (rawLanguage.equalsIgnoreCase("zh")) {
                return "zh_CN";
            }

            if (rawLanguage.equalsIgnoreCase("en")) {
                return "en_US";
            }

            if (!isEmpty(defaultLanguage)) {
                return defaultLanguage;
            }

            return DEFAULT_LANGUAGE;
        }

        if (!rawLanguage.matches("[a-z|A-Z]{2}[_|-]{1}[a-z|A-Z]{2}")) {
            if (!isEmpty(defaultLanguage)) {
                return defaultLanguage;
            }

            return DEFAULT_LANGUAGE;
        }

        String leftPart = rawLanguage.substring(0, 2);

        if (leftPart.equalsIgnoreCase("zh")) {
            return "zh_CN";
        }

        if (leftPart.equalsIgnoreCase("en")) {
            return "en_US";
        }

        if (!isEmpty(defaultLanguage)) {
            return defaultLanguage;
        }

        return DEFAULT_LANGUAGE;
    }

    /**
     * 校验签名是否正确
     *
     * @param sign
     * @return
     */
    public static boolean isValidSign(String sign) {
        // UUID校验
        if (isEmpty(sign)) {
            return false;
        }
        return strIsCharOrNum(sign);
    }

    /**
     * 校验字符串是否为字母和数字组成
     *
     * @param str
     * @return
     */
    private static boolean strIsCharOrNum(String str) {
        if (isEmpty(str)) {
            return false;
        }

        String regex = "[0-9A-Za-z]+";
        if (str.matches(regex)) {
            return true;
        }
        return false;
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }

    public static int getRandom(int min, int max) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(max - min) + min;
    }

    public static String getId() {
        SecureRandom r = new SecureRandom();
        return String.valueOf(System.currentTimeMillis() * 1000 + r.nextInt(1000));
    }
}
