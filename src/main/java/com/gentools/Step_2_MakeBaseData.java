package com.gentools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.common.annotation.ClientLanguage;
import com.dodo.common.annotation.action.DodoActionType;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.menu.DodoMenus;
import com.dodo.common.annotation.right.DodoButtonRight;
import com.dodo.common.annotation.right.DodoButtonRightEvent;
import com.dodo.common.annotation.right.DodoButtonRights;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.annotation.right.DodoRowRights;
import com.dodo.common.annotation.tree.DodoTreeRef;
import com.dodo.common.enums.EnumInterface;
import com.dodo.generate.config.DodoAction2DodoActionTypes;
import com.dodo.generate.config.DodoFrameworkGenerateConfigUtil.DodoGenerateCodeUtil;
import com.dodo.generate.hibernate.DodoColumnType;
import com.dodo.generate.hibernate.HibernateConfigUtil;
import com.dodo.generate.hibernate.property.DodoProperty;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.admin_1.base_1.Role;
import com.dodo.privilege.entity.admin_1.config_5.ExtendModel;
import com.dodo.privilege.entity.admin_1.config_5.Field;
import com.dodo.privilege.entity.admin_1.config_5.FieldRight;
import com.dodo.privilege.entity.admin_1.config_5.MenuInfo;
import com.dodo.privilege.entity.admin_1.config_5.Right;
import com.dodo.privilege.enums.ExtendModelFieldType;
import com.dodo.privilege.enums.FieldRightType;
import com.dodo.utils.CommonUtil;
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
public class Step_2_MakeBaseData {
    public static void main(String[] args) throws Exception {
        MakeBaseDataUtil.makeBaseData();
    }
}

class MakeBaseDataUtil {
    public static HibernateConfigUtil hibernateConfigUtil;
    private static final Logger       LOGGER     = LoggerFactory.getLogger(MakeBaseDataUtil.class);
    private static List<Boolean>      menuChange = new ArrayList<Boolean>();

    private static Integer getNextMenuCode(Session session, DodoMenuLevel level, Integer defaultCode) {
        Object maxValue = session
                .createQuery(
                        "select max(t.menuCode) from MenuInfo t where t.menuLevel=:menuLevel and t.menuCode like '"
                                + String.valueOf(defaultCode).substring(0, 1) + "%'").setParameter("menuLevel", level)
                .uniqueResult();
        if (maxValue != null) {
            return new BigDecimal(maxValue.toString()).add(new BigDecimal("1")).intValue();
        } else {
            return defaultCode;
        }
    }

    private static Integer getNextFieldRightCode(Session session, Integer defaultCode) {
        Object maxValue = session.createQuery(
                "select max(t.rightCode) from FieldRight t where t.rightCode like '"
                        + String.valueOf(defaultCode).substring(0, 1) + "%'").uniqueResult();

        if (maxValue != null) {
            return new BigDecimal(maxValue.toString()).add(new BigDecimal("1")).intValue();
        } else {
            return defaultCode;
        }
    }

    private static Integer getNextRightCode(Session session, Integer defaultCode) {
        Object maxValue = session.createQuery(
                "select max(t.rightCode) from Right t where t.rightCode like '"
                        + String.valueOf(defaultCode).substring(0, 1) + "%'").uniqueResult();

        if (maxValue != null) {
            return new BigDecimal(maxValue.toString()).add(new BigDecimal("1")).intValue();
        } else {
            return defaultCode;
        }
    }

