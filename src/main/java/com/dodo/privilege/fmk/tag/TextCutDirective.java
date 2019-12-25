package com.dodo.privilege.fmk.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.dodo.utils.TemplateParameterUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Component("textCut")
public class TextCutDirective implements TemplateDirectiveModel {
    public static final String PARAM_S      = "s";
    public static final String PARAM_LEN    = "len";
    public static final String PARAM_APPEND = "append";

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String s = TemplateParameterUtil.getString(PARAM_S, params);
        Integer len = TemplateParameterUtil.getNumber(PARAM_LEN, params);
        String append = TemplateParameterUtil.getString(PARAM_APPEND, params);
        if (s != null) {
            Writer out = env.getOut();
            if (len != null) {
                out.append(textCut(s, len, append));
            } else {
                out.append(s);
            }
        }
    }

    /**
     * 剪切文本。如果进行了剪切，则在文本后加上 append
     * 
     * @param s
     *            剪切对象。
     * @param len
     *            编码小于256的作为一个字符，大于256的作为两个字符。
     * @return
     */
    public static String textCut(String s, int len, String append) {
        if (s == null) {
            return null;
        }
        int slen = s.length();
        if (slen <= len) {
            return s;
        }
        // 最大计数（如果全是英文）
        int maxCount = len * 2;
        int count = 0;
        int i = 0;
        for (; count < maxCount && i < slen; i++) {
            if (s.codePointAt(i) < 256) {
                count++;
            } else {
                count += 2;
            }
        }
        if (i < slen) {
            if (count > maxCount) {
                i--;
            }
            if (!StringUtils.isBlank(append)) {
                if (s.codePointAt(i - 1) < 256) {
                    i -= 2;
                } else {
                    i--;
                }
                return s.substring(0, i) + append;
            } else {
                return s.substring(0, i);
            }
        } else {
            return s;
        }
    }
}
