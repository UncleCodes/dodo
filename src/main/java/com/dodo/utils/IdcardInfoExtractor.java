package com.dodo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 类说明:提取身份证相关信息
 * </p>
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class IdcardInfoExtractor {
    private static final DateTimeFormatter formatter   = DateTimeFormatter.ofPattern("yyyyMMdd");
    // 省份   
    private String                         province;
    // 城市   
    private String                         city;
    // 区县   
    private String                         region;
    // 年份   
    private int                            year;
    // 月份   
    private int                            month;
    // 日期   
    private int                            day;
    // 性别   
    private String                         gender;
    // 出生日期   
    private LocalDate                      birthday;

    private Map<String, String>            cityCodeMap = new HashMap<String, String>(50) {
                                                           private static final long serialVersionUID = 4815778737226363156L;
                                                           {
                                                               put("11", "北京");
                                                               put("12", "天津");
                                                               put("13", "河北");
                                                               put("14", "山西");
                                                               put("15", "内蒙古");
                                                               put("21", "辽宁");
                                                               put("22", "吉林");
                                                               put("23", "黑龙江");
                                                               put("31", "上海");
                                                               put("32", "江苏");
                                                               put("33", "浙江");
                                                               put("34", "安徽");
                                                               put("35", "福建");
                                                               put("36", "江西");
                                                               put("37", "山东");
                                                               put("41", "河南");
                                                               put("42", "湖北");
                                                               put("43", "湖南");
                                                               put("44", "广东");
                                                               put("45", "广西");
                                                               put("46", "海南");
                                                               put("50", "重庆");
                                                               put("51", "四川");
                                                               put("52", "贵州");
                                                               put("53", "云南");
                                                               put("54", "西藏");
                                                               put("61", "陕西");
                                                               put("62", "甘肃");
                                                               put("63", "青海");
                                                               put("64", "宁夏");
                                                               put("65", "新疆");
                                                               put("71", "台湾");
                                                               put("81", "香港");
                                                               put("82", "澳门");
                                                               put("91", "国外");
                                                           }
                                                       };

    /**
     * 通过构造方法初始化各个成员属性
     */
    public IdcardInfoExtractor(String idcard) {
        try {
            if (IdcardValidator.isValidatedAllIdcard(idcard)) {
                if (idcard.length() == 15) {
                    idcard = IdcardValidator.convertIdcarBy15bit(idcard);
                }
                // 获取省份   
                String provinceId = idcard.substring(0, 2);
                Set<String> key = this.cityCodeMap.keySet();
                for (String id : key) {
                    if (id.equals(provinceId)) {
                        this.province = this.cityCodeMap.get(id);
                        break;
                    }
                }

                // 获取性别   
                String id17 = idcard.substring(16, 17);
                if (Integer.parseInt(id17) % 2 != 0) {
                    this.gender = "男";
                } else {
                    this.gender = "女";
                }

                // 获取出生日期   
                String birthday = idcard.substring(6, 14);
                this.birthday = LocalDate.parse(birthday, formatter);
                this.year = this.birthday.get(ChronoField.YEAR);
                this.month = this.birthday.get(ChronoField.MONTH_OF_YEAR);
                this.day = this.birthday.get(ChronoField.DAY_OF_MONTH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "省份：" + this.province + ",性别：" + this.gender + ",出生日期：" + this.birthday;
    }
}