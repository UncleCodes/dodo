package com.dodo.common.fmk.tag;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.framework.bean.file.DodoFile;
import com.dodo.utils.JacksonUtil;
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
public class ParseFileFieldDirective implements TemplateDirectiveModel {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment environment, Map map, TemplateModel[] atemplatemodel,
            TemplateDirectiveBody templatedirectivebody) throws TemplateException, IOException {
        String var = "dodoFiles";
        try {
            String json = TemplateParameterUtil.getString("json", map);
            String tempVar = TemplateParameterUtil.getString("var", map);
            if (StringUtils.isNotBlank(tempVar)) {
                var = tempVar;
            }
            DodoFile[] files = null;
            try {
                files = JacksonUtil.toObject(json, DodoFile[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (files == null) {
                files = new DodoFile[] {};
            }
            environment.setVariable(var, TagConst.DEFAULT_WRAPPER.wrap(files));
        } catch (Exception e) {
            e.printStackTrace();
            environment.setVariable("dodoFiles", TagConst.DEFAULT_WRAPPER.wrap(new DodoFile[] {}));
        }
        templatedirectivebody.render(environment.getOut());
    }
}