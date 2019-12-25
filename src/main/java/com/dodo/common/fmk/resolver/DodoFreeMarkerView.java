package com.dodo.common.fmk.resolver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoFreeMarkerView extends FreeMarkerView {
	public static final String CONTEXT_PATH = "base";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void exposeHelpers(Map model, HttpServletRequest request)
			throws Exception {
		super.exposeHelpers(model, request);
		model.put(CONTEXT_PATH, request.getContextPath());
		model.put("fileServer",DodoCommonConfigUtil.fileServer);
		model.put("fileServerTemp",DodoCommonConfigUtil.fileServerTemp);
		model.put("fileServerNotAllowed",DodoCommonConfigUtil.fileServerNotAllowed);
		model.put("fileServerSubstitute",DodoCommonConfigUtil.fileServerSubstitute);
		model.put("webHomeUrl", DodoCommonConfigUtil.webHomeUrl);
		model.put("backManageRootPath", DodoCommonConfigUtil.viewRootPath);
	}
}