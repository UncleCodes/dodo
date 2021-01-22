package com.dodo.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.dodo.common.annotation.field.FileStyle;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.doc2swf.office.DodoOfficeConverter;
import com.dodo.common.enums.EnumInterface;
import com.dodo.common.framework.bean.file.DodoFile;
import com.dodo.common.framework.bean.file.DodoVideoFile;
import com.dodo.common.framework.bean.location.DodoLocationInfo;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.common.framework.listener.DodoWebAppRootListener;
import com.dodo.common.video.VideoConvertor;
import com.dodo.privilege.enums.ExtendModelFieldType;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;
import com.dodo.utils.file.FileUtils;
import com.dodo.utils.http.HttpUtils;
import com.dodo.utils.web.WebUtil;

/**
 * 通用的工具类
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class CommonUtil {
    public static final String       EXTEND_MODEL_CHECK_STATUS      = "status";
    public static final String       EXTEND_MODEL_CHECK_STATUS_FAIL = "fail";
    public static final String       EXTEND_MODEL_CHECK_MSG         = "msg";
    private static final Logger      LOGGER                         = LoggerFactory.getLogger(CommonUtil.class);
    public static final Pattern      PATTERN_MOBILE                 = Pattern.compile("^1\\d{10}$");
    public static final Pattern      PATTERN_EMAIL                  = Pattern
                                                                            .compile("^([\\w-]+\\.)*[\\w-]+@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$");
    public static final Pattern      PATTERN_DIGITS                 = Pattern.compile("^-?\\d+$");
    public static final Pattern      PATTERN_FLOAT                  = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    public static final Pattern      PATTERN_CREDITCARD             = Pattern.compile("^(4\\d{12}(?:\\d{3})?)$");
    public static final Pattern      PATTERN_IP                     = Pattern
                                                                            .compile("\\b((\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\b|\\.)){4}");
    public static final String[]     DATEFORMATS                    = { "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyyMMdd",
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "yyyyMMdd HH:mm:ss", "yyyyMMddHH:mm:ss", "HH:mm:ss", "yyyy.MM.dd" };
    public static final List<String> BOOLEANSELECT                  = new ArrayList<String>(2) {
                                                                        private static final long serialVersionUID = -4783338460236494533L;
                                                                        {
                                                                            add("TRUE|是");
                                                                            add("FALSE|否");
                                                                        }
                                                                    };

    public static String getWebRootPath() {
        return System.getProperty(DodoWebAppRootListener.WEB_APP_ROOT_KEY);
    }

    public static String getWebappName() {
        return System.getProperty(DodoWebAppRootListener.WEB_APP_NAME_KEY);
    }

    public static String escapeHtml(String string) {
        if (StringUtils.isBlank(string)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = string.length(); i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String unEscapeHtml(String string) {
        if (StringUtils.isBlank(string)) {
            return "";
        }
        return string.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"").replaceAll("&#39;", "'");
    }

    public static <T> T parseSpecialDate(String formatStr, String[] parsePattern, Class<T> clazz) {
        if (StringUtils.isBlank(formatStr)) {
            return null;
        }
        try {
            return clazz.getConstructor(long.class).newInstance(
                    DateUtils.parseDate(formatStr,
                            (parsePattern == null || parsePattern.length == 0) ? DATEFORMATS : parsePattern).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseSpecialDate(String formatStr, String parsePattern, Class<T> clazz) {
        if (StringUtils.isBlank(formatStr) || StringUtils.isBlank(parsePattern)
                || formatStr.length() != parsePattern.length()) {
            return null;
        }
        try {
            return clazz.getConstructor(long.class).newInstance(
                    DateUtils.parseDate(formatStr, new String[] { parsePattern }).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseSpecialDate(String formatStr, Class<T> clazz) {
        if (StringUtils.isBlank(formatStr)) {
            return null;
        }
        try {
            return clazz.getConstructor(long.class).newInstance(DateUtils.parseDate(formatStr, DATEFORMATS).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSpecialDateStr(java.util.Date date, String parsePattern) {
        if (date == null) {
            return "";
        }
        try {
            return new SimpleDateFormat(parsePattern).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ParseDateError";
    }

    public static String getOnlineTimeStr(long onLineTimeMillis) {
        StringBuilder sbBuffer = new StringBuilder("00:00:00:000");
        sbBuffer.replace(9, 12, lpad(Long.valueOf(onLineTimeMillis % 1000).toString(), 3, "0"));
        long onLineSeconds = onLineTimeMillis / 1000;
        sbBuffer.replace(6, 8, lpad(Long.valueOf(onLineSeconds % 60).toString(), 2, "0"));
        onLineSeconds = onLineSeconds / 60;
        sbBuffer.replace(3, 5, lpad(Long.valueOf(onLineSeconds % 60).toString(), 2, "0"));
        onLineSeconds = onLineSeconds / 60;
        sbBuffer.replace(0, 2, lpad(Long.valueOf(onLineSeconds).toString(), 2, "0"));
        return sbBuffer.toString();
    }

    public static String lpad(String res, int length, String relpace) {
        StringBuffer sb = new StringBuffer();
        int need = length - res.length();
        for (int i = 0; i < need; i++) {
            sb.append(relpace);
        }
        sb.append(res);
        return sb.toString().trim();
    }

    public static String rpad(String res, int length, String relpace) {
        StringBuffer sb = new StringBuffer();
        sb.append(res);
        int need = length - res.length();
        for (int i = 0; i < need; i++) {
            sb.append(relpace);
        }
        return sb.toString().trim();
    }

    public static boolean isIdCardOk(String src) {
        if (StringUtils.isBlank(src)) {
            return false;
        }
        String temp = src.trim();
        if (temp.length() == 15) {
            return IdcardValidator.isValidate15Idcard(temp);
        } else if (temp.length() == 18) {
            return IdcardValidator.isValidate18Idcard(temp);
        } else {
            return false;
        }
    }

    public static String buildHmacSignature(String pKey, String pStringToSign) {
        String lSignature = "None";
        try {
            Mac lMac = Mac.getInstance("HmacMD5");
            SecretKeySpec lSecret = new SecretKeySpec(pKey.getBytes(), "HmacMD5");
            lMac.init(lSecret);

            byte[] lDigest = lMac.doFinal(pStringToSign.getBytes());
            BigInteger lHash = new BigInteger(1, lDigest);
            lSignature = lHash.toString(16);
            if ((lSignature.length() % 2) != 0) {
                lSignature = "0" + lSignature;
            }
        } catch (NoSuchAlgorithmException lEx) {
            throw new RuntimeException("Problems calculating HMAC", lEx);
        } catch (InvalidKeyException lEx) {
            throw new RuntimeException("Problems calculating HMAC", lEx);
        }

        return lSignature;
    }

    public static boolean isLuhn(String strNum) {
        int oddSum = 0;
        int evenSum = 0;
        boolean isOdd = true;
        for (int i = strNum.length() - 1; i >= 0; i--) {
            char cNum = strNum.charAt(i);
            int num = Integer.parseInt(cNum + "");
            if (isOdd) {
                oddSum += num;
            } else {
                num = num * 2;
                if (num > 9) {
                    num = num % 10 + 1;
                }
                evenSum = evenSum + num;
            }
            isOdd = !isOdd;
        }
        return ((evenSum + oddSum) % 10 == 0);
    }

    public static boolean isMobile(String mobiles) {
        if (StringUtils.isBlank(mobiles)) {
            return false;
        }
        return PATTERN_MOBILE.matcher(mobiles).matches();
    }

    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return PATTERN_EMAIL.matcher(email).matches();
    }

    public static boolean isUsername(String s) {
        return StringUtils.defaultIfEmpty(s, "").matches("[a-zA-Z_0-9]+");
    }

    public static boolean isUrl(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return s.matches("[a-zA-z]+://[^\\s]*");

    }

    public static boolean isNumber(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return PATTERN_FLOAT.matcher(s).matches() || PATTERN_DIGITS.matcher(s).matches();
    }

    public static boolean isNumber(Object s) {
        if (s == null) {
            return false;
        }
        String ss = String.valueOf(s);
        if (StringUtils.isBlank(ss)) {
            return false;
        }
        return PATTERN_FLOAT.matcher(ss).matches() || PATTERN_DIGITS.matcher(ss).matches();
    }

    public static boolean isDigits(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return PATTERN_DIGITS.matcher(s).matches();
    }

    public static boolean isDigits(Object s) {
        if (s == null) {
            return false;
        }
        String ss = String.valueOf(s);

        if (StringUtils.isBlank(ss)) {
            return false;
        }
        return PATTERN_DIGITS.matcher(ss).matches();
    }

    public static boolean isCreditcard(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }

        return PATTERN_CREDITCARD.matcher(s).matches() && isLuhn(s);
    }

    public static boolean isIp(String s) {
        if (StringUtils.isBlank(s)) {
            return false;
        }
        return PATTERN_IP.matcher(s).matches();
    }

    public static boolean isMatchRegExp(String s, String regExp) {
        return s.matches(regExp);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> coverStringToObjects(String s, String aplitChar, Class<T> clazz) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        s = s.replaceAll("，", ",").replaceAll(" ", "");

        boolean isEnumInterface = isEnumInterface(clazz);

        String[] arr1 = s.split(aplitChar);
        try {
            Set<T> hasSet = new HashSet<T>();
            for (int i = 0; i < arr1.length; i++) {
                arr1[i] = arr1[i].trim();
                if (StringUtils.isBlank(arr1[i])) {
                    continue;
                }

                if (isEnumInterface) {
                    hasSet.add((T) getEnumInterfaceValue((Class<? extends EnumInterface>) clazz, arr1[i]));
                } else if (!clazz.isEnum()) {
                    try {
                        hasSet.add(clazz.getConstructor(String.class).newInstance(arr1[i]));
                    } catch (Exception e) {
                    }
                }
            }
            return hasSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getObjectDeleteLog(Object oldObj) {
        HttpServletRequest request = WebUtil.getRequest();
        try {
            StringBuffer sbBuffer = new StringBuffer("");
            Field[] fields = oldObj.getClass().getDeclaredFields();
            Field[] fieldsSuper = oldObj.getClass().getSuperclass().getDeclaredFields();
            List<Field> fieldsList = new LinkedList<Field>();
            for (Field f : fieldsSuper) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                fieldsList.add(f);
            }
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                fieldsList.add(f);
            }
            for (Field f : fieldsList) {
                try {
                    f.setAccessible(true);
                    Class<?> fieldType = f.getType();
                    Object oldValue = f.get(oldObj);
                    if (isEnumInterface(fieldType)) {
                        EnumInterface oldValueEnum = (EnumInterface) oldValue;
                        oldValue = oldValue == null ? ""
                                : (StringUtils.isBlank(oldValueEnum.getNameKey()) ? oldValueEnum.getName() : SpringUtil
                                        .getMessageBack(oldValueEnum.getNameKey(), request));
                    } else if (isDate(fieldType)) {
                        oldValue = oldValue == null ? "" : getSpecialDateStr((Date) oldValue, "yyyy-MM-dd HH:mm:ss");
                    } else if (isBaseEntity(fieldType)) {
                        sbBuffer.append(oldValue == null ? "" : ((BaseEntity) oldValue).getId());
                    }
                    // collection file doc or others
                    else {
                        oldValue = oldValue == null ? "" : oldValue.toString();
                    }
                    sbBuffer.append(f.getName());
                    sbBuffer.append(":");
                    sbBuffer.append(oldValue);
                    sbBuffer.append(";<br/>");
                } catch (Exception e) {
                    LOGGER.error("Save Delete Log :{}", e.getMessage());
                }
            }
            if (sbBuffer.length() == 0) {
                try {
                    sbBuffer.append(oldObj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sbBuffer.append(";");
            }
            return sbBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getObjectAddLog(Object oldObj) {
        HttpServletRequest request = WebUtil.getRequest();
        try {
            StringBuffer sbBuffer = new StringBuffer("");
            Field[] fields = oldObj.getClass().getDeclaredFields();
            Field[] fieldsSuper = oldObj.getClass().getSuperclass().getDeclaredFields();
            List<Field> fieldsList = new LinkedList<Field>();
            for (Field f : fieldsSuper) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                fieldsList.add(f);
            }
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                fieldsList.add(f);
            }
            for (Field f : fieldsList) {
                try {
                    Class<?> fieldType = f.getType();
                    f.setAccessible(true);
                    Object vaObject = f.get(oldObj);
                    sbBuffer.append(f.getName());
                    sbBuffer.append(":");
                    if (isEnumInterface(fieldType)) {
                        EnumInterface enumvaObject = (EnumInterface) vaObject;
                        sbBuffer.append(vaObject == null ? ""
                                : (StringUtils.isBlank(enumvaObject.getNameKey()) ? enumvaObject.getName() : SpringUtil
                                        .getMessageBack(enumvaObject.getNameKey(), request)));
                    } else if (isDate(fieldType)) {
                        sbBuffer.append(vaObject == null ? "" : getSpecialDateStr((Date) vaObject,
                                "yyyy-MM-dd HH:mm:ss"));
                    } else if (isBaseEntity(fieldType)) {
                        sbBuffer.append(vaObject == null ? "" : ((BaseEntity) vaObject).getId());
                    } else {
                        sbBuffer.append(vaObject == null ? "" : vaObject.toString());
                    }
                    sbBuffer.append(";<br/>");
                } catch (Exception e) {
                    LOGGER.error("Save Insert Log :{}", e.getMessage());
                }
            }
            if (sbBuffer.length() == 0) {
                try {
                    sbBuffer.append(oldObj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sbBuffer.append(";");
            }

            return sbBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getObjectUpdateLog(Object oldObj, Object newObj) {
        HttpServletRequest request = WebUtil.getRequest();
        try {
            StringBuffer sbBuffer = new StringBuffer("");
            Field[] fields = oldObj.getClass().getDeclaredFields();
            Field[] fieldsSuper = oldObj.getClass().getSuperclass().getDeclaredFields();
            List<Field> fieldsList = new LinkedList<Field>();
            for (Field f : fieldsSuper) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                fieldsList.add(f);
            }
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                fieldsList.add(f);
            }
            for (Field f : fieldsList) {
                try {
                    f.setAccessible(true);
                    Class<?> fieldType = f.getType();
                    Object oldValue = f.get(oldObj);
                    Object newValue = f.get(newObj);
                    if ((oldValue != null && !oldValue.equals(newValue))
                            || (newValue != null && !newValue.equals(oldValue))) {
                        if (isEnumInterface(fieldType)) {
                            EnumInterface oldValueEnum = (EnumInterface) oldValue;
                            EnumInterface newValueEnum = (EnumInterface) newValue;

                            oldValue = oldValue == null ? ""
                                    : (StringUtils.isBlank(oldValueEnum.getNameKey()) ? oldValueEnum.getName()
                                            : SpringUtil.getMessageBack(oldValueEnum.getNameKey(), request));
                            newValue = newValue == null ? ""
                                    : (StringUtils.isBlank(newValueEnum.getNameKey()) ? newValueEnum.getName()
                                            : SpringUtil.getMessageBack(newValueEnum.getNameKey(), request));
                        } else if (isDate(fieldType)) {
                            oldValue = oldValue == null ? ""
                                    : getSpecialDateStr((Date) oldValue, "yyyy-MM-dd HH:mm:ss");
                            newValue = newValue == null ? ""
                                    : getSpecialDateStr((Date) newValue, "yyyy-MM-dd HH:mm:ss");
                        } else if (isBaseEntity(fieldType)) {
                            oldValue = oldValue == null ? "" : ((BaseEntity) oldValue).getId();
                            newValue = newValue == null ? "" : ((BaseEntity) newValue).getId();
                        }
                        // others
                        else {
                            oldValue = oldValue == null ? "" : oldValue.toString();
                            newValue = newValue == null ? "" : newValue.toString();
                        }
                        if (!oldValue.equals(newValue)) {
                            sbBuffer.append(f.getName());
                            sbBuffer.append(":");
                            sbBuffer.append(oldValue);
                            sbBuffer.append("->");
                            sbBuffer.append(newValue);
                            sbBuffer.append(";<br/>");
                        }
                    }
                } catch (Exception e) {
                    sbBuffer.append(f.getName());
                    sbBuffer.append(";");
                    LOGGER.error("Save Update Log :{}", e.getMessage());
                }
            }
            return sbBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isCollectionType(Class<?> clazz) {
        return isTargetClass(clazz, Collection.class);
    }

    public static boolean isListOrSet(Class<?> clazz) {
        return isTargetClass(clazz, List.class) || isTargetClass(clazz, Set.class);
    }

    public static boolean isList(Class<?> clazz) {
        return isTargetClass(clazz, List.class);
    }

    public static boolean isSet(Class<?> clazz) {
        return isTargetClass(clazz, Set.class);
    }

    public static boolean isMap(Class<?> clazz) {
        return isTargetClass(clazz, Map.class);
    }

    public static boolean isTargetClass(Class<?> clazz, Class<?> targetClazz) {
        if (clazz == null || targetClazz == null) {
            return false;
        }
        return targetClazz.isAssignableFrom(clazz);
    }

    public static boolean isDate(Class<?> clazz) {
        return java.util.Date.class.equals(clazz) || java.util.Date.class.equals(clazz.getSuperclass());
    }

    public static boolean isEnumInterface(Class<?> clazz) {
        if (clazz == null || !clazz.isEnum()) {
            return false;
        }
        return isTargetClass(clazz, EnumInterface.class);
    }

    public static List<String> getEnumValues(Class<?> clazz, HttpServletRequest request) {
        List<String> list = null;
        if (isEnumInterface(clazz)) {
            EnumInterface[] objs = (EnumInterface[]) clazz.getEnumConstants();
            list = new ArrayList<String>(objs.length);
            for (EnumInterface obj : objs) {
                list.add(obj.name()
                        + "|"
                        + (StringUtils.isBlank(obj.getNameKey()) ? obj.getName() : SpringUtil.getMessageBack(
                                obj.getNameKey(), request)));
            }
        }
        return list;
    }

    public static boolean isBaseEntity(Class<?> clazz) {
        return isTargetClass(clazz, BaseEntity.class);
    }

    public static String getDownloadFilename(HttpServletRequest request, String fileName) {
        try {
            if (HttpUtils.getBrowser(request).contains("MSIE")) {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static boolean isEnumStringA(EnumInterface e, String s) {
        if (e.name().equals(s) || s.equals(e.getName()) || s.equals(e.getNameKey())
                || (s.matches("\\d+") && e.ordinal() == Integer.parseInt(s))) {
            return true;
        }
        return false;
    }

    public static <T extends EnumInterface> T getEnumInterfaceAccurateValue(Class<T> clazz, String s) {
        if (s.matches("\\d+")) {
            return EnumInterface.valueOf(clazz, Integer.valueOf(s));
        } else {
            return EnumInterface.valueOf(clazz, s);
        }
    }

    public static EnumInterface getEnumInterfaceValue(Class<? extends EnumInterface> clazz, String s) {
        if (s.matches("\\d+")) {
            return EnumInterface.valueOf(clazz, Integer.valueOf(s));
        } else {
            return EnumInterface.valueOf(clazz, s);
        }
    }

    public static DodoLocationInfo getLocationFromRequest(HttpServletRequest request, String propertyName,
            String extAttrJson) {
        List<?> extAttr = null;
        try {
            extAttr = JacksonUtil.toObject(extAttrJson, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> attr = null;
        if (extAttr != null) {
            attr = new HashMap<String, String>(extAttr.size());
            for (Object key : extAttr) {
                attr.put((String) key, CommonUtil.escapeHtml(request.getParameter(propertyName + "-location-" + key))
                        .trim());
            }
        }
        String latitude = request.getParameter(propertyName + "Latitude");
        String longitude = request.getParameter(propertyName + "Longitude");
        if (StringUtils.isNotBlank(longitude) || StringUtils.isNotBlank(latitude) || (attr != null && attr.size() > 0)) {
            DodoLocationInfo locationInfo = new DodoLocationInfo();
            locationInfo.setLatitude(latitude);
            locationInfo.setLongitude(longitude);
            if (attr != null) {
                locationInfo.setAttr(attr);
            }
            return locationInfo;
        }
        return null;
    }

    public static Map<String, String> getExtendInfo(String extendProperty, Records extendConfiguration,
            HttpServletRequest request, Map<String, String> extendInfos) throws IOException {
        return getExtendInfo(extendProperty, extendConfiguration.getRawData(), null, request, extendInfos);
    }

    public static Map<String, String> getExtendInfo(String extendProperty,
            List<Map<String, Object>> extendConfiguration, HttpServletRequest request, Map<String, String> extendInfos)
            throws IOException {
        return getExtendInfo(extendProperty, extendConfiguration, null, request, extendInfos);
    }

    public static Map<String, String> getExtendInfo(String extendProperty, Records extendConfiguration,
            Map<String, String> extendInfosSrc, HttpServletRequest request, Map<String, String> extendInfos)
            throws IOException {
        return getExtendInfo(extendProperty, extendConfiguration.getRawData(), extendInfosSrc, request, extendInfos);
    }

    public static Map<String, String> getExtendInfo(String extendProperty,
            List<Map<String, Object>> extendConfiguration, Map<String, String> extendInfosSrc,
            HttpServletRequest request, Map<String, String> extendInfos) throws IOException {
        Map<String, String> resultMap = new HashMap<String, String>(4);
        resultMap.put(EXTEND_MODEL_CHECK_STATUS, EXTEND_MODEL_CHECK_STATUS_FAIL);
        String extFieldName = null;
        ExtendModelFieldType fieldType = null;
        String extShowName = null;
        String extShowNameKey = null;
        Boolean nullable = null;
        Integer minLength = null;
        Integer maxLength = null;
        Boolean isEmail = null;
        Boolean isMobile = null;
        Boolean isUrl = null;
        Boolean isCreditcard = null;
        Boolean isIp = null;
        Integer minValue = null;
        Integer maxValue = null;
        String fileExts = null;
        String valueList = null;
        String currRequestFieldName = null;
        String currRequestFieldShowName = null;
        List<DodoFile> fileList = null;
        List<DodoVideoFile> videoFileList = null;
        String uploadFileReturn = null;
        DodoLocationInfo locationInfo = null;
        String[] fieldValues = null;
        List<String> valueListCheck = null;
        StringBuilder sbBuilder = new StringBuilder();
        String fieldValue = null;
        FileStyle fileStyle = null;
        String ossBucket = null;
        for (Map<String, Object> extendFieldConfig : extendConfiguration) {
            extFieldName = (String) extendFieldConfig.get("extFieldName");
            // 当前操作是添加 但是配置字段不允许添加
            if (extendInfosSrc == null && (!(Boolean) extendFieldConfig.get("addable"))) {
                continue;
            }
            // 当前操作是修改 但是配置字段不允许修改
            if (extendInfosSrc != null && (!(Boolean) extendFieldConfig.get("editable"))) {
                extendInfos.put(extFieldName, extendInfosSrc.get(extFieldName));
                continue;
            }

            fieldType = (ExtendModelFieldType) extendFieldConfig.get("fieldType");
            extShowName = (String) extendFieldConfig.get("extShowName");
            extShowNameKey = (String) extendFieldConfig.get("extShowNameKey");
            nullable = (Boolean) extendFieldConfig.get("nullable");
            minLength = (Integer) extendFieldConfig.get("minLength");
            maxLength = (Integer) extendFieldConfig.get("maxLength");
            isEmail = (Boolean) extendFieldConfig.get("isEmail");
            isMobile = (Boolean) extendFieldConfig.get("isMobile");
            isUrl = (Boolean) extendFieldConfig.get("isUrl");
            isCreditcard = (Boolean) extendFieldConfig.get("isCreditcard");
            isIp = (Boolean) extendFieldConfig.get("isIp");
            minValue = (Integer) extendFieldConfig.get("minValue");
            maxValue = (Integer) extendFieldConfig.get("maxValue");
            fileExts = (String) extendFieldConfig.get("fileExts");
            valueList = (String) extendFieldConfig.get("valueList");
            fileStyle = (FileStyle) extendFieldConfig.get("fileStyle");
            ossBucket = (String) extendFieldConfig.get("ossBucket");
            currRequestFieldName = extendProperty + "_" + extFieldName;
            currRequestFieldShowName = StringUtils.isNotBlank(extShowNameKey) ? SpringUtil.getMessageBack(
                    extShowNameKey, new Object[0], request) : extShowName;
            if (fieldType == ExtendModelFieldType.SINGLEFILE || fieldType == ExtendModelFieldType.MULTIFILE
                    || fieldType == ExtendModelFieldType.DOC) {
                fileList = new LinkedList<DodoFile>();
                if (fieldType == ExtendModelFieldType.SINGLEFILE && fileStyle == FileStyle.OnlyPath
                        && StringUtils.isNotBlank(ossBucket)) {
                    uploadFileReturn = FileUtils
                            .getUploadFiles("uploader-" + currRequestFieldName, fileList, request, (StringUtils
                                    .isBlank(fileExts) && fieldType == ExtendModelFieldType.DOC) ? DodoOfficeConverter
                                    .getAllSupportSb().toString().toLowerCase() : fileExts, null, ossBucket);
                } else {
                    uploadFileReturn = FileUtils
                            .getUploadFiles("uploader-" + currRequestFieldName, fileList, request, (StringUtils
                                    .isBlank(fileExts) && fieldType == ExtendModelFieldType.DOC) ? DodoOfficeConverter
                                    .getAllSupportSb().toString().toLowerCase() : fileExts, null, "");
                }

                if (!FileUtils.FILE_PROCESS_SUCCESS.equals(uploadFileReturn)) {
                    resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.file.ext.error",
                            new Object[] { uploadFileReturn }, request));
                    return resultMap;
                }
                if (fieldType != ExtendModelFieldType.MULTIFILE && fileList.size() > 1) {
                    resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.single.file",
                            new Object[] { currRequestFieldShowName }, request));
                    return resultMap;
                }
                // 文件字段 添加时校验非空
                if (extendInfosSrc == null && (!nullable) && fileList.size() == 0) {
                    resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.notnull",
                            new Object[] { currRequestFieldShowName }, request));
                    return resultMap;
                }
                if (fileList.size() > 0) {
                    if (fieldType == ExtendModelFieldType.SINGLEFILE && fileStyle == FileStyle.OnlyPath) {
                        extendInfos.put(extFieldName, fileList.get(0).getHttpPath());
                    } else {
                        extendInfos.put(extFieldName, JacksonUtil.toJackson(fileList));
                    }
                } else if (extendInfosSrc != null) {
                    extendInfos.put(extFieldName, extendInfosSrc.get(extFieldName));
                }
            } else if (fieldType == ExtendModelFieldType.VIDEO) {
                videoFileList = new LinkedList<DodoVideoFile>();
                uploadFileReturn = FileUtils.getUploadVideos("uploader-" + currRequestFieldName, videoFileList,
                        request, StringUtils.isBlank(fileExts) ? VideoConvertor.getAllSupport().toLowerCase()
                                : fileExts, null);
                if (!FileUtils.FILE_PROCESS_SUCCESS.equals(uploadFileReturn)) {
                    resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.file.ext.error",
                            new Object[] { uploadFileReturn }, request));
                    return resultMap;
                }
                if (videoFileList.size() > 1) {
                    resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.single.file",
                            new Object[] { currRequestFieldShowName }, request));
                    return resultMap;
                }
                // 文件字段 添加时校验非空
                if (extendInfosSrc == null && (!nullable) && videoFileList.size() == 0) {
                    resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.notnull",
                            new Object[] { currRequestFieldShowName }, request));
                    return resultMap;
                }
                if (videoFileList.size() > 0) {
                    extendInfos.put(extFieldName, JacksonUtil.toJackson(videoFileList));
                } else if (extendInfosSrc != null) {
                    extendInfos.put(extFieldName, extendInfosSrc.get(extFieldName));
                }
            } else if (fieldType == ExtendModelFieldType.LOCATION) {
                locationInfo = getLocationFromRequest(request, currRequestFieldName, null);
                if (locationInfo == null || (!locationInfo.isLegal())) {
                    if (!nullable) {
                        resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.common.location.illegal",
                                new Object[] { currRequestFieldShowName }, request));
                        return resultMap;
                    }
                } else {
                    extendInfos.put(extFieldName, JacksonUtil.toJackson(locationInfo));
                }
            } else if (fieldType == ExtendModelFieldType.CHECKBOX) {
                fieldValues = request.getParameterValues(currRequestFieldName);
                if (fieldValues == null || fieldValues.length == 0 || StringUtils.isBlank(valueList)) {
                    if (!nullable) {
                        resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.notnull",
                                new Object[] { currRequestFieldShowName }, request));
                        return resultMap;
                    }
                } else {
                    valueListCheck = Arrays.asList(valueList.split(","));
                    sbBuilder.delete(0, sbBuilder.length());
                    for (String val : fieldValues) {
                        if (valueListCheck.contains(val)) {
                            sbBuilder.append(val).append(",");
                        }
                    }
                    if (sbBuilder.length() > 0) {
                        sbBuilder.deleteCharAt(sbBuilder.length() - 1);
                        extendInfos.put(extFieldName, escapeHtml(sbBuilder.toString()));
                    } else if (!nullable) {
                        resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.notnull",
                                new Object[] { currRequestFieldShowName }, request));
                        return resultMap;
                    }
                }
            } else {
                fieldValue = request.getParameter(currRequestFieldName);
                if (StringUtils.isBlank(fieldValue)) {
                    if (!nullable) {
                        resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.notnull",
                                new Object[] { currRequestFieldShowName }, request));
                        return resultMap;
                    }
                } else {
                    if (fieldType == ExtendModelFieldType.RICHTEXT) {
                        fieldValue = StringUtils.replace(fieldValue, DodoCommonConfigUtil.fileServer,
                                DodoCommonConfigUtil.fileServerSubstitute);
                    } else {
                        fieldValue = escapeHtml(fieldValue);
                    }
                    if (fieldType == ExtendModelFieldType.STRING) {
                        if (minLength != null && fieldValue.length() < minLength) {
                            resultMap.put(
                                    EXTEND_MODEL_CHECK_MSG,
                                    SpringUtil.getMessageBack("dodo.infotip.minlength", new Object[] {
                                            currRequestFieldShowName, minLength }, request));
                            return resultMap;
                        }
                        if (maxLength != null && fieldValue.length() > maxLength) {
                            resultMap.put(
                                    EXTEND_MODEL_CHECK_MSG,
                                    SpringUtil.getMessageBack("dodo.infotip.maxlength", new Object[] {
                                            currRequestFieldShowName, maxLength }, request));
                            return resultMap;
                        }
                        if (isEmail && (!isEmail(fieldValue))) {
                            resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.isemail",
                                    new Object[] { currRequestFieldShowName }, request));
                            return resultMap;
                        }
                        if (isMobile && (!isMobile(fieldValue))) {
                            resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.ismobile",
                                    new Object[] { currRequestFieldShowName }, request));
                            return resultMap;
                        }
                        if (isUrl && (!isUrl(fieldValue))) {
                            resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.isurl",
                                    new Object[] { currRequestFieldShowName }, request));
                            return resultMap;
                        }
                        if (isCreditcard && (!isCreditcard(fieldValue))) {
                            resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack(
                                    "dodo.infotip.iscreditcard", new Object[] { currRequestFieldShowName }, request));
                            return resultMap;
                        }
                        if (isIp && (!isIp(fieldValue))) {
                            resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.isip",
                                    new Object[] { currRequestFieldShowName }, request));
                            return resultMap;
                        }
                    } else if (fieldType == ExtendModelFieldType.NUMBER || fieldType == ExtendModelFieldType.DIGITS) {
                        if (fieldType == ExtendModelFieldType.NUMBER && (!isNumber(fieldValue))) {
                            resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.isnumber",
                                    new Object[] { currRequestFieldShowName }, request));
                            return resultMap;
                        }
                        if (fieldType == ExtendModelFieldType.DIGITS && (!isDigits(fieldValue))) {
                            resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.isdigits",
                                    new Object[] { currRequestFieldShowName }, request));
                            return resultMap;
                        }
                        if (minValue != null && Double.parseDouble(fieldValue) < minValue) {
                            resultMap.put(
                                    EXTEND_MODEL_CHECK_MSG,
                                    SpringUtil.getMessageBack("dodo.infotip.min", new Object[] {
                                            currRequestFieldShowName, minValue }, request));
                            return resultMap;
                        }
                        if (maxValue != null && Double.parseDouble(fieldValue) > maxValue) {
                            resultMap.put(
                                    EXTEND_MODEL_CHECK_MSG,
                                    SpringUtil.getMessageBack("dodo.infotip.max", new Object[] {
                                            currRequestFieldShowName, maxValue }, request));
                            return resultMap;
                        }
                    } else if (fieldType == ExtendModelFieldType.TEXTAREA) {
                        if (minLength != null && fieldValue.length() < minLength) {
                            resultMap.put(
                                    EXTEND_MODEL_CHECK_MSG,
                                    SpringUtil.getMessageBack("dodo.infotip.minlength", new Object[] {
                                            currRequestFieldShowName, minLength }, request));
                            return resultMap;
                        }
                        if (maxLength != null && fieldValue.length() > maxLength) {
                            resultMap.put(
                                    EXTEND_MODEL_CHECK_MSG,
                                    SpringUtil.getMessageBack("dodo.infotip.maxlength", new Object[] {
                                            currRequestFieldShowName, maxLength }, request));
                            return resultMap;
                        }
                    } else if (fieldType == ExtendModelFieldType.SELECT || fieldType == ExtendModelFieldType.RADIO) {
                        if (StringUtils.isBlank(valueList)
                                || (!Arrays.asList(valueList.split(",")).contains(fieldValue))) {
                            if (!nullable) {
                                resultMap.put(EXTEND_MODEL_CHECK_MSG, SpringUtil.getMessageBack("dodo.infotip.notnull",
                                        new Object[] { currRequestFieldShowName }, request));
                                return resultMap;
                            }
                            fieldValue = null;
                        }
                    }
                    if (fieldValue != null) {
                        extendInfos.put(extFieldName, fieldValue);
                    }
                }
            }
        }
        resultMap.remove(EXTEND_MODEL_CHECK_STATUS);
        return resultMap;
    }

    @SuppressWarnings("unchecked")
    public static void initQueryConditionForHelper(HqlHelper helper, HttpServletRequest request, Model model,
            String propertyName, Class<?> fieldType, String identityClass, String dateFormat) {
        String queryType = null;
        String[] queryValues = null;
        String queryValue1 = null;
        String queryValue2 = null;
        String queryTypeKey = "_field_query_type_" + propertyName;
        String queryValueKey = "_field_query_value_" + propertyName;
        queryType = request.getParameter(queryTypeKey);
        if (StringUtils.isBlank(queryType)) {
            return;
        }

        if ("isNotNull".equals(queryType)) {
            if (fieldType.equals(String.class)) {
                helper.and().isNotNull(propertyName).or().eq(propertyName, "").end().end();
            } else {
                helper.isNotNull(propertyName);
            }
            if (model != null) {
                model.addAttribute(queryTypeKey, queryType);
            }
        } else if ("isNull".equals(queryType)) {
            if (fieldType.equals(String.class)) {
                helper.and().isNull(propertyName).or().eq(propertyName, "").end().end();
            } else {
                helper.isNull(propertyName);
            }
            if (model != null) {
                model.addAttribute(queryTypeKey, queryType);
            }
        } else if (isBaseEntity(fieldType)) {
            if ("eq".equals(queryType) || "ne".equals(queryType)) {
                queryValues = request.getParameterValues(queryValueKey);
                if (queryValues == null || queryValues.length == 0) {
                    queryValues = request.getParameterValues(queryValueKey + "[]");
                }
                if (queryValues != null && queryValues.length > 0) {
                    Object[] queryIds = new Object[queryValues.length];
                    String[] tempStrArr = null;
                    Map<String, String> _field_query_value_ = new java.util.HashMap<String, String>(queryValues.length);
                    for (int i = 0; i < queryValues.length; i++) {
                        tempStrArr = queryValues[i].split("\\|");
                        _field_query_value_.put(tempStrArr[0], tempStrArr.length > 1 ? tempStrArr[1] : "");
                        if ("String".equals(identityClass)) {
                            queryIds[i] = tempStrArr[0];
                        } else if ("Long".equals(identityClass)) {
                            queryIds[i] = Long.parseLong(tempStrArr[0]);
                        } else {
                            queryIds[i] = Integer.parseInt(tempStrArr[0]);
                        }
                    }
                    if (queryIds.length == 1) {
                        if ("eq".equals(queryType)) {
                            helper.eq(HqlHelper.makeFieldChain(propertyName, "id"), queryIds[0]);
                        } else {
                            helper.ne(HqlHelper.makeFieldChain(propertyName, "id"), queryIds[0]);
                        }
                    } else {
                        if ("eq".equals(queryType)) {
                            helper.in(HqlHelper.makeFieldChain(propertyName, "id"), queryIds);
                        } else {
                            helper.notIn(HqlHelper.makeFieldChain(propertyName, "id"), queryIds);
                        }
                    }
                    if (model != null) {
                        model.addAttribute(queryValueKey, _field_query_value_);
                        model.addAttribute(queryTypeKey, queryType);
                    }
                }
            }
        } else if (isEnumInterface(fieldType)) {
            if ("eq".equals(queryType) || "ne".equals(queryType)) {
                queryValues = request.getParameterValues(queryValueKey);
                if (queryValues == null || queryValues.length == 0) {
                    queryValues = request.getParameterValues(queryValueKey + "[]");
                }
                if (queryValues != null && queryValues.length > 0) {
                    List<Object> queryIds = new ArrayList<Object>(queryValues.length);
                    StringBuilder _field_query_value_ = new StringBuilder(",");
                    for (String queryValue : queryValues) {
                        if (StringUtils.isBlank(queryValue)) {
                            continue;
                        }
                        _field_query_value_.append(queryValue).append(",");
                        queryIds.add(getEnumInterfaceValue((Class<? extends EnumInterface>) fieldType, queryValue));
                    }
                    if (queryIds.size() == 1) {
                        if ("eq".equals(queryType)) {
                            helper.eq(propertyName, queryIds.get(0));
                        } else {
                            helper.ne(propertyName, queryIds.get(0));
                        }
                    } else {
                        if ("eq".equals(queryType)) {
                            helper.in(propertyName, queryIds);
                        } else {
                            helper.notIn(propertyName, queryIds);
                        }
                    }
                    if (model != null) {
                        model.addAttribute(queryValueKey, _field_query_value_.toString());
                        model.addAttribute(queryTypeKey, queryType);
                    }
                }
            }
        } else if (fieldType.equals(Boolean.class)) {
            if ("eq".equals(queryType)) {
                queryValues = request.getParameterValues(queryValueKey);
                if (queryValues == null || queryValues.length == 0) {
                    queryValues = request.getParameterValues(queryValueKey + "[]");
                }
                if (queryValues != null && queryValues.length > 0) {
                    List<Object> queryIds = new ArrayList<Object>(queryValues.length);
                    StringBuilder _field_query_value_ = new StringBuilder(",");
                    for (String queryValue : queryValues) {
                        if (StringUtils.isBlank(queryValue)) {
                            continue;
                        }
                        _field_query_value_.append(queryValue).append(",");
                        queryIds.add("1".equals(queryValue));
                    }
                    if (queryIds.size() == 1) {
                        helper.eq(propertyName, queryIds.get(0));
                    } else {
                        helper.in(propertyName, queryIds);
                    }
                    if (model != null) {
                        model.addAttribute(queryValueKey, _field_query_value_.toString());
                        model.addAttribute(queryTypeKey, queryType);
                    }
                }
            }
        } else if (isDate(fieldType)) {
            if ("notBetween".equals(queryType) || "between".equals(queryType)) {
                String queryValueKeyBegin = queryValueKey + "_begin";
                String queryValueKeyEnd = queryValueKey + "_end";
                queryValue1 = request.getParameter(queryValueKeyBegin);
                queryValue2 = request.getParameter(queryValueKeyEnd);
                Object object1 = parseSpecialDate(queryValue1, dateFormat, fieldType);
                Object object2 = parseSpecialDate(queryValue2, dateFormat, fieldType);
                if (object1 != null && object2 != null) {
                    if ("between".equals(queryType)) {
                        helper.between(propertyName, object1, object2);
                    } else {
                        helper.notBetween(propertyName, object1, object2);
                    }
                    if (model != null) {
                        model.addAttribute(queryValueKeyBegin, queryValue1);
                        model.addAttribute(queryValueKeyEnd, queryValue2);
                        model.addAttribute(queryTypeKey, queryType);
                    }
                }
            } else {
                queryValue1 = request.getParameter(queryValueKey);
                Object object = parseSpecialDate(queryValue1, dateFormat, fieldType);
                if (object != null) {
                    if ("eq".equals(queryType)) {
                        helper.eq(propertyName, object);
                    } else if ("ne".equals(queryType)) {
                        helper.ne(propertyName, object);
                    } else if ("le".equals(queryType)) {
                        helper.le(propertyName, object);
                    } else if ("ge".equals(queryType)) {
                        helper.ge(propertyName, object);
                    } else if ("lt".equals(queryType)) {
                        helper.lt(propertyName, object);
                    } else if ("gt".equals(queryType)) {
                        helper.gt(propertyName, object);
                    }
                    if (model != null) {
                        model.addAttribute(queryValueKey, queryValue1);
                        model.addAttribute(queryTypeKey, queryType);
                    }
                }
            }
        } else if (fieldType.equals(String.class)) {
            queryValue1 = request.getParameter(queryValueKey);
            if (StringUtils.isNotBlank(queryValue1)) {
                if ("eq".equals(queryType)) {
                    helper.eq(propertyName, queryValue1);
                } else if ("ne".equals(queryType)) {
                    helper.ne(propertyName, queryValue1);
                } else if ("like".equals(queryType)) {
                    helper.like(propertyName, queryValue1);
                } else if ("notLike".equals(queryType)) {
                    helper.notLike(propertyName, queryValue1);
                }
                if (model != null) {
                    model.addAttribute(queryValueKey, queryValue1);
                    model.addAttribute(queryTypeKey, queryType);
                }
            }
        } else {
            try {
                if ("notBetween".equals(queryType) || "between".equals(queryType)) {
                    String queryValueKeyBegin = queryValueKey + "_begin";
                    String queryValueKeyEnd = queryValueKey + "_end";
                    queryValue1 = request.getParameter(queryValueKeyBegin);
                    queryValue2 = request.getParameter(queryValueKeyEnd);
                    if (StringUtils.isNotBlank(queryValue1) && StringUtils.isNotBlank(queryValue2)) {
                        Object object1 = fieldType.getConstructor(String.class).newInstance(queryValue1);
                        Object object2 = fieldType.getConstructor(String.class).newInstance(queryValue2);
                        if ("between".equals(queryType)) {
                            helper.between(propertyName, object1, object2);
                        } else {
                            helper.notBetween(propertyName, object1, object2);
                        }
                        if (model != null) {
                            model.addAttribute(queryValueKeyBegin, queryValue1);
                            model.addAttribute(queryValueKeyEnd, queryValue2);
                            model.addAttribute(queryTypeKey, queryType);
                        }
                    }
                } else {
                    queryValue1 = request.getParameter(queryValueKey);
                    if (StringUtils.isNotBlank(queryValue1)) {
                        Object object = fieldType.getConstructor(String.class).newInstance(queryValue1);
                        if (object != null) {
                            if ("eq".equals(queryType)) {
                                helper.eq(propertyName, object);
                            } else if ("ne".equals(queryType)) {
                                helper.ne(propertyName, object);
                            } else if ("le".equals(queryType)) {
                                helper.le(propertyName, object);
                            } else if ("ge".equals(queryType)) {
                                helper.ge(propertyName, object);
                            } else if ("lt".equals(queryType)) {
                                helper.lt(propertyName, object);
                            } else if ("gt".equals(queryType)) {
                                helper.gt(propertyName, object);
                            }
                            if (model != null) {
                                model.addAttribute(queryValueKey, queryValue1);
                                model.addAttribute(queryTypeKey, queryType);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
