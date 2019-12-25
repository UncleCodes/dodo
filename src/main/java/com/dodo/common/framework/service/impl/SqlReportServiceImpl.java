package com.dodo.common.framework.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.report.ReportFieldType;
import com.dodo.common.annotation.report.ReportQueryType;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.database.hql.HqlHelper.MatchType;
import com.dodo.common.framework.dao.HqlHelperDao;
import com.dodo.common.framework.service.SqlReportService;
import com.dodo.common.sqlreport.ReportDesignBean;
import com.dodo.common.sqlreport.SqlReportException;
import com.dodo.privilege.entity.admin_1.base_1.Role;
import com.dodo.privilege.entity.admin_1.config_5.MenuInfo;
import com.dodo.privilege.entity.admin_1.config_5.Right;
import com.dodo.privilege.entity.report_3.config_1.ReportEntity;
import com.dodo.privilege.entity.report_3.config_1.ReportField;
import com.dodo.privilege.entity.report_3.config_1.ReportMenu;
import com.dodo.security.DodoSecurityMetadataSource;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.SpringUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SqlReportServiceImpl implements SqlReportService {
    private HqlHelperDao helperDao;

    public HqlHelperDao getHelperDao() {
        return helperDao;
    }

    public void setHelperDao(HqlHelperDao helperDao) {
        this.helperDao = helperDao;
    }

    @Override
    public void saveOrUpdateReport(ReportDesignBean designBean, HttpServletRequest request) {
        ReportEntity reportEntity = null;
        ReportMenu reportMenu = helperDao.load(DodoFrameworkConfigUtil.getRightTypeIdValue(designBean.getMenuSELECT()),
                ReportMenu.class);

        HqlHelper helper = HqlHelper.queryFrom(MenuInfo.class);
        helper.eq("menuNameKey", "dodo.privilege.report.menuNameKey").eq("menuLevel", DodoMenuLevel.LEVEL1);
        MenuInfo rootReportMenu = helperDao.getEntity(helper);

        if (StringUtils.isNotBlank(designBean.getUpdateReportId())) {
            reportEntity = helperDao.get(DodoFrameworkConfigUtil.getRightTypeIdValue(designBean.getUpdateReportId()),
                    ReportEntity.class);
            List<ReportField> fields = reportEntity.getReportFields();
            reportEntity.setReportFields(null);
            for (ReportField field : fields) {
                helperDao.delete(field);
            }
        } else {
            reportEntity = new ReportEntity();
        }
        reportEntity.setMenu(reportMenu);
        reportEntity.setExecSql(designBean.getExecSql());
        reportEntity.setName(CommonUtil.escapeHtml(designBean.getName()));
        if (StringUtils.isNotBlank(designBean.getUpdateReportId())) {
            helperDao.update(reportEntity);
        } else {
            helperDao.save(reportEntity);
        }

        // 删除没有关联的菜单和权限
        MenuInfo reportEntityMenu = null;
        helper.resetQueryFrom(MenuInfo.class).eq("menuRemark", designBean.getMenuSELECT())
                .eq("menuLevel", DodoMenuLevel.LEVEL2);
        reportEntityMenu = helperDao.getEntity(helper);
        if (reportEntityMenu == null) {
            reportEntityMenu = new MenuInfo();
            reportEntityMenu.setMenuCode(getNextMenuCodeCode(DodoMenuLevel.LEVEL2));
            reportEntityMenu.setMenuLevel(DodoMenuLevel.LEVEL2);
            reportEntityMenu.setMenuLink("");
            reportEntityMenu.setMenuName(reportMenu.getMenuName());
            reportEntityMenu.setMenuRemark(designBean.getMenuSELECT());
            reportEntityMenu.setModifyDate(new Date());
            reportEntityMenu.setSortSeq(99);
            reportEntityMenu.setCreateDate(new Date());
            reportEntityMenu.setParentMenuInfo(rootReportMenu);
            helperDao.save(reportEntityMenu);
        } else {
            reportEntityMenu.setMenuName(reportMenu.getMenuName());
            helperDao.update(reportEntityMenu);
        }

        List<MenuInfo> clildMenuInfos = reportEntityMenu.getChildMenuInfo();
        if (clildMenuInfos != null && clildMenuInfos.size() > 0) {
            List<MenuInfo> tobeRemove = new ArrayList<MenuInfo>(clildMenuInfos.size());
            Serializable idLong = null;
            helper.resetQueryFrom(ReportEntity.class).fetch("id");
            List<Right> allRights = null;
            List<Role> allRoles = null;
            for (MenuInfo child : clildMenuInfos) {
                idLong = DodoFrameworkConfigUtil.getRightTypeIdValue(StringUtils.substringBefore(
                        StringUtils.substringAfterLast(child.getMenuLink(), "/"), "."));
                if (helperDao.getRecord(helper.resetQueryParameters().eq("id", idLong)) != null) {
                    continue;
                }
                allRights = child.getAllRights();
                if (allRights != null) {
                    child.setAllRights(null);
                    if (allRoles == null) {
                        allRoles = helperDao.getEntitys(Role.class);
                    }
                    for (Right right : allRights) {
                        for (Role role : allRoles) {
                            role.getAllRights().remove(right);
                        }
                        helperDao.delete(right);
                    }
                }
                tobeRemove.add(child);
            }
            for (MenuInfo menuInfo : tobeRemove) {
                clildMenuInfos.remove(menuInfo);
                helperDao.delete(menuInfo);
            }
        }

        // 插入当前菜单
        String menuLink = new StringBuilder().append("/sqlreport/report/").append(reportEntity.getId())
                .append(".jhtml").toString();
        String rightExcelLink = new StringBuilder().append("/sqlreport/reportexcel/").append(reportEntity.getId())
                .append(".jhtml").toString();
        helper.resetQueryFrom(MenuInfo.class).eq("menuLink", menuLink);
        MenuInfo menuInfo = helperDao.getEntity(helper);
        if (menuInfo == null) {
            menuInfo = new MenuInfo();
            menuInfo.setMenuLevel(DodoMenuLevel.LEVEL3);
            menuInfo.setMenuName(reportEntity.getName());
            menuInfo.setMenuLink(menuLink);
            menuInfo.setParentMenuInfo(reportEntityMenu);
            menuInfo.setMenuCode(getNextMenuCodeCode(DodoMenuLevel.LEVEL3));
            helperDao.save(menuInfo);
        } else {
            if (!menuInfo.getParentMenuInfo().equals(reportEntityMenu)) {
                menuInfo.setParentMenuInfo(reportEntityMenu);
            }
            menuInfo.setMenuName(reportEntity.getName());
            helperDao.update(menuInfo);
        }
        // 将当前菜单的权限打上临时备注
        helper.resetQueryFrom(Right.class).update("rightRemark", "Temp").eq("menuInfo", menuInfo);
        helperDao.update(helper);

        // 权限处理 - VIEW
        helper.resetQueryFrom(Right.class).eq("rightLink", menuLink).eq("menuInfo", menuInfo);
        Right right = helperDao.getEntity(helper);
        if (right == null) {
            right = new Right();
            right.setRightLink(menuLink);
            right.setRightName(reportEntity.getName() + "_VIEW");
            right.setMenuInfo(menuInfo);
            right.setRightCode(getNextRightCodeCode());
            helperDao.save(right);
        } else {
            right.setRightName(reportEntity.getName() + "_VIEW");
            right.setRightRemark(null);
            helperDao.update(right);
        }

        // 权限处理 - Excel
        helper.resetQueryFrom(Right.class).eq("rightLink", rightExcelLink).eq("menuInfo", menuInfo);
        right = helperDao.getEntity(helper);
        if (right == null) {
            right = new Right();
            right.setRightLink(rightExcelLink);
            right.setRightName(reportEntity.getName() + "_Excel");
            right.setMenuInfo(menuInfo);
            right.setRightCode(getNextRightCodeCode());
            helperDao.save(right);
        } else {
            right.setRightName(reportEntity.getName() + "_Excel");
            right.setRightRemark(null);
            helperDao.update(right);
        }

        // 字段Href权限
        String jumpLink = null;
        String jumpLinkRight = null;
        for (int i = 0; i < designBean.getFieldLabel().size(); i++) {
            ReportField reportField = new ReportField();
            reportField.setFieldType(ReportFieldType.valueOf(designBean.getFieldType().get(i)));
            reportField.setQueryField(CommonUtil.escapeHtml(designBean.getFieldName().get(i)));
            if (StringUtils.isNotBlank(designBean.getFieldQueryType().get(i))) {
                reportField.setQueryType(ReportQueryType.valueOf(designBean.getFieldQueryType().get(i)));
            }
            reportField.setReportEntity(reportEntity);
            reportField.setShowName(CommonUtil.escapeHtml(designBean.getFieldLabel().get(i)));
            reportField.setIsShow(designBean.getFieldIsShow().get(i));
            reportField.setSortSeq(i);

            jumpLink = designBean.getJumpLink().get(i);
            if (StringUtils.isNotBlank(jumpLink)) {
                jumpLink = jumpLink.trim();
                reportField.setJumpLink(jumpLink);
                // 后台菜单
                if (jumpLink.startsWith("{rootPath}")) {
                    jumpLinkRight = StringUtils.substringBeforeLast(jumpLink, "?");
                    jumpLinkRight = jumpLinkRight.replace("{rootPath}", "");

                    // 获得一个权限
                    helper.resetQueryFrom(Right.class).eq("rightLink", jumpLinkRight);
                    Right rightField = helperDao.getEntity(helper);
                    if (rightField == null) {
                        rightField = new Right();
                        rightField.setRightLink(jumpLinkRight);
                        rightField.setRightName(reportEntity.getName()
                                + "_DETAIL_"
                                + (StringUtils.isBlank(reportField.getShowName()) ? CommonUtil.escapeHtml(reportField
                                        .getQueryField()) : reportField.getShowName()));
                        rightField.setRightCode(getNextRightCodeCode());
                        rightField.setMenuInfo(menuInfo);
                        helperDao.save(rightField);
                    } else if (rightField.getMenuInfo() == null) {
                        rightField.setRightName(reportEntity.getName()
                                + "_DETAIL_"
                                + (StringUtils.isBlank(reportField.getShowName()) ? CommonUtil.escapeHtml(reportField
                                        .getQueryField()) : reportField.getShowName()));
                        rightField.setMenuInfo(menuInfo);
                        rightField.setRightRemark(null);
                        helperDao.update(rightField);
                    } else if (((Object) rightField.getMenuInfo().getId()).equals((Object) menuInfo.getId())) {
                        rightField.setRightRemark(null);
                        helperDao.update(rightField);
                    }
                    // 链接已经在别的菜单下存在
                    else {
                        throw new SqlReportException(SpringUtil.getMessageBack("dodo.sqlreport.duplicate.jumplink",
                                new Object[] { jumpLink }, request));
                    }
                }
            }
            helperDao.save(reportField);
        }

        // 更新失效权限
        helper.resetQueryFrom(Right.class).update("menuInfo", null).eq("rightRemark", "Temp").eq("menuInfo", menuInfo);
        helperDao.update(helper);

        DodoSecurityMetadataSource.refreshSysMetadata = Boolean.TRUE;
    }

    private String getNextRightCodeCode() {
        HqlHelper helper = HqlHelper.queryFrom(Right.class);
        helper.max("rightCode", "rightCode").like("rightCode", "9", MatchType.START);
        String maxValue = helperDao.getRecordGroup(helper).get("rightCode");
        if (maxValue == null) {
            return "900001";
        } else {
            return new java.math.BigDecimal(maxValue).add(new java.math.BigDecimal("1")).toString();
        }
    }

    private String getNextMenuCodeCode(DodoMenuLevel menuLevel) {
        HqlHelper helper = HqlHelper.queryFrom(MenuInfo.class);
        helper.max("menuCode", "menuCode");
        if (menuLevel == DodoMenuLevel.LEVEL1) {
            helper.like("menuCode", "6", MatchType.START);
        } else if (menuLevel == DodoMenuLevel.LEVEL2) {
            helper.like("menuCode", "7", MatchType.START);
        } else if (menuLevel == DodoMenuLevel.LEVEL3) {
            helper.like("menuCode", "8", MatchType.START);
        }

        String maxValue = helperDao.getRecordGroup(helper).get("menuCode");

        if (maxValue == null) {
            if (menuLevel == DodoMenuLevel.LEVEL1) {
                return "600001";
            } else if (menuLevel == DodoMenuLevel.LEVEL2) {
                return "700001";
            } else if (menuLevel == DodoMenuLevel.LEVEL3) {
                return "800001";
            }
            return null;
        } else {
            return new java.math.BigDecimal(maxValue).add(new java.math.BigDecimal("1")).toString();
        }
    }
}