    private static MenuInfo saveMenu(Session session, DodoMenu menu, Integer menuCode, String menuLink,
            MenuInfo parentmMenuInfo) {
        Query<?> query = null;
        String menuName = null;
        if (StringUtils.isNotBlank(menu.nameKey())) {
            query = session
                    .createQuery("from MenuInfo m where m.menuName = ''  and m.menuNameKey=:menuNameKey and menuLevel=:menuLevel and parentMenuInfo=:parentMenuInfo");
            query.setParameter("menuNameKey", menu.nameKey());
            menuName = hibernateConfigUtil.getPrivilegeMessage(menu.nameKey());
        } else {
            query = session
                    .createQuery("from MenuInfo m where m.menuName=:menuName  and m.menuNameKey  = ''  and menuLevel=:menuLevel and parentMenuInfo=:parentMenuInfo");
            query.setParameter("menuName", menu.name());
            menuName = menu.name();
        }
        DodoMenuLevel[] allLevels = { DodoMenuLevel.LEVEL0, DodoMenuLevel.LEVEL1, DodoMenuLevel.LEVEL2,
                DodoMenuLevel.LEVEL3 };
        DodoMenuLevel currLevel = allLevels[parentmMenuInfo.getMenuLevel().getSeq()];
        MenuInfo menuInfo = (MenuInfo) query.setParameter("menuLevel", currLevel)
                .setParameter("parentMenuInfo", parentmMenuInfo).uniqueResult();
        if (menuInfo == null) {
            menuChange.add(Boolean.TRUE);
            menuInfo = new MenuInfo();
            menuInfo.setMenuCode(menuCode + "");
            menuInfo.setMenuLevel(currLevel);
            menuInfo.setMenuLink(menuLink);
            if (StringUtils.isNotBlank(menu.nameKey())) {
                menuInfo.setMenuNameKey(menu.nameKey());
                menuInfo.setMenuName("");
            } else {
                menuInfo.setMenuNameKey("");
                menuInfo.setMenuName(menu.name());
            }
            menuInfo.setMenuRemark("");
            menuInfo.setModifyDate(new Date());
            menuInfo.setSortSeq(menu.sortSeq());
            menuInfo.setCreateDate(new Date());
            menuInfo.setParentMenuInfo(parentmMenuInfo);
            session.save(menuInfo);
            LOGGER.info(menuName + " is Null, Make a new one....");
        } else {
            menuInfo.setSortSeq(menu.sortSeq());
            session.update(menuInfo);
            menuChange.add(Boolean.FALSE);
            LOGGER.info(menuName + " is exists.. ");
        }
        return (MenuInfo) query.setParameter("menuLevel", currLevel).setParameter("parentMenuInfo", parentmMenuInfo)
                .uniqueResult();
    }

    private static MenuInfo addRootMenu(Session session) {
        MenuInfo rootMenu = (MenuInfo) session.createQuery("from MenuInfo a where a.menuCode = '000000'")
                .uniqueResult();
        if (rootMenu == null) {
            rootMenu = new MenuInfo();
            rootMenu.setMenuCode("000000");
            rootMenu.setMenuLevel(DodoMenuLevel.LEVEL0);
            rootMenu.setMenuLink("");
            rootMenu.setMenuNameKey("dodo.privilege.root.menuNameKey");
            rootMenu.setModifyDate(new Date());
            rootMenu.setSortSeq(0);
            rootMenu.setCreateDate(new Date());
            session.save(rootMenu);
            LOGGER.info("RootMenu is Null, Make a new one....");
        } else {
            LOGGER.info("final RootMenu is " + rootMenu);
        }
        return rootMenu;
    }

