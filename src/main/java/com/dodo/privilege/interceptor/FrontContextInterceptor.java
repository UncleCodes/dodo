package com.dodo.privilege.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class FrontContextInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (DodoCommonConfigUtil.isDev) {
            RequestReporter.reportRequest(request, (HandlerMethod) handler);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        //默认系统中的文件是采用'读取文件-response输出给客户端'方式进行浏览的 当配置了fileServer属性的时候可以静态访问
        //静态访问不需要登录系统 不安全
        if (modelAndView != null) {
            modelAndView.getModelMap().put("fileServer", DodoCommonConfigUtil.fileServer);
            modelAndView.getModelMap().put("fileServerTemp", DodoCommonConfigUtil.fileServerTemp);
            modelAndView.getModelMap().put("fileServerNotAllowed", DodoCommonConfigUtil.fileServerNotAllowed);
            modelAndView.getModelMap().put("fileServerSubstitute", DodoCommonConfigUtil.fileServerSubstitute);
            modelAndView.getModelMap().put("webHomeUrl", DodoCommonConfigUtil.webHomeUrl);
        }
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
