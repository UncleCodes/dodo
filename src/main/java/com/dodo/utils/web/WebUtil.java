package com.dodo.utils.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WebUtil {
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

    public static boolean isHttpProtocol(String url) {
        return StringUtils.isNotBlank(url) && (url.startsWith("http://") || url.startsWith("https://"));
    }

    /**
     * 请求一个远程文件的内容
     *
     * @param httpUrl
     * @return
     * @throws java.io.IOException
     */
    public static String requestFile(String httpUrl) {
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) {
            return null;
        }
        InputStream in = null;
        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        try {
            in = url.openStream();
            streamReader = new InputStreamReader(in);
            reader = new BufferedReader(streamReader);
            String lineCode;
            StringBuilder pageCodeBuffer = new StringBuilder();
            while ((lineCode = reader.readLine()) != null) {
                pageCodeBuffer.append(lineCode);
                pageCodeBuffer.append("\n");
            }

            return pageCodeBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Map<String, String> getParameterMap(URI uri) {
        String qStr = uri.getQuery();
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(qStr)) {
            String[] params = qStr.split("&");
            for (String param : params) {
                String[] p = param.split("=");
                map.put(p[0], p.length > 1 ? p[1] : null);
            }
        }
        return map;
    }

    /**
     * 获取bool类型的数据
     *
     * @param request
     * @param key
     * @return
     */
    public static Boolean getBooleanParam(HttpServletRequest request, String key) {
        String param = request.getParameter(key);
        boolean is = false;
        if (param != null) {
            if (StringUtils.isBlank(param)) {
                is = true;
            } else {
                try {
                    is = Boolean.parseBoolean(param);
                } catch (Exception e) {
                    is = true;
                }
            }
        }
        return is;
    }

    /**
     * 获取查询字符串
     *
     * @param request
     * @return
     */
    public static String getQueryString(HttpServletRequest request) {
        //根据JSP规范获取include方式的请求字符串
        Object queryString = request.getAttribute("javax.servlet.include.query_string");
        if (queryString == null) {
            //根据JSP规范获取forward方式的请求字符串
            queryString = request.getAttribute("javax.servlet.forward.query_string");
        }

        if (queryString != null) {
            return queryString.toString();
        }
        return request.getQueryString();
    }

    /**
     * 获取请求的资源路径
     *
     * @param request
     * @return
     */
    public static String getRequestUri(HttpServletRequest request) {
        //根据JSP规范获取include方式的请求的资源路径
        Object queryUri = request.getAttribute("javax.servlet.include.request_uri");
        if (queryUri == null) {
            //根据JSP规范获取forward方式的请求的资源路径
            queryUri = request.getAttribute("javax.servlet.forward.request_uri");
        }

        if (queryUri != null) {
            return queryUri.toString();
        }
        return request.getRequestURI();
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] carr = request.getCookies();
        if (carr != null)
            for (Cookie c : carr)
                if (name.equals(c.getName()))
                    return c.getValue();
        return null;
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        Cookie c = new Cookie(name, value);
        c.setPath(request.getContextPath() + "/");
        response.addCookie(c);
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value,
            Integer maxAge) {
        Cookie c = new Cookie(name, value);
        if (maxAge != null)
            c.setMaxAge(maxAge.intValue());
        c.setPath(request.getContextPath() + "/");
        response.addCookie(c);
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie c = new Cookie(name, null);
        c.setMaxAge(0);
        c.setPath(request.getContextPath() + "/");
        response.addCookie(c);
    }
}
