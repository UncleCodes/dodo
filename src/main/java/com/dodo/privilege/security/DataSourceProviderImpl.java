package com.dodo.privilege.security;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.database.data.Ite;
import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.config_5.MenuInfo;
import com.dodo.privilege.entity.admin_1.config_5.Right;
import com.dodo.security.DodoMetadataSourceProvider;

/**
 * 系统全部 资源与权限 元数据源 提供者 实现类
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DataSourceProviderImpl implements DodoMetadataSourceProvider {
    private String[]         sqlReportDesignLink = { "/dbmeta.jhtml", "/queryType.jhtml", "/fieldType.jhtml",
            "/query.jhtml", "/easyui/query.jhtml", "/condition/exec.jhtml", "/saveOrUpdate.jhtml" };
    private HqlHelperService hqlHelperService;
    private String           adminRootPath;

    public void setHqlHelperService(HqlHelperService hqlHelperService) {
        this.hqlHelperService = hqlHelperService;
    }

    public void setAdminRootPath(String adminRootPath) {
        this.adminRootPath = adminRootPath;
    }

    /**
     * 
     * 1、加载菜单的链接+Code<br/>
     * 2、加载权限的链接+Code，如果链接相同，覆盖掉菜单的Code<br/>
     * 3、报表的设计限特殊处理，添加其他额外的链接，公用同一个Code<br/>
     * */
    @Override
    public Map<String, Collection<ConfigAttribute>> loadConfigAttributeMap() {
        HqlHelper helper = HqlHelper.queryFrom(MenuInfo.class);
        helper.fetch("menuLink", "menuCode", "menuLevel", "id").ne("menuLink", "");

        Records allMenusInfos = hqlHelperService.getRecords(helper, Boolean.FALSE);

        Map<String, Collection<ConfigAttribute>> rMap = new HashMap<String, Collection<ConfigAttribute>>(
                allMenusInfos.size());
        Record record = null;
        Ite ite = allMenusInfos.iterator();
        String menuLink = null;
        while (ite.hasNext()) {
            record = ite.next();
            menuLink = record.get("menuLink");
            if (StringUtils.isBlank(menuLink)) {
                continue;
            }
            Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
            ConfigAttribute configAttribute = new SecurityConfig(record.get("menuCode").toString());
            configAttributes.add(configAttribute);
            if ((DodoMenuLevel) record.get("menuLevel") == DodoMenuLevel.LEVEL1) {
                rMap.put(MessageFormat.format(StringUtils.substringBefore(menuLink, "?"), record.get("id").toString()),
                        configAttributes);
            } else {
                rMap.put(StringUtils.substringBefore(menuLink, "?"), configAttributes);
            }
        }

        helper.resetQueryFrom(Right.class).fetch("rightCode", "rightLink").isNotNull("menuInfo").ne("rightLink", "");

        Records allrRights = hqlHelperService.getRecords(helper, Boolean.FALSE);
        String rightLink = null;
        String rightCode = null;
        record = null;
        ite = allrRights.iterator();
        while (ite.hasNext()) {
            record = ite.next();
            rightLink = StringUtils.substringBefore((String) record.get("rightLink"), "?");
            rightCode = record.get("rightCode");
            Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
            ConfigAttribute configAttribute = new SecurityConfig(rightCode);
            configAttributes.add(configAttribute);
            rMap.put(rightLink, configAttributes);

            if (rightLink.contains("/sqlreport/design.jhtml")) {
                String prefixPath = StringUtils.substringBeforeLast(rightLink, "/");
                for (String string : sqlReportDesignLink) {
                    Collection<ConfigAttribute> tempConfigAttributes = new ArrayList<ConfigAttribute>();
                    ConfigAttribute tempConfigAttribute = new SecurityConfig(rightCode);
                    tempConfigAttributes.add(tempConfigAttribute);
                    rMap.put(prefixPath + string, tempConfigAttributes);
                }
            }
        }

        Collection<ConfigAttribute> rootConfigAttributes = new ArrayList<ConfigAttribute>();
        ConfigAttribute rootConfigAttribute = new SecurityConfig("000000");
        rootConfigAttributes.add(rootConfigAttribute);
        rMap.put(getAdminRootPath(), rootConfigAttributes);

        // 清空内存 重新加载 hasRight 使用
        DodoSecurityServiceImpl.rights = null;
        return rMap;
    }

    @Override
    public String getAdminRootPath() {
        return adminRootPath;
    }
}
