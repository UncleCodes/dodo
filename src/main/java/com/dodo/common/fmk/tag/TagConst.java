package com.dodo.common.fmk.tag;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;

/**
 * 
 * 注释内容<br/>
 * 
 * @author djh
 * 
 * @version
 */
public class TagConst {
    public static DefaultObjectWrapper DEFAULT_WRAPPER = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28)
                                                               .build();
}
