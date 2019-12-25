package com.dodo.common.captcha.octo;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dodo.utils.SpringUtil;
import com.octo.captcha.service.CaptchaService;

/**
 * 验证码工具类
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class CaptchaUtil {
    private static final String CAPTCHASERVICE = "captchaService";

    /**
     * 校验请求中的验证码是否和服务器的验证码一致
     * 
     * @param request
     *            请求对象
     * 
     * @param captchaCodeKey
     *            请求中存储验证码的参数名称
     * 
     * */
    public static boolean validateCaptcha(HttpServletRequest request, String captchaCodeKey) {
        String sessionId = request.getSession().getId();
        String captchaCode = StringUtils.upperCase(request.getParameter(captchaCodeKey));
        if ((StringUtils.isBlank(sessionId)) || (StringUtils.isBlank(captchaCode))) {
            return false;
        }
        CaptchaService captchaService = (CaptchaService) SpringUtil.getBean(CAPTCHASERVICE);
        try {
            if (captchaService.validateResponseForID(sessionId, captchaCode).booleanValue()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 默认请求中存储验证码的参数名称为 j_captcha
     * 
     * @see #validateCaptcha(HttpServletRequest, String)
     * */
    public static boolean validateJCaptcha(HttpServletRequest request) {
        return validateCaptcha(request, "j_captcha");
    }
}