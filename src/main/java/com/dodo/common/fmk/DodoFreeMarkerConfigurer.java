package com.dodo.common.fmk;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.jsp.TaglibFactory;

/**
 * 
 * 解决一个警告：
 * <p>
 * 警告: Custom EL functions won't be loaded because no ObjectWrapper was
 * specified for the TaglibFactory (via TaglibFactory.setObjectWrapper(...),
 * exists since 2.3.22).
 * </p>
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoFreeMarkerConfigurer extends FreeMarkerConfigurer {
    @Override
    public TaglibFactory getTaglibFactory() {
        TaglibFactory tagLibFactory = super.getTaglibFactory();
        if (tagLibFactory.getObjectWrapper() == null) {
            tagLibFactory.setObjectWrapper(super.getConfiguration().getObjectWrapper());
        }
        return tagLibFactory;
    }
}