    private static int initDodoButtonRights(Class<?> clazz, MenuInfo rootMenu, String basepath, Session session,
            MenuInfo menuInfoThree, DodoEntity dodoEntity, int rightCode) {
        int rightCodeLocal = rightCode;

        DodoButtonRight[] buttonRights = null;
        if (clazz.isAnnotationPresent(DodoButtonRights.class)) {
            buttonRights = clazz.getAnnotation(DodoButtonRights.class).value();
        } else if (clazz.isAnnotationPresent(DodoButtonRight.class)) {
            buttonRights = new DodoButtonRight[] { clazz.getAnnotation(DodoButtonRight.class) };
        }

        if (buttonRights != null) {
            String buttonPath = null;
            String buttonName = "";
            String rightLink = null;
            for (DodoButtonRight buttonRight : buttonRights) {
                rightLink = null;
                buttonName = buttonRight.name();
                if (StringUtils.isNotBlank(buttonRight.nameKey())) {
                    buttonName = hibernateConfigUtil.getPrivilegeMessage(buttonRight.nameKey());
                }
                buttonPath = buttonRight.path();
                if (buttonRight.event() == DodoButtonRightEvent.AJAX) {
                    buttonPath = ("/" + buttonPath).replaceAll("[/]{2,}", "/");
                    buttonPath = (buttonPath.endsWith("/") ? buttonPath.substring(0, buttonPath.length() - 1)
                            : buttonPath) + ".jhtml";
                    rightLink = rootMenu.getMenuLink() + basepath + buttonPath;
                } else if (buttonPath.startsWith("${rootPath}")) {
                    rightLink = buttonPath.replace("${rootPath}", rootMenu.getMenuLink());
                } else {
                    continue;
                }
                Right rightButton = (Right) session.createQuery("from Right r where r.rightLink=:rightLink")
                        .setParameter("rightLink", rightLink).uniqueResult();
                if (rightButton == null) {
                    rightButton = (Right) session.createQuery("from Right r where r.rightLink=:rightLink")
                            .setParameter("rightLink", rightLink + "?clientlang=").uniqueResult();
                }
                // 新一个
                if (rightButton == null) {
                    rightButton = new Right();
                    rightButton.setClassName(clazz.getName());
                    rightButton.setCreateDate(new Date());
                    rightButton.setMenuInfo(menuInfoThree);
                    rightButton.setModifyDate(new Date());
                    rightButton.setRightCode(rightCodeLocal++ + "");
                    rightButton.setRightLink(rightLink);
                    rightButton.setIsSystem(Boolean.TRUE);
                    if (StringUtils.isNotBlank(buttonRight.nameKey())) {
                        rightButton.setRightKey(buttonRight.nameKey());
                    } else {
                        rightButton.setRightName(buttonRight.name());
                    }
                    if (StringUtils.isNotBlank(dodoEntity.nameKey())) {
                        rightButton.setEntityKey(dodoEntity.nameKey());
                    } else {
                        rightButton.setEntityName(dodoEntity.name());
                    }
                    rightButton.setSortSeq(0);
                    session.save(rightButton);
                    LOGGER.info(clazz.getSimpleName() + "_" + buttonName + " is Null, Make a new one....");
                }
                // 游离权限
                else if (rightButton.getMenuInfo() == null) {
                    rightButton.setClassName(clazz.getName());
                    rightButton.setCreateDate(new Date());
                    rightButton.setMenuInfo(menuInfoThree);
                    rightButton.setModifyDate(new Date());
                    rightButton.setRightCode(rightCodeLocal++ + "");
                    rightButton.setRightLink(rightLink);
                    rightButton.setIsSystem(Boolean.TRUE);
                    if (StringUtils.isNotBlank(buttonRight.nameKey())) {
                        rightButton.setRightKey(buttonRight.nameKey());
                    } else {
                        rightButton.setRightName(buttonRight.name());
                    }
                    if (StringUtils.isNotBlank(dodoEntity.nameKey())) {
                        rightButton.setEntityKey(dodoEntity.nameKey());
                    } else {
                        rightButton.setEntityName(dodoEntity.name());
                    }
                    rightButton.setSortSeq(0);
                    session.update(rightButton);
                    LOGGER.info(clazz.getSimpleName() + "_" + buttonName + " is Not Null, Update It....");
                }
                // 已经被别的菜单配置过，说明是重复的
                else if (!rightButton.getMenuInfo().equals(menuInfoThree)) {
                    throw new RuntimeException("Duplicate ButtonRight Path:" + buttonRight.path());
                } else {
                    LOGGER.info(clazz.getSimpleName() + "_" + buttonName + " is exists.. ");
                }
            }
        }
        return rightCodeLocal;
    }

    private static int initCommonRights(Class<?> clazz, List<DodoActionType> myTypes, MenuInfo rootMenu,
            String basepath, Session session, ClientLanguage clientLanguage, MenuInfo menuInfoThree, int rightCode,
            MenuInfo menuInfoOne, DodoEntity dodoEntity) {
        int rightCodeLocal = rightCode;
        DodoActionType[] allActTypes = DodoActionType.values();
        Right currManagerRight = null;
        for (DodoActionType type : allActTypes) {
            if (type == DodoActionType.ALL) {
                continue;
            }
            if (!myTypes.contains(type) && !myTypes.contains(DodoActionType.ALL)) {
                continue;
            }
            String rightLink = null;
            if (StringUtils.isBlank(type.getLink())) {
                rightLink = rootMenu.getMenuLink() + basepath + "/" + type.name();
            } else {
                rightLink = rootMenu.getMenuLink()
                        + basepath
                        + type.getLink().replace("${clientlanguage}",
                                clientLanguage == null ? "" : clientLanguage.value());
            }
            Right right0 = (Right) session.createQuery("from Right r where r.rightLink=:rightLink")
                    .setParameter("rightLink", rightLink).uniqueResult();
            if (right0 == null) {
                right0 = new Right();
                right0.setClassName(clazz.getName());
                right0.setCreateDate(new Date());
                right0.setMenuInfo(menuInfoThree);
                right0.setModifyDate(new Date());
                right0.setRightCode(rightCodeLocal++ + "");
                right0.setRightLink(rightLink);
                right0.setRightKey(type.getName());
                right0.setIsSystem(Boolean.TRUE);
                if (!type.getIsManager()) {
                    right0.setManagerRight(currManagerRight);
                }
                if (StringUtils.isNotBlank(dodoEntity.nameKey())) {
                    right0.setEntityKey(dodoEntity.nameKey());
                } else {
                    right0.setEntityName(dodoEntity.name());
                }
                right0.setRightRemark(dodoEntity.remark());
                right0.setSortSeq(0);
                session.save(right0);

                if (type == DodoActionType.LIST) {
                    if (clientLanguage == null) {
                        menuInfoThree.setMenuLink(StringUtils.substringBefore(rightLink, "?"));
                    } else {
                        menuInfoThree.setMenuLink(rightLink.replace("${clientlanguage}", clientLanguage.value()));
                    }
                    session.update(menuInfoOne);
                }

                LOGGER.info(clazz.getSimpleName() + "_" + hibernateConfigUtil.getMessage(type.getName())
                        + " is Null, Make a new one....");
            } else {
                LOGGER.info(clazz.getSimpleName() + "_" + hibernateConfigUtil.getMessage(type.getName())
                        + " is exists.. ");
            }
            if (type.getIsManager()) {
                currManagerRight = right0;
            }
        }
        return rightCodeLocal;
    }

