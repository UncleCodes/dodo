package com.dodo.common.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoUploaderUtil;

import java.io.IOException;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DisableUploadJspFilter implements Filter {
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest)servletRequest;
    	HttpServletResponse response = (HttpServletResponse)servletResponse;
    	if(request.getServletPath().startsWith(DodoUploaderUtil.tempFileDirServletPrefix)||request.getServletPath().startsWith(DodoUploaderUtil.targetFileDirServletPrefix)){
    		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    		return ;
    	}
    	filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}