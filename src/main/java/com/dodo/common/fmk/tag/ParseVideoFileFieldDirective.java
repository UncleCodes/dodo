package com.dodo.common.fmk.tag;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.framework.bean.file.DodoVideoFile;
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
public class ParseVideoFileFieldDirective implements TemplateDirectiveModel {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment environment, Map map, TemplateModel[] atemplatemodel,
            TemplateDirectiveBody templatedirectivebody) throws TemplateException, IOException {
        String var = "dodoVideoFiles";
        try {
            String json = TemplateParameterUtil.getString("json", map);
            String tempVar = TemplateParameterUtil.getString("var", map);
            if (StringUtils.isNotBlank(tempVar)) {
                var = tempVar;
            }
            DodoVideoFile[] videoFiles = null;
            try {
                videoFiles = JacksonUtil.toObject(json, DodoVideoFile[].class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (videoFiles == null) {
                videoFiles = new DodoVideoFile[] {};
            }
            environment.setVariable(var, TagConst.DEFAULT_WRAPPER.wrap(videoFiles));
        } catch (Exception e) {
            e.printStackTrace();
            environment.setVariable(var, TagConst.DEFAULT_WRAPPER.wrap(new DodoVideoFile[] {}));
        }
        templatedirectivebody.render(environment.getOut());
    }
}