    private static com.dodo.privilege.entity.admin_1.config_5.Entity initEntityInfo(Session session, String basepath,
            Class<?> clazz, MenuInfo menuInfoOne, MenuInfo menuInfoTwo, MenuInfo menuInfoThree, DodoEntity dodoEntity) {
        com.dodo.privilege.entity.admin_1.config_5.Entity entity = (com.dodo.privilege.entity.admin_1.config_5.Entity) session
                .createQuery("from Entity r where r.className=:className").setParameter("className", clazz.getName())
                .uniqueResult();
        if (entity == null) {
            entity = new com.dodo.privilege.entity.admin_1.config_5.Entity();
            entity.setActionName(DodoGenerateCodeUtil.actionBasePackage + HibernateConfigUtil.getShortPackage(clazz)
                    + "." + clazz.getSimpleName() + "Action");
            entity.setActionType(DodoAction2DodoActionTypes.transfer(dodoEntity.actions()).toString());
            entity.setBasePath(basepath);
            entity.setClassName(clazz.getName());
            entity.setCreateDate(new Date());
            entity.setDaoName(DodoGenerateCodeUtil.daoBasePackage + HibernateConfigUtil.getShortPackage(clazz) + "."
                    + clazz.getSimpleName() + "Dao");
            entity.setMenuInfoPath(menuInfoOne.getMenuName() + "-" + menuInfoTwo.getMenuName() + "-"
                    + menuInfoThree.getMenuName());
            entity.setModifyDate(new Date());
            if (StringUtils.isNotBlank(dodoEntity.nameKey())) {
                entity.setShowKey(dodoEntity.nameKey());
            } else {
                entity.setName(dodoEntity.name());
            }
            entity.setServiceName(DodoGenerateCodeUtil.serviceBasePackage + HibernateConfigUtil.getShortPackage(clazz)
                    + "." + clazz.getSimpleName() + "Service");
            entity.setSortSeq(0);
            entity.setMenuInfo(menuInfoThree);

            if (clazz.isAnnotationPresent(DodoTreeRef.class)) {
                DodoTreeRef treeRef = clazz.getAnnotation(DodoTreeRef.class);
                entity.setWholeTreeMethod(StringUtils.isBlank(treeRef.wholeTreeMethod()) ? "get"
                        + clazz.getSimpleName() + "WholeTree" : treeRef.wholeTreeMethod());
            }
            entity.setIsModelExtend(Boolean.FALSE);
            session.save(entity);
        }

        entity.setIsModelExtend(Boolean.FALSE);
        session.update(entity);
        return entity;
    }

