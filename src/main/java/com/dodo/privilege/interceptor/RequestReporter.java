package com.dodo.privilege.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import com.dodo.privilege.interceptor.client.Client;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public final class RequestReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestReporter.class);

    public static void reportRequest(HttpServletRequest request, HandlerMethod handlerMethod) throws Exception {
        StringBuilder sb = new StringBuilder("\n------------------------------Dodo Request Reporter -------- ")
                .append(" ------------------------------\n");
        sb.append("User-Agent     : ").append(request.getHeader("User-Agent")).append("\n");
        sb.append("Client         : ").append(Client.getCurrentClient(request)).append("\n");
        sb.append("ServletPath    : ").append(request.getServletPath()).append("\n");
        sb.append("ActionBean     : ").append(handlerMethod.getBean().toString()).append("\n");
        sb.append("ActionMethod   : ").append(handlerMethod.getMethod().getName()).append("\n");

        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("Parameter      : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=").append(values[0]);
                } else {
                    sb.append(name).append("[]={");
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0)
                            sb.append(",");
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
        sb.append("--------------------------------------------------------------------------------\n");
        LOGGER.info(sb.toString());
    }
}
