package com.dodo.privilege.action.frame;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dodo.common.framework.bean.tree.DodoTree;
import com.dodo.common.framework.bean.tree.DodoTreeNode;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.security.DodoSecurityService;
import com.dodo.utils.JacksonUtil;
import com.dodo.utils.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

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
@RequestMapping("${dodo.backmanage.view.rootPath}/framemenu")
public class FrameMenuAction {
    @Autowired
    private DodoSecurityService securityService;

    @RequestMapping("/main.jhtml")
    public String main(Model model) {
        return "page_main";
    }

    private void dealMenuNameKey(DodoTreeNode node, HttpServletRequest request) {
        if (StringUtils.isNotBlank(node.getNameKey())) {
            node.setName(SpringUtil.getMessageBack(node.getNameKey(), request));
        }
        if (node.isLeaf()) {
            return;
        }
        for (DodoTreeNode childNode : node.getChildList()) {
            dealMenuNameKey(childNode, request);
        }
    }

    @RequestMapping("/header.jhtml")
    public String header(Model model, HttpServletRequest request) {
        Admin admin = (Admin) securityService.getLoginPrincipal();
        DodoTree menuInfoTree = admin.getMenuInfoTree();
        List<DodoTreeNode> rootNodes = menuInfoTree.getRootNodes();
        for (DodoTreeNode rooTreeNode : rootNodes) {
            dealMenuNameKey(rooTreeNode, request);
        }
        model.addAttribute("menuInfoTree", menuInfoTree);
        return "page_header";
    }

    @RequestMapping("/left_{menuId}.jhtml")
    public String left(@PathVariable String menuId, Model model, HttpServletRequest request) {
        Admin admin = (Admin) securityService.getLoginPrincipal();
        DodoTree menuInfoTree = admin.getMenuInfoTree();
        List<DodoTreeNode> rootNodes = menuInfoTree.getRootNodes();
        for (DodoTreeNode rooTreeNode : rootNodes) {
            dealMenuNameKey(rooTreeNode, request);
        }
        if ("dodo".equals(menuId)) {
            try {
                model.addAttribute("menuInfoTree", JacksonUtil.toJackson(menuInfoTree.getValidTreeNode()));
            } catch (JsonProcessingException e) {
                model.addAttribute("menuInfoTree", null);
            }
        } else if ("whole".equals(menuId)) {
            model.addAttribute("menuInfoTree", menuInfoTree);
        } else {
            model.addAttribute("menuInfoTree", menuInfoTree.getDodoTreeNodeById(menuId));
        }
        return "page_left";
    }

    @RequestMapping("/middle.jhtml")
    public String middle(Model model) {
        return "page_middle";
    }

    @RequestMapping("/index.jhtml")
    public String index(Model model) {
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        long freeMemoery = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long usedMemory = totalMemory - freeMemoery;
        long maxMemory = runtime.maxMemory();
        long useableMemory = maxMemory - totalMemory + freeMemoery;
        model.addAttribute("props", props);
        model.addAttribute("freeMemoery", freeMemoery);
        model.addAttribute("totalMemory", totalMemory);
        model.addAttribute("usedMemory", usedMemory);
        model.addAttribute("maxMemory", maxMemory);
        model.addAttribute("useableMemory", useableMemory);
        return "page_index";
    }

    @RequestMapping("/footer.jhtml")
    public String footer(Model model) {
        return "page_footer";
    }
}