    private static int initFields(HibernateConfigUtil hibernateConfigUtil, Class<?> clazz, Session session,
            com.dodo.privilege.entity.admin_1.config_5.Entity entity, List<DodoActionType> myTypes, int fieldRightCode)
            throws Exception {
        int fieldRightCodeLocal = fieldRightCode;
        Object[] objs = hibernateConfigUtil.getProperties(clazz.getName());
        @SuppressWarnings("unchecked")
        List<DodoProperty> propertyList = (List<DodoProperty>) objs[0];

        for (Iterator<?> iterator = propertyList.iterator(); iterator.hasNext();) {
            DodoProperty dodoProperty = (DodoProperty) iterator.next();
            Field field = (Field) session
                    .createQuery("from Field r where r.entity=:entity and r.propertyName=:propertyName")
                    .setParameter("entity", entity).setParameter("propertyName", dodoProperty.getPropertyName())
                    .uniqueResult();
            if (field == null) {
                field = new Field();
            }
            if (dodoProperty.getColumnType() == DodoColumnType.MAP) {
                entity.setIsModelExtend(Boolean.TRUE);
                session.update(entity);
            }
            field.setAddable(dodoProperty.getAddable());
            field.setCheckMethod(dodoProperty.getCheckMethod());
            field.setColumnType(dodoProperty.getColumnType().toString());
            field.setCreateDate(new Date());
            field.setEditable(dodoProperty.getEditable());
            field.setFieldSortSeq(dodoProperty.getSortSeq());
            field.setFieldType(dodoProperty.getFieldType() == null ? null : dodoProperty.getFieldType().getName());
            field.setFileType(dodoProperty.getFileType());
            field.setGenerateClass(dodoProperty.getGenerateClass() == null ? null : dodoProperty.getGenerateClass()
                    .getName());
            field.setGenerateMethodName(dodoProperty.getGenerateMethodName());
            field.setInfoTip(CommonUtil.escapeHtml(dodoProperty.getInfoTip()));
            field.setIsCreditcard(dodoProperty.getIsCreditcard());
            field.setIsDigits(dodoProperty.getIsDigits());
            field.setIsDoc(dodoProperty.getIsDoc());
            field.setIsEmail(dodoProperty.getIsEmail());
            field.setIsFile(dodoProperty.getIsFile());
            field.setIsIp(dodoProperty.getIsIp());
            field.setIsMobile(dodoProperty.getIsMobile());
            field.setIsNullable(dodoProperty.getIsNullable());
            field.setIsNumber(dodoProperty.getIsNumber());
            field.setIspassword(dodoProperty.getIspassword());
            field.setIsUrl(dodoProperty.getIsUrl());
            field.setIsUsername(dodoProperty.getIsUsername());
            field.setListable(dodoProperty.getListable());
            field.setMaxValue(dodoProperty.getMax());
            field.setMaxLength(dodoProperty.getMaxLength());
            field.setMinValue(dodoProperty.getMin());
            field.setMinLength(dodoProperty.getMinLength());
            field.setModifyDate(new Date());
            field.setPropertyName(dodoProperty.getPropertyName());
            field.setShowName(CommonUtil.escapeHtml(dodoProperty.getShowName()));
            field.setSortSeq(0);
            field.setTreeSrvName(dodoProperty.getTreeBaseSrvName() == null ? null : StringUtils.capitalize(dodoProperty
                    .getTreeBaseSrvName()) + "Service");
            field.setTreeMethodName(dodoProperty.getTreeMethodName());
            field.setTreeTargetClass(dodoProperty.getTreeTargetClass() == null ? null : dodoProperty
                    .getTreeTargetClass().getName());
            field.setIsCascade(dodoProperty.getIsCascade());
            field.setCascadeString(dodoProperty.getCascadeString().replaceAll("\\\\", ""));
            field.setQueryParams(CommonUtil.escapeHtml(dodoProperty.getQueryParams()));
            field.setIsVideo(dodoProperty.getIsVideo());
            field.setEntity(entity);
            field.setDateFormat(dodoProperty.getDateFormat());
            field.setIsDetailView(dodoProperty.getIsDetailView());
            field.setIsDetailEdit(dodoProperty.getIsDetailEdit());
            field.setIsShowWholeTree(dodoProperty.getIsShowWholeTree());
            field.setIsPopup(dodoProperty.getIsPopup());
            field.setSingleModel(dodoProperty.getSingleModel());
            field.setIsEncode(dodoProperty.getIsEncode());
            field.setFieldReturnClass(dodoProperty.getFieldReturnClass() == null ? "" : dodoProperty
                    .getFieldReturnClass().getName());
            field.setIsLocation(dodoProperty.getIsLocation());
            field.setIsColor(dodoProperty.getIsColor());
            field.setPicWidth(dodoProperty.getPicWidth());
            field.setPicHeight(dodoProperty.getPicHeight());
            field.setExtAttrJson(CommonUtil.escapeHtml(dodoProperty.getExtAttrJson()));
            field.setMaxFileSize(dodoProperty.getMaxFileSize());
            field.setIsSuppBreakpoint(dodoProperty.getIsSuppBreakpoint());
            field.setShowNameKey(dodoProperty.getShowNameKey());
            field.setOssBucket(dodoProperty.getOssBucket());
            field.setRegExp(dodoProperty.getRegExpDb());
            field.setRegExpTip(dodoProperty.getRegExpTip());
            field.setRegExpTipKey(dodoProperty.getRegExpTipKey());
            session.saveOrUpdate(field);

            if ((myTypes.contains(DodoActionType.ALL) || myTypes.contains(DodoActionType.TOUPDATE))
                    && dodoProperty.getEditable()) {
                if (session.createQuery("from FieldRight r where r.field=:field and r.fieldRightType=:fieldRightType")
                        .setParameter("field", field).setParameter("fieldRightType", FieldRightType.UPDATE)
                        .uniqueResult() == null) {
                    FieldRight fieldRightUpdate = new FieldRight();
                    fieldRightUpdate.setCreateDate(new Date());
                    fieldRightUpdate.setField(field);
                    fieldRightUpdate.setModifyDate(new Date());
                    fieldRightUpdate.setRightCode((fieldRightCodeLocal++) + "");
                    fieldRightUpdate.setFieldRightType(FieldRightType.UPDATE);
                    fieldRightUpdate.setSortSeq(0);
                    session.save(fieldRightUpdate);
                }
            }

            if (session.createQuery("from FieldRight r where r.field=:field and r.fieldRightType=:fieldRightType")
                    .setParameter("field", field).setParameter("fieldRightType", FieldRightType.VIEW).uniqueResult() == null) {
                FieldRight fieldRightView = new FieldRight();
                fieldRightView.setCreateDate(new Date());
                fieldRightView.setField(field);
                fieldRightView.setModifyDate(new Date());
                fieldRightView.setRightCode((fieldRightCodeLocal++) + "");
                fieldRightView.setFieldRightType(FieldRightType.VIEW);
                fieldRightView.setSortSeq(0);
                session.save(fieldRightView);
            }
        }
        return fieldRightCodeLocal;
    }

