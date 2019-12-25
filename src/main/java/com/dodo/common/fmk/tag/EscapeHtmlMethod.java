package com.dodo.common.fmk.tag;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class EscapeHtmlMethod implements TemplateMethodModelEx {
    public EscapeHtmlMethod() {
    }

    @SuppressWarnings("rawtypes")
    public Object exec(List list) {
        try {
            return new SimpleScalar(escapeHtml(list.get(0).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleScalar("");
        }
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
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }
}