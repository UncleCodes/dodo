package com.dodo.privilege.interceptor;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dodo.common.annotation.sign.DodoBizLogRequired;
import com.dodo.common.framework.bean.thread.MapThreadVar;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.monitor_2.log_1.BusiLog;
import com.dodo.privilege.entity.monitor_2.log_1.ProcessLog;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.JacksonUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoLogRecordUtil;
import com.dodo.utils.http.HttpUtils;

/**
 *
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class AdminContextInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER                 = LoggerFactory.getLogger(AdminContextInterceptor.class);
    private final static String REQ_START_TIME         = "DODO_REQUEST_START_TIME";
    private final static String REQ_ACTION_FINISH_TIME = "DODO_ACTION_FINISH_TIME";
    private final static String REQ_LOG_FINISH_TIME    = "DODO_LOG_FINISH_TIME";
    private final static String REQ_PAGE_FINISH_TIME   = "DODO_PAGE_FINISH_TIME";
    private final static String REQ_VIEW_NAME          = "DODO_VIEW_NAME";
    public final static String  EXCEPTION_IN_REQUEST   = "EXCEPTION_IN_REQUEST";

    private HqlHelperService    hqlHelperService;

    public void setHqlHelperService(HqlHelperService hqlHelperService) {
        this.hqlHelperService = hqlHelperService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(REQ_START_TIME, System.currentTimeMillis());
        if (DodoCommonConfigUtil.isDev) {
            RequestReporter.reportRequest(request, (HandlerMethod) handler);
        }
        MapThreadVar.remove();
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        String callBack = request.getParameter("callBack");
        if (modelAndView != null) {
            modelAndView.getModelMap().put("callBack", callBack);
        }
        request.setAttribute(REQ_ACTION_FINISH_TIME, System.currentTimeMillis());
        request.setAttribute(REQ_VIEW_NAME,
                modelAndView == null ? "" : StringUtils.defaultIfEmpty(modelAndView.getViewName(), ""));
        HandlerMethod a = (HandlerMethod) handler;
        if (DodoLogRecordUtil.isLogRecord) {
            if (a.getMethod().isAnnotationPresent(DodoBizLogRequired.class)) {
                Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Map<String, Object> mapss = MapThreadVar.get();
                if (mapss != null && mapss.size() > 0) {
                    String entityClassFullName = mapss.get("entityClassFullName").toString();
                    String entityShowName = mapss.get("entityShowName").toString();
                    String updateType = mapss.get("updateType").toString();
                    Integer updateTypeSeq = Integer.parseInt(mapss.get("updateTypeSeq").toString());

                    Object oldObjs = mapss.get("oldObjs");
                    Object newObjs = mapss.get("newObjs");
                    // 添加
                    if (updateTypeSeq == 0) {
                        BusiLog busiLogger = new BusiLog();
                        busiLogger.setEntityClassName(entityClassFullName);
                        busiLogger.setEntityName(entityShowName);
                        busiLogger.setEntityId(String.valueOf(((BaseEntity) oldObjs).getId()));
                        busiLogger.setUpdatePerson(admin);
                        busiLogger.setModifyLog(CommonUtil.getObjectAddLog(oldObjs));
                        busiLogger.setUpdateType(updateType);
                        busiLogger.setUpdatePersonIp(HttpUtils.getRemoteAddr(request));
                        busiLogger.setUpdateBrowserType(HttpUtils.getBrowser(request));
                        hqlHelperService.save(busiLogger);
                    }
                    // 修改
                    else if (updateTypeSeq == 1) {
                        BusiLog busiLogger = new BusiLog();
                        busiLogger.setEntityClassName(entityClassFullName);
                        busiLogger.setEntityName(entityShowName);
                        busiLogger.setEntityId(String.valueOf(((BaseEntity) newObjs).getId()));
                        busiLogger.setUpdatePerson(admin);
                        busiLogger.setModifyLog(CommonUtil.getObjectUpdateLog(oldObjs, newObjs));
                        busiLogger.setUpdateType(updateType);
                        busiLogger.setUpdatePersonIp(HttpUtils.getRemoteAddr(request));
                        busiLogger.setUpdateBrowserType(HttpUtils.getBrowser(request));
                        hqlHelperService.save(busiLogger);
                    }
                    // 删除
                    else if (updateTypeSeq == 2) {
                        List<?> entitysList = (List<?>) oldObjs;
                        for (Object obj : entitysList) {
                            BusiLog busiLogger = new BusiLog();
                            busiLogger.setEntityClassName(entityClassFullName);
                            busiLogger.setEntityName(entityShowName);
                            busiLogger.setEntityId(String.valueOf(((BaseEntity) obj).getId()));
                            busiLogger.setUpdatePerson(admin);
                            busiLogger.setModifyLog(CommonUtil.getObjectDeleteLog(obj));
                            busiLogger.setUpdateType(updateType);
                            busiLogger.setUpdatePersonIp(HttpUtils.getRemoteAddr(request));
                            busiLogger.setUpdateBrowserType(HttpUtils.getBrowser(request));
                            hqlHelperService.save(busiLogger);
                        }
                    }
                    // 批量添加
                    else if (updateTypeSeq == 3) {
                        List<?> entitysList = (List<?>) oldObjs;
                        for (Object obj : entitysList) {
                            BusiLog busiLogger = new BusiLog();
                            busiLogger.setEntityClassName(entityClassFullName);
                            busiLogger.setEntityName(entityShowName);
                            busiLogger.setEntityId(String.valueOf(((BaseEntity) obj).getId()));
                            busiLogger.setUpdatePerson(admin);
                            busiLogger.setModifyLog(CommonUtil.getObjectAddLog(obj));
                            busiLogger.setUpdateType(updateType);
                            busiLogger.setUpdatePersonIp(HttpUtils.getRemoteAddr(request));
                            busiLogger.setUpdateBrowserType(HttpUtils.getBrowser(request));
                            hqlHelperService.save(busiLogger);
                        }
                    } else {
                        LOGGER.error("BusiLog UpdateType Error:{}", updateTypeSeq);
                    }
                }
            }
        }

        request.setAttribute(REQ_LOG_FINISH_TIME, System.currentTimeMillis());
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MapThreadVar.remove();
        Object tempTime = null;
        HandlerMethod a = (HandlerMethod) handler;
        request.setAttribute(REQ_PAGE_FINISH_TIME, System.currentTimeMillis());
        Admin admin = null;
        try {
            admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
        }
        try {
            Map<String, String> requestHeaders = new HashMap<String, String>();
            Enumeration<String> reqEnum = request.getHeaderNames();
            while (reqEnum.hasMoreElements()) {
                String headerName = reqEnum.nextElement();
                requestHeaders.put(headerName, request.getHeader(headerName));
            }
            String VIEW_NAME = String.valueOf(request.getAttribute(REQ_VIEW_NAME));
            Long START_TIME = Long.parseLong(request.getAttribute(REQ_START_TIME).toString());
            tempTime = request.getAttribute(REQ_ACTION_FINISH_TIME);
            Long ACTION_FINISH_TIME = tempTime == null ? new Date().getTime() : Long.parseLong(tempTime.toString());
            tempTime = request.getAttribute(REQ_LOG_FINISH_TIME);
            Long LOG_FINISH_TIME = tempTime == null ? new Date().getTime() : Long.parseLong(tempTime.toString());
            tempTime = request.getAttribute(REQ_PAGE_FINISH_TIME);
            Long PAGE_FINISH_TIME = tempTime == null ? new Date().getTime() : Long.parseLong(tempTime.toString());
            ProcessLog processLog = new ProcessLog();
            processLog.setActionBean(a.getBean().toString());
            processLog.setActionMethod(a.getMethod().getName());
            processLog.setActionTime(CommonUtil.getSpecialDateStr(new Date(ACTION_FINISH_TIME - START_TIME),
                    "mm:ss.SSS"));
            processLog.setActionView(VIEW_NAME);
            processLog.setTotalTime(CommonUtil.getSpecialDateStr(new Date(PAGE_FINISH_TIME - START_TIME), "mm:ss.SSS"));
            processLog.setAdmin(admin);
            processLog.setLogTime(CommonUtil.getSpecialDateStr(new Date(LOG_FINISH_TIME - ACTION_FINISH_TIME),
                    "mm:ss.SSS"));
            processLog.setRequestBeginTime(CommonUtil
                    .getSpecialDateStr(new Date(START_TIME), "yyyy-MM-dd HH:mm:ss.SSS"));
            processLog.setRequestEndTime(CommonUtil.getSpecialDateStr(new Date(PAGE_FINISH_TIME),
                    "yyyy-MM-dd HH:mm:ss.SSS"));
            processLog.setViewTime(CommonUtil.getSpecialDateStr(new Date(PAGE_FINISH_TIME - LOG_FINISH_TIME),
                    "mm:ss.SSS"));
            processLog.setRequestParameters(JacksonUtil.toJackson(request.getParameterMap()));
            processLog.setServletPath(request.getServletPath());
            processLog.setRequestHeaders(JacksonUtil.toJackson(requestHeaders));
            processLog.setRequestIp(HttpUtils.getRemoteAddr(request));
            processLog.setBrowserType(HttpUtils.getBrowser(request));
            processLog.setExceptionInfo((String) request.getAttribute(EXCEPTION_IN_REQUEST));
            if (processLog.getExceptionInfo() != null && VIEW_NAME == null) {
                processLog.setActionView("Controller Exception");
                processLog.setViewTime("00:00.000");
            }
            hqlHelperService.save(processLog);
            request.removeAttribute(EXCEPTION_IN_REQUEST);
        } catch (Exception e) {
        }
        super.afterCompletion(request, response, handler, ex);
    }
}
