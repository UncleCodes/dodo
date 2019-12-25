package com.dodo.privilege.security;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dodo.common.database.data.Ite;
import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.menu.DodoMenuExt;
import com.dodo.common.framework.bean.pager.PageModel.OrderType;
import com.dodo.common.framework.bean.tree.DodoTree;
import com.dodo.common.framework.bean.tree.DodoTreeNode;
import com.dodo.common.framework.dao.HqlHelperDao;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.admin_1.config_5.FieldRight;
import com.dodo.privilege.entity.admin_1.config_5.MenuInfo;
import com.dodo.privilege.entity.admin_1.config_5.Right;
import com.dodo.privilege.enums.FieldRightType;
import com.dodo.privilege.listener.DodoHttpSessionListener;
import com.dodo.security.DodoPrincipalGrantedAuthoritie;
import com.dodo.security.DodoUserDetails;
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
@Service("dodoSecurityServiceImpl")
public class DodoSecurityServiceImpl implements DodoSecurityService {

    private static final Logger          LOGGER       = LoggerFactory.getLogger(DodoSecurityService.class);

    private static Map<String, String>   fieldRights  = null;
    protected static Map<String, String> rights       = null;
    private final static String          LIST_ACT_URL = "/list.jhtml?clientlang=";

    @Autowired
    private HqlHelperDao                 hqlHelperDao;

