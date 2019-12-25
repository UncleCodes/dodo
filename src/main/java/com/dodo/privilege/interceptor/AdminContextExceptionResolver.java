package com.dodo.privilege.interceptor;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class AdminContextExceptionResolver implements HandlerExceptionResolver {
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ex.printStackTrace();
		StringWriter sWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(sWriter);
		ex.printStackTrace(printWriter);
		request.setAttribute(AdminContextInterceptor.EXCEPTION_IN_REQUEST,sWriter.toString());
		HandlerMethod a = (HandlerMethod)handler;
		// ajax or interface
		if(a==null||a.getMethod().isAnnotationPresent(ResponseBody.class)){
			Map<String,Object> attrMaps = new HashMap<String, Object>(8);
			attrMaps.put("_exception_tip_","An exception occurs, See the log for more information.");
			//attrMaps.put("exception_type",ex.getClass());
			attrMaps.put("_exception_type_str_",ex.getClass().getName());
			attrMaps.put("_exception_msg_",ex.getMessage());
			attrMaps.put("_exception_status_","error");
			attrMaps.put("_exception_message_","A runtime exception occurs");
			//attrMaps.put("exception_stack_trace",excepString);
			ModelAndView modelAndView = new ModelAndView();
			MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
			mappingJackson2JsonView.setAttributesMap(attrMaps);
			modelAndView.setView(mappingJackson2JsonView);
			return modelAndView;
		}else{
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("info");
			modelAndView.addObject("dialogType","error");
			modelAndView.addObject("infoTip","An exception occurs, See the log for more information.");
			return modelAndView;
		}
	}
}
