package com.example.action.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("${dodo.backmanage.view.rootPath}/special")
public class SpecialAction {
    // Demo系统 - 基础演示 - 自定义按钮演示 - `后台URL`点击后的测试页面
    @RequestMapping("/view.jhtml")
    public String view(String entityId, Model model) {
        model.addAttribute("entityId", entityId);
        return "test_view";
    }
}