    @Override
    public UserDetails getLoginPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return (UserDetails) auth.getPrincipal();
        }
        return null;
    }

    public UserDetails getLoginPrincipal(SecurityContext context) {
        Authentication auth = context.getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return (UserDetails) auth.getPrincipal();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        HqlHelper helper = HqlHelper.queryFrom(Admin.class);
        helper.eq("username", userName);
        Admin localAdmin = hqlHelperDao.getEntity(helper);
        if (localAdmin == null) {
            throw new UsernameNotFoundException("The admin named [" + userName + "] doesn't exists!");
        }
        hqlHelperDao.evict(localAdmin);
        return refreshUser(localAdmin);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserDetails refreshUser(Admin localAdmin) throws UsernameNotFoundException {
        String contextPath = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getContextPath();

        localAdmin.setIsSystemAdmin(Boolean.FALSE);

        Set<String> haveRights = new HashSet<String>();
        Set<String> haveRightQuerys = new HashSet<String>();
        Set<String> haveFieldRights = new HashSet<String>();

        HqlHelper helper = HqlHelper.queryFrom(Admin.class);
        helper.join(HqlHelper.currTable, "roleSet", "r").fetchOther("r", "isSystem", "isSystem")
                .join("r", "allRights", "ri").isNotNull("ri", "menuInfo").isNull("ri", "managerRight")
                .fetchOther("ri", "rightCode", "rightCode").fetchOther("ri", "id", "rightId")
                .fetchOther("ri", "rightLink", "rightLink").fetchOther("ri", "isSystem", "isSystemRight")
                .eq("id", localAdmin.getId());
        String rightCode = null;
        String rightLink = null;
        Records adminPrivs = hqlHelperDao.getRecords(helper, Boolean.TRUE);
        Record record = null;
        Ite ite = adminPrivs.iterator();
        Set<Serializable> managerRightIds = new HashSet<Serializable>();
        while (ite.hasNext()) {
            record = ite.next();
            rightCode = record.get("rightCode");
            rightLink = record.get("rightLink");
            if ((Boolean) record.get("isSystem")) {
                localAdmin.setIsSystemAdmin(Boolean.TRUE);
            }
            haveRights.add(rightCode);

            if (StringUtils.isNotBlank(rightLink)
                    && ((!(Boolean) record.get("isSystemRight")) || rightLink.contains(LIST_ACT_URL))) {
                haveRightQuerys.add(rightCode);
            }
            managerRightIds.add((Serializable) record.get("rightId"));

            LOGGER.debug("Have Manager right--Key ['{}':'{}']", rightLink, rightCode);
        }

        if (managerRightIds.size() > 0) {
            helper.resetQueryFrom(Right.class).isNotNull("menuInfo").in("managerRight.id", managerRightIds)
                    .fetch("isSystem", "rightCode", "rightLink");
            adminPrivs = hqlHelperDao.getRecords(helper, Boolean.TRUE);
            ite = adminPrivs.iterator();
            while (ite.hasNext()) {
                record = ite.next();
                rightCode = record.get("rightCode");
                rightLink = record.get("rightLink");
                haveRights.add(rightCode);

                if (StringUtils.isNotBlank(rightLink) && (!(Boolean) record.get("isSystem"))) {
                    haveRightQuerys.add(rightCode);
                }
                LOGGER.debug("Have right--Key ['{}':'{}']", rightLink, rightCode);
            }
        }

        helper.resetQueryFrom(Admin.class).join(HqlHelper.currTable, "roleSet", "r").join("r", "allFieldRights", "fri")
                .fetchOther("fri", "rightCode", "fieldRightCode").fetchOther("fri", "id", "fieldRightId")
                .eq("id", localAdmin.getId());

        adminPrivs = hqlHelperDao.getRecords(helper, Boolean.TRUE);
        ite = adminPrivs.iterator();
        while (ite.hasNext()) {
            record = ite.next();
            haveFieldRights.add((String) record.get("fieldRightCode"));
        }

        List<DodoTreeNode> treeNodeList = new ArrayList<DodoTreeNode>();
        List<Object> menuIds = null;
        List<String> menuCodes = new ArrayList<String>();

        int currLevel = 4;
        Records records = null;
        String menuPid = null;
        String menuId = null;
        String menuCode = null;
        String menuName = null;
        String menuNameKey = null;
        String menuLink = null;
        Integer sortSeq = null;
        if (haveRightQuerys != null && haveRightQuerys.size() > 0) {
            helper.resetQueryFrom(Right.class)
                    .fetch("menuInfo.id", "menuInfo.menuName", "menuInfo.menuNameKey", "menuInfo.sortSeq",
                            "menuInfo.menuCode", "menuInfo.menuLink", "menuInfo.parentMenuInfo.id")
                    .in("rightCode", haveRightQuerys);

            records = hqlHelperDao.getRecords(helper, Boolean.TRUE);
            menuIds = new ArrayList<Object>(records.size());

            ite = records.iterator();
            while (ite.hasNext()) {
                record = ite.next();
                Serializable parentMenuId = record.get("menuInfo_parentMenuInfo_id");
                if (parentMenuId == null) {
                    menuPid = "-1";
                } else {
                    menuPid = parentMenuId.toString();
                }
                menuId = record.get("menuInfo_id").toString();
                menuName = record.get("menuInfo_menuName");
                menuNameKey = record.get("menuInfo_menuNameKey");
                menuLink = record.get("menuInfo_menuLink");
                menuCode = record.get("menuInfo_menuCode");
                sortSeq = record.get("menuInfo_sortSeq");
                DodoTreeNode treeNode = new DodoTreeNode(menuPid, "00000" + menuId, menuName, menuNameKey,
                        new DodoMenuExt(menuLink, MessageFormat.format(menuLink, menuId)));
                treeNode.setBusinessLevel(currLevel);
                treeNode.setUrl(contextPath + DodoCommonConfigUtil.viewRootPath + menuLink);
                treeNode.setTarget("mainFrame");
                treeNode.setSortSeq(sortSeq);
                treeNodeList.add(treeNode);
                menuIds.add(record.get("menuInfo_id"));
                menuCodes.add(menuCode);
            }
        }

        while (menuIds != null && menuIds.size() > 0) {
            --currLevel;
            helper.resetQueryFrom(MenuInfo.class)
                    .fetch("parentMenuInfo.id", "parentMenuInfo.menuName", "parentMenuInfo.menuNameKey",
                            "parentMenuInfo.sortSeq", "parentMenuInfo.menuCode", "parentMenuInfo.menuLink",
                            "parentMenuInfo.parentMenuInfo.id").in("id", menuIds)
                    .orderBy("parentMenuInfo.sortSeq", OrderType.asc);

            records = hqlHelperDao.getRecords(helper, Boolean.TRUE);
            menuIds = new ArrayList<Object>(records.size());
            ite = records.iterator();
            while (ite.hasNext()) {
                record = ite.next();
                Serializable parentParentMenuId = record.get("parentMenuInfo_parentMenuInfo_id");
                if (parentParentMenuId == null) {
                    menuPid = "-1";
                } else {
                    menuPid = parentParentMenuId.toString();
                }
                menuId = record.get("parentMenuInfo_id").toString();
                menuName = record.get("parentMenuInfo_menuName");
                menuNameKey = record.get("parentMenuInfo_menuNameKey");
                menuLink = record.get("parentMenuInfo_menuLink");
                menuCode = record.get("parentMenuInfo_menuCode");
                sortSeq = record.get("parentMenuInfo_sortSeq");
                DodoTreeNode treeNode = new DodoTreeNode(menuPid, menuId, menuName, menuNameKey, new DodoMenuExt(
                        menuLink, MessageFormat.format(menuLink, menuId)));
                treeNode.setBusinessLevel(currLevel);
                treeNode.setSortSeq(sortSeq);
                treeNodeList.add(treeNode);
                menuIds.add(record.get("parentMenuInfo_id"));
                menuCodes.add(menuCode);
            }
        }

        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        for (String menuAut : menuCodes) {
            list.add(new DodoPrincipalGrantedAuthoritie(menuAut));
        }

        for (String rc : haveRights) {
            list.add(new DodoPrincipalGrantedAuthoritie(rc));
        }

        for (String rc : haveFieldRights) {
            list.add(new DodoPrincipalGrantedAuthoritie(rc));
        }

        DodoTree dodoTree = new DodoTree(treeNodeList, "-1");
        dodoTree.setMaxBusinessLevel(4);
        dodoTree.sortTree();
        localAdmin.setAuthorities(list);
        localAdmin.setMenuInfoTree(dodoTree);
        localAdmin.setFieldRightHaveCode(haveFieldRights);
        localAdmin.setRightHaveCode(haveRights);
        return localAdmin;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public boolean hasFieldRight(String entityFullName, String fieldName, FieldRightType type) {
        return ((DodoUserDetails) getLoginPrincipal()).hasFieldRight(getFieldRightFromDb(entityFullName, fieldName,
                type));
    }

    private synchronized void loadAllFieldRightsFromDb() {
        if (fieldRights == null) {
            LOGGER.debug("fieldRights.size() 0. Initialization fieldRights ............");
            HqlHelper helper = HqlHelper.queryFrom(FieldRight.class);
            helper.fetch("field.entity.className", "field.propertyName", "fieldRightType", "rightCode");

            Records localFieldRights = hqlHelperDao.getRecords(helper, Boolean.FALSE);
            StringBuilder sbBuilderKey = new StringBuilder();
            fieldRights = new HashMap<String, String>(localFieldRights.size());
            Record record = null;
            Ite ite = localFieldRights.iterator();
            while (ite.hasNext()) {
                record = ite.next();
                sbBuilderKey.delete(0, sbBuilderKey.length());
                sbBuilderKey.append((Object) record.get("field_entity_className")).append("-")
                        .append((Object) record.get("field_propertyName")).append("-")
                        .append(((FieldRightType) record.get("fieldRightType")).name());

                LOGGER.debug("Put field right--Key ['{}':'{}']", sbBuilderKey, record.get("rightCode"));

                fieldRights.put(sbBuilderKey.toString(), record.get("rightCode").toString());
            }
            LOGGER.debug("Initialization fieldRights ............ OK ...");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private String getFieldRightFromDb(String entityFullName, String fieldName, FieldRightType type) {
        if (fieldRights == null) {
            loadAllFieldRightsFromDb();
        }

        String sBuilder = new StringBuilder(entityFullName).append("-").append(fieldName).append("-")
                .append(type.name()).toString();
        String gotFieldRight = fieldRights.get(sBuilder);
        LOGGER.debug("Query field right Key:{} , Got:{}", sBuilder, gotFieldRight);
        return gotFieldRight;
    }

    private synchronized void loadAllRightsFromDb() {
        if (rights == null) {
            LOGGER.debug("rights.size() 0. Initialization rights ............");
            HqlHelper helper = HqlHelper.queryFrom(Right.class);
            helper.fetch("rightLink", "rightCode").isNotNull("menuInfo").ne("rightLink", "");
            Records localRights = hqlHelperDao.getRecords(helper, Boolean.FALSE);
            rights = new HashMap<String, String>(localRights.size());
            Record record = null;
            Ite ite = localRights.iterator();
            while (ite.hasNext()) {
                record = ite.next();
                LOGGER.debug("Put right--Key ['{}':'{}']", record.get("rightLink"), record.get("rightCode"));
                rights.put(StringUtils.substringBefore((String) record.get("rightLink"), "?"), record.get("rightCode")
                        .toString());
            }
            LOGGER.debug("Initialization rights ............ OK ...");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private String getRightFromDb(String path) {
        if (rights == null) {
            loadAllRightsFromDb();
        }
        String gotRight = rights.get(StringUtils.substringBefore(path, "?"));
        LOGGER.debug("Query right Key:{} , Got:{}", path, gotRight);
        return gotRight;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public boolean hasRight(String path) {
        return ((DodoUserDetails) getLoginPrincipal()).hasRight(getRightFromDb(path));
    }

    @Override
    public void refreshCurrLoginAdmin() {
        List<String> invalidSessionId = new ArrayList<String>();
        Collection<HttpSession> backSessions = DodoHttpSessionListener.getAllLoginBackSessions().values();
        for (HttpSession session : backSessions) {
            SecurityContext securityContext = null;
            try {
                securityContext = (SecurityContext) session
                        .getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            } catch (IllegalStateException e) {
                invalidSessionId.add(session.getId());
                continue;
            }
            if (securityContext == null) {
                continue;
            }

            Admin admin = (Admin) getLoginPrincipal(securityContext);
            if (admin == null) {
                continue;
            }
            LOGGER.info("Refresh Admin Authority {}-> {}({})", session.getId(), admin.getUsername(), admin.getName());
            refreshUser(admin);
            Authentication auth = securityContext.getAuthentication();
            List<GrantedAuthority> newAuthorities = new ArrayList<>(admin.getAuthorities());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),
                    auth.getCredentials(), newAuthorities);
            securityContext.setAuthentication(newAuth);
        }
    }
}