    private static void initExtendModel(List<Class<?>> classList, Session session) throws SecurityException,
            NoSuchFieldException {
        for (Class<?> clazz : classList) {
            if ((!clazz.isAnnotationPresent(DodoMenus.class)) || (!clazz.isAnnotationPresent(DodoEntity.class))) {
                continue;
            }

            DodoRowRight[] rowRights = null;
            if (clazz.isAnnotationPresent(DodoRowRights.class)) {
                rowRights = clazz.getAnnotation(DodoRowRights.class).value();
            } else if (clazz.isAnnotationPresent(DodoRowRight.class)) {
                rowRights = new DodoRowRight[] { clazz.getAnnotation(DodoRowRight.class) };
            }

            if (rowRights != null) {
                for (DodoRowRight right : rowRights) {
                    if (!"this".equals(right.entityProperty().trim())) {
                        java.lang.reflect.Field field = clazz.getDeclaredField(right.entityProperty().trim());
                        if (!CommonUtil.isBaseEntity(field.getType())) {
                            if (session
                                    .createQuery(
                                            "select t from ExtendModel t join t.entity e where e.className=:className and t.extFieldName=:extFieldName")
                                    .setParameter("extFieldName", right.principalKey())
                                    .setParameter("className", Role.class.getName()).uniqueResult() != null) {
                                continue;
                            }
                            ExtendModel extendModel = new ExtendModel();
                            extendModel.setEntity((com.dodo.privilege.entity.admin_1.config_5.Entity) session
                                    .createQuery("from Entity r where r.className=:className")
                                    .setParameter("className", Role.class.getName()).uniqueResult());
                            extendModel.setExtFieldName(right.principalKey());
                            if (CommonUtil.isEnumInterface(field.getType())) {
                                extendModel.setFieldType(ExtendModelFieldType.CHECKBOX);
                                StringBuilder valueList = new StringBuilder();
                                StringBuilder labelList = new StringBuilder();
                                EnumInterface[] objs = (EnumInterface[]) field.getType().getEnumConstants();
                                for (EnumInterface obj : objs) {
                                    valueList.append(obj.name()).append(",");
                                    labelList.append(
                                            StringUtils.isBlank(obj.getNameKey()) ? obj.getName() : obj.getNameKey())
                                            .append(",");
                                }
                                extendModel.setLabelList(labelList.deleteCharAt(labelList.length() - 1).toString());
                                extendModel.setValueList(valueList.deleteCharAt(valueList.length() - 1).toString());
                            } else if (field.getType() == Boolean.class) {
                                extendModel.setFieldType(ExtendModelFieldType.CHECKBOX);
                                extendModel.setLabelList("dodo.common.yes,dodo.common.no");
                                extendModel.setValueList("true,false");
                            } else {
                                extendModel.setFieldType(ExtendModelFieldType.STRING);
                                extendModel.setInfoTipKey("dodo.infotip.rowrights.message");
                            }
                            extendModel.setExtShowName(right.principalKeyShowName());
                            extendModel.setExtShowNameKey(right.principalKeyShowNameKey());
                            extendModel.setAddable(Boolean.TRUE);
                            extendModel.setEditable(Boolean.TRUE);
                            extendModel.setNullable(Boolean.TRUE);
                            extendModel.setMinLength(null);
                            extendModel.setMaxLength(null);
                            extendModel.setIsEmail(Boolean.FALSE);
                            extendModel.setIsMobile(Boolean.FALSE);
                            extendModel.setIsUrl(Boolean.FALSE);
                            extendModel.setIsCreditcard(Boolean.FALSE);
                            extendModel.setIsIp(Boolean.FALSE);
                            extendModel.setMaxValue(null);
                            extendModel.setMinValue(null);
                            extendModel.setInfoTip(null);
                            extendModel.setMaxFileSize(null);
                            extendModel.setFileExts(null);
                            extendModel.setCreateDate(new Date());
                            extendModel.setModifyDate(new Date());
                            extendModel.setSortSeq(0);
                            session.save(extendModel);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<Role> initRole(Session session) {
        List<Right> allRights = (List<Right>) session.createQuery("from Right r where r.managerRight is null").list();
        Set<Right> set = new HashSet<Right>();
        for (Right r : allRights) {
            set.add(r);
        }

        List<FieldRight> fieldRights = session.createQuery("from FieldRight").list();
        Set<FieldRight> ffset = new HashSet<FieldRight>();
        for (FieldRight r : fieldRights) {
            ffset.add(r);
        }

        Role role = (Role) session.createQuery("from Role r where r.isSystem=:isSystem")
                .setParameter("isSystem", Boolean.TRUE).uniqueResult();
        if (role == null) {
            role = new Role();
            role.setCreateDate(new Date());
            role.setDescription("系统管理员_拥有全部权限");
            role.setIsSystem(Boolean.TRUE);
            role.setModifyDate(new Date());
            role.setName("系统管理员");
            role.setSortSeq(0);
            role.setAllRights(set);
            role.setAllFieldRights(ffset);
            session.save(role);
            LOGGER.info("角色：系统管理员 is Null, Make a new one....");
        } else {
            role.setAllRights(set);
            role.setAllFieldRights(ffset);
            session.update(role);
            LOGGER.info("角色：系统管理员 is Not Null, Try Update it ....");
        }

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);
        return roles;
    }

    private static void initAdmin(Session session, Set<Role> roles) {
        Admin admin = (Admin) session.createQuery("from Admin a where a.name=:name").setParameter("name", "超级管理员")
                .uniqueResult();
        if (admin == null) {
            admin = new Admin();
            admin.setEmail("admin@admin.com");
            admin.setName("超级管理员");
            admin.setAdminPassword(DigestUtils.md5Hex("admin" + DodoCommonConfigUtil.passwordSalt));
            admin.setUsername("admin");
            admin.setRoleSet(roles);
            admin.setCreateDate(new Date());
            admin.setModifyDate(new Date());
            admin.onSave();
            session.save(admin);
            LOGGER.info("超级管理员 is Null, Make a new one....");
        } else {
            admin.setRoleSet(roles);
            session.update(admin);
            LOGGER.info("超级管理员 is Not Null, Try Update it ....");
        }
    }

    public static void makeBaseData() throws Exception {
        long beginTime = System.currentTimeMillis();
        hibernateConfigUtil = new HibernateConfigUtil();
        Session session = hibernateConfigUtil.getNewSession();
        Transaction transaction = session.beginTransaction();
        try {
            MenuInfo rootMenu = addRootMenu(session);
            int levelOne = getNextMenuCode(session, DodoMenuLevel.LEVEL1, 100001);
            int levelTwo = getNextMenuCode(session, DodoMenuLevel.LEVEL2, 200001);
            int levelThree = getNextMenuCode(session, DodoMenuLevel.LEVEL3, 300001);
            int rightCode = getNextRightCode(session, 400001);
            int fieldRightCode = getNextFieldRightCode(session, 500001);

            int levelOneOld = levelOne;
            int levelTwoOld = levelTwo;
            int levelThreeOld = levelThree;
            int rightCodeOld = rightCode;
            int fieldRightCodeOld = fieldRightCode;
            LOGGER.info("levelOne is " + levelOne);
            LOGGER.info("levelTwo is " + levelTwo);
            LOGGER.info("levelThree is " + levelThree);
            LOGGER.info("rightCode is " + rightCode);
            LOGGER.info("fieldRightCode is " + fieldRightCode);

            for (Class<?> clazz : hibernateConfigUtil.getEntityClassList()) {
                LOGGER.info(clazz + " ........ Process !");

                ClientLanguage clientLanguage = null;
                if (clazz.isAnnotationPresent(ClientLanguage.class)) {
                    clientLanguage = clazz.getAnnotation(ClientLanguage.class);
                    if (StringUtils.isBlank(clientLanguage.value())) {
                        clientLanguage = null;
                    }
                }
                DodoMenus dodoMenus = null;
                if (clazz.isAnnotationPresent(DodoMenus.class)) {
                    dodoMenus = clazz.getAnnotation(DodoMenus.class);
                } else {
                    continue;
                }

                DodoEntity dodoEntity = null;
                if (clazz.isAnnotationPresent(DodoEntity.class)) {
                    dodoEntity = clazz.getAnnotation(DodoEntity.class);
                } else {
                    continue;
                }

                DodoMenu menuOne = dodoMenus.levelOne();
                DodoMenu menuTwo = dodoMenus.levelTwo();
                DodoMenu menuThree = dodoMenus.levelThree();

                MenuInfo menuInfoOne = saveMenu(session, menuOne, ++levelOne, rootMenu.getMenuLink()
                        + "/framemenu/left_{0}.jhtml", rootMenu);
                if (!menuChange.get(menuChange.size() - 1)) {
                    --levelOne;
                }
                MenuInfo menuInfoTwo = saveMenu(session, menuTwo, ++levelTwo, "", menuInfoOne);
                if (!menuChange.get(menuChange.size() - 1)) {
                    --levelTwo;
                }
                MenuInfo menuInfoThree = saveMenu(session, menuThree, ++levelThree, "", menuInfoTwo);
                if (!menuChange.get(menuChange.size() - 1)) {
                    --levelThree;
                }

                List<DodoActionType> myTypes = new ArrayList<DodoActionType>();
                Set<DodoActionType> configTypes = DodoAction2DodoActionTypes.transfer(dodoEntity.actions());
                for (DodoActionType type : configTypes) {
                    myTypes.add(type);
                }
                String basepath = "/" + clazz.getSimpleName().toLowerCase();
                basepath = (basepath.endsWith("/") ? basepath.substring(0, basepath.length() - 1) : basepath);
                //button right
                rightCode = initDodoButtonRights(clazz, rootMenu, basepath, session, menuInfoThree, dodoEntity,
                        rightCode);

                // common right
                rightCode = initCommonRights(clazz, myTypes, rootMenu, basepath, session, clientLanguage,
                        menuInfoThree, rightCode, menuInfoOne, dodoEntity);

                // make entity data
                com.dodo.privilege.entity.admin_1.config_5.Entity entity = initEntityInfo(session, basepath, clazz,
                        menuInfoOne, menuInfoTwo, menuInfoThree, dodoEntity);

                // field 
                fieldRightCode = initFields(hibernateConfigUtil, clazz, session, entity, myTypes, fieldRightCode);
                LOGGER.info(clazz + " ........ Complete !");
            }

            //extend model
            initExtendModel(hibernateConfigUtil.getEntityClassList(), session);

            // admin & role
            initAdmin(session, initRole(session));

            transaction.commit();
            session.close();
            LOGGER.info("levelOne is " + levelOneOld + "-->" + levelOne);
            LOGGER.info("levelTwo is " + levelTwoOld + "-->" + levelTwo);
            LOGGER.info("levelThree is " + levelThreeOld + "-->" + levelThree);
            LOGGER.info("rightCode is " + rightCodeOld + "-->" + rightCode);
            LOGGER.info("fieldRightCode is " + fieldRightCodeOld + "-->" + fieldRightCode);
            LOGGER.info("");
            LOGGER.info("******************************************************");
            LOGGER.info(MakeBaseDataUtil.class.getSimpleName() + " ....Exec OK!");
            LOGGER.info("Take time -> " + CommonUtil.getOnlineTimeStr(new Date().getTime() - beginTime));
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            LOGGER.error("");
            LOGGER.error(MakeBaseDataUtil.class.getSimpleName() + " ....Exec Error!");
        } finally {
            session.close();
        }
        System.exit(0);
    }
}