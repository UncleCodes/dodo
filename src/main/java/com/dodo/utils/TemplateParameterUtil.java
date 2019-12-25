package com.dodo.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;
import freemarker.template.utility.DeepUnwrap;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class TemplateParameterUtil {
    public static String getString(String key, Map<String, TemplateModel> modelMap) throws TemplateModelException {
        TemplateModel templateModel = (TemplateModel) modelMap.get(key);
        if (templateModel == null)
            return null;
        if ((templateModel instanceof TemplateScalarModel))
            return ((TemplateScalarModel) templateModel).getAsString();
        if ((templateModel instanceof TemplateNumberModel))
            return ((TemplateNumberModel) templateModel).getAsNumber().toString();
        throw new TemplateModelException("The \"" + key + "\" parameter " + "must be a string.");
    }

    public static Integer getNumber(String key, Map<String, TemplateModel> modelMap) throws TemplateModelException {
        TemplateModel templateModel = (TemplateModel) modelMap.get(key);
        if (templateModel == null)
            return null;
        if ((templateModel instanceof TemplateScalarModel)) {
            String str = ((TemplateScalarModel) templateModel).getAsString();
            return StringUtils.isBlank(str) ? null : Integer.valueOf(Integer.parseInt(str));
        }
        if ((templateModel instanceof TemplateNumberModel))
            return Integer.valueOf(((TemplateNumberModel) templateModel).getAsNumber().intValue());
        throw new TemplateModelException("The \"" + key + "\" parameter " + "must be a integer.");
    }

    public static Boolean getBoolean(String key, Map<String, TemplateModel> modelMap) throws TemplateModelException {
        TemplateModel templateModel = (TemplateModel) modelMap.get(key);
        if (templateModel == null)
            return null;
        if ((templateModel instanceof TemplateScalarModel)) {
            String str = ((TemplateScalarModel) templateModel).getAsString();
            return StringUtils.isBlank(str) ? null : Boolean.valueOf(str);
        }
        if ((templateModel instanceof TemplateBooleanModel))
            return Boolean.valueOf(((TemplateBooleanModel) templateModel).getAsBoolean());
        throw new TemplateModelException("The \"" + key + "\" parameter " + "must be a boolean.");
    }

    public static Date getDate(String key, Map<String, TemplateModel> modelMap) throws TemplateModelException {
        TemplateModel templateModel = (TemplateModel) modelMap.get(key);
        if (templateModel == null)
            return null;
        if ((templateModel instanceof TemplateScalarModel)) {
            String str = ((TemplateScalarModel) templateModel).getAsString();
            try {
                return StringUtils.isBlank(str) ? null : DateUtils.parseDate(str, new String[] { "yyyy-MM", "yyyyMM",
                        "yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss",
                        "yyyy/MM/dd HH:mm:ss" });
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        if ((templateModel instanceof TemplateDateModel))
            return ((TemplateDateModel) templateModel).getAsDate();
        throw new TemplateModelException("The \"" + key + "\" parameter " + "must be a date.");
    }

    public static Object getObject(String key, Map<String, TemplateModel> modelMap) throws TemplateModelException {
        TemplateModel templateModel = (TemplateModel) modelMap.get(key);
        if (templateModel == null)
            return null;
        try {
            return DeepUnwrap.unwrap(templateModel);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        throw new TemplateModelException("The \"" + key + "\" parameter " + "must be a object.");
    }
}
