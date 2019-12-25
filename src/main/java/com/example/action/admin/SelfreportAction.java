package com.example.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("selfreportAction")
@RequestMapping("${dodo.backmanage.view.rootPath}/selfreport")
public class SelfreportAction {

    // Demo报表 - 带跳转（后台） 单元格点击的时候的测试页面
    @RequestMapping("/test2.jhtml")
    public String view(Model model, HttpServletRequest request, String fieldValue) {
        model.addAttribute("fieldValue", fieldValue);
        return "test2";
    }
}
