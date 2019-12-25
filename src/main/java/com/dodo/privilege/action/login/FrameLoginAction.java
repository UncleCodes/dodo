package com.dodo.privilege.action.login;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dodo.privilege.security.DodoUsernamePasswordAuthenticationFilter;
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
@Controller
@RequestMapping("${dodo.backmanage.view.rootPath}/enjoy")
public class FrameLoginAction {
    @RequestMapping("/otherlogin.jhtml")
    public String otherlogin(Model model) {
        return "page_otherlogin";
    }

    @RequestMapping("/timeout.jhtml")
    public String timeout(Model model) {
        return "page_timeout";
    }

    @RequestMapping("/accessdeny.jhtml")
    public String accessdeny(Model model) {
        return "page_accessdeny";
    }

    @RequestMapping("/login_index.jhtml")
    public String login_index(Model model, HttpServletRequest request) {
        String loginSalt = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute(DodoUsernamePasswordAuthenticationFilter.LOGIN_SALT_IN_SESSION, loginSalt);
        session.setAttribute(DodoUsernamePasswordAuthenticationFilter.LOGIN_SALT_DATE_IN_SESSION, new Date());
        model.addAttribute("AUTHENTICATION_EXCEPTION", session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
        model.addAttribute("loginSalt", loginSalt);
        model.addAttribute("passwordSalt", DodoCommonConfigUtil.passwordSalt);
        return "page_login_index";
    }
}