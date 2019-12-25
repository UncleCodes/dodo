package com.dodo.privilege.fmk.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dodo.privilege.enums.FieldRightType;
import com.dodo.privilege.security.DodoSecurityService;

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
@Component("hasFieldRight")
public class HasFieldRight implements TemplateMethodModelEx {

    @Autowired
    private DodoSecurityService securityService;

    public HasFieldRight() {
    }

    @SuppressWarnings("rawtypes")
    public Object exec(List list) {
        try {
            if (securityService.hasFieldRight(String.valueOf(list.get(0)), String.valueOf(list.get(1)),
                    FieldRightType.valueOf(String.valueOf(list.get(2))))) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}