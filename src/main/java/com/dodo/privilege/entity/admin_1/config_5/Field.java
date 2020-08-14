package com.dodo.privilege.entity.admin_1.config_5;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.tree.DodoTreeRef;
import com.dodo.common.framework.entity.BaseEntity;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@javax.persistence.Entity
@DynamicInsert
@DodoEntity(nameKey = "dodo.privilege.admin.config.Field.entityKey", actions = { DodoAction.VIEW, DodoAction.CHART,
        DodoAction.EXPORT }, levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.config.menuNameKey", sortSeq = 5), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.config.Field.menuNameKey", sortSeq = 4))
@DodoTreeRef(mapParentField = "entity")
public class Field extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -6099663981803044881L;
    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.config.Field.namekey.entity", queryOnList = true)
    private Entity            entity;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.config.Field.namekey.fieldSortSeq")
    private Integer           fieldSortSeq;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.config.Field.namekey.propertyName", queryOnList = true)
    @DodoShowColumn(sortSeq = 0)
    private String            propertyName;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.config.Field.namekey.showName", queryOnList = true)
    @DodoShowColumn(sortSeq = 1)
    private String            showName;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.config.Field.namekey.showNameKey")
    private String            showNameKey;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.config.Field.namekey.editable")
    private Boolean           editable;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.config.Field.namekey.addable")
    private Boolean           addable;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.config.Field.namekey.listable")
    private Boolean           listable;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.config.Field.namekey.ispassword")
    private Boolean           ispassword;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.config.Field.namekey.isNullable")
    private Boolean           isNullable;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.config.Field.namekey.columnType")
    private String            columnType;

    @DodoField(sortSeq = 10, nameKey = "dodo.privilege.admin.config.Field.namekey.fieldType")
    private String            fieldType;

    @DodoField(sortSeq = 11, nameKey = "dodo.privilege.admin.config.Field.namekey.generateMethodName")
    private String            generateMethodName;

    @DodoField(sortSeq = 12, nameKey = "dodo.privilege.admin.config.Field.namekey.generateClass")
    private String            generateClass;

    @DodoField(sortSeq = 13, nameKey = "dodo.privilege.admin.config.Field.namekey.infoTip")
    private String            infoTip;

    @DodoField(sortSeq = 14, nameKey = "dodo.privilege.admin.config.Field.namekey.minLength")
    private Integer           minLength;

    @DodoField(sortSeq = 15, nameKey = "dodo.privilege.admin.config.Field.namekey.maxLength")
    private Integer           maxLength;

    @DodoField(sortSeq = 16, nameKey = "dodo.privilege.admin.config.Field.namekey.isEmail")
    private Boolean           isEmail;

    @DodoField(sortSeq = 17, nameKey = "dodo.privilege.admin.config.Field.namekey.isMobile")
    private Boolean           isMobile;

    @DodoField(sortSeq = 18, nameKey = "dodo.privilege.admin.config.Field.namekey.checkMethod")
    private String            checkMethod;

    @DodoField(sortSeq = 19, nameKey = "dodo.privilege.admin.config.Field.namekey.isUsername")
    private Boolean           isUsername;

    @DodoField(sortSeq = 20, nameKey = "dodo.privilege.admin.config.Field.namekey.isUrl")
    private Boolean           isUrl;

    @DodoField(sortSeq = 21, nameKey = "dodo.privilege.admin.config.Field.namekey.isNumber")
    private Boolean           isNumber;

    @DodoField(sortSeq = 22, nameKey = "dodo.privilege.admin.config.Field.namekey.isDigits")
    private Boolean           isDigits;

    @DodoField(sortSeq = 23, nameKey = "dodo.privilege.admin.config.Field.namekey.isCreditcard")
    private Boolean           isCreditcard;

    @DodoField(sortSeq = 24, nameKey = "dodo.privilege.admin.config.Field.namekey.isIp")
    private Boolean           isIp;

    @DodoField(sortSeq = 25, nameKey = "dodo.privilege.admin.config.Field.namekey.maxValue")
    private Double            maxValue;

    @DodoField(sortSeq = 26, nameKey = "dodo.privilege.admin.config.Field.namekey.minValue")
    private Double            minValue;

    @DodoField(sortSeq = 27, nameKey = "dodo.privilege.admin.config.Field.namekey.isFile")
    private Boolean           isFile;

    @DodoField(sortSeq = 27, nameKey = "dodo.privilege.admin.config.Field.namekey.ossBucket")
    private String            ossBucket;

    @DodoField(sortSeq = 28, nameKey = "dodo.privilege.admin.config.Field.namekey.isDoc")
    private Boolean           isDoc;

    @DodoField(sortSeq = 28, nameKey = "dodo.privilege.admin.config.Field.namekey.isVideo")
    private Boolean           isVideo;

    @DodoField(sortSeq = 29, listable = false, nameKey = "dodo.privilege.admin.config.Field.namekey.fileType")
    private String            fileType;

    @DodoField(sortSeq = 30, nameKey = "dodo.privilege.admin.config.Field.namekey.treeSrvName")
    private String            treeSrvName;

    @DodoField(sortSeq = 31, nameKey = "dodo.privilege.admin.config.Field.namekey.treeMethodName")
    private String            treeMethodName;

    @DodoField(sortSeq = 33, nameKey = "dodo.privilege.admin.config.Field.namekey.treeTargetClass")
    private String            treeTargetClass;

    @DodoField(sortSeq = 34, listable = false, editable = false, nameKey = "dodo.privilege.admin.config.Field.namekey.allFieldRights")
    private List<FieldRight>  allFieldRights   = new ArrayList<FieldRight>();

    @DodoField(sortSeq = 35, nameKey = "dodo.privilege.admin.config.Field.namekey.isCascade")
    private Boolean           isCascade;

    @DodoField(sortSeq = 36, nameKey = "dodo.privilege.admin.config.Field.namekey.cascadeString")
    private String            cascadeString;

    @DodoField(sortSeq = 37, nameKey = "dodo.privilege.admin.config.Field.namekey.queryParams")
    private String            queryParams;

    @DodoField(sortSeq = 38, nameKey = "dodo.privilege.admin.config.Field.namekey.dateFormat")
    private String            dateFormat;

    @DodoField(sortSeq = 39, nameKey = "dodo.privilege.admin.config.Field.namekey.isDetailView")
    private Boolean           isDetailView;

    @DodoField(sortSeq = 40, nameKey = "dodo.privilege.admin.config.Field.namekey.isDetailEdit")
    private Boolean           isDetailEdit;

    @DodoField(sortSeq = 41, nameKey = "dodo.privilege.admin.config.Field.namekey.isShowWholeTree")
    private Boolean           isShowWholeTree;

    @DodoField(sortSeq = 42, nameKey = "dodo.privilege.admin.config.Field.namekey.isPopup")
    private Boolean           isPopup;

    @DodoField(sortSeq = 43, nameKey = "dodo.privilege.admin.config.Field.namekey.singleModel")
    private String            singleModel;

    @DodoField(sortSeq = 44, nameKey = "dodo.privilege.admin.config.Field.namekey.isEncode")
    private Boolean           isEncode;

    @DodoField(sortSeq = 45, nameKey = "dodo.privilege.admin.config.Field.namekey.fieldReturnClass")
    private String            fieldReturnClass;

    @DodoField(sortSeq = 46, nameKey = "dodo.privilege.admin.config.Field.namekey.isLocation")
    private Boolean           isLocation;

    @DodoField(sortSeq = 47, nameKey = "dodo.privilege.admin.config.Field.namekey.isColor")
    private Boolean           isColor;

    @DodoField(sortSeq = 48, nameKey = "dodo.privilege.admin.config.Field.namekey.extAttrJson")
    private String            extAttrJson;

    @DodoField(sortSeq = 49, nameKey = "dodo.privilege.admin.config.Field.namekey.picWidth")
    private int               picWidth;

    @DodoField(sortSeq = 50, nameKey = "dodo.privilege.admin.config.Field.namekey.picHeight")
    private int               picHeight;

    @DodoField(sortSeq = 51, nameKey = "dodo.privilege.admin.config.Field.namekey.maxFileSize")
    private int               maxFileSize;

    @DodoField(sortSeq = 52, nameKey = "dodo.privilege.admin.config.Field.namekey.isSuppBreakpoint")
    private Boolean           isSuppBreakpoint;

    @DodoField(sortSeq = 53, nameKey = "dodo.privilege.admin.config.Field.namekey.regExp")
    private String            regExp;

    @DodoField(sortSeq = 54, nameKey = "dodo.privilege.admin.config.Field.namekey.regExpTip")
    private String            regExpTip;

    @DodoField(sortSeq = 55, nameKey = "dodo.privilege.admin.config.Field.namekey.regExpTipKey")
    private String            regExpTipKey;

    @OneToMany(targetEntity = FieldRight.class, cascade = { CascadeType.ALL }, mappedBy = "field")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("sortSeq asc")
    public List<FieldRight> getAllFieldRights() {
        return allFieldRights;
    }

    public void setAllFieldRights(List<FieldRight> allFieldRights) {
        this.allFieldRights = allFieldRights;
    }

    @ManyToOne
    public Entity getEntity() {
        return entity;
    }

    public Boolean getEditable() {
        return editable;
    }

    public Boolean getAddable() {
        return addable;
    }

    public Boolean getListable() {
        return listable;
    }

    public Boolean getIspassword() {
        return ispassword;
    }

    @Column(length = 64)
    public String getPropertyName() {
        return propertyName;
    }

    @Column(length = 64)
    public String getShowName() {
        return showName;
    }

    public Boolean getIsNullable() {
        return isNullable;
    }

    @Column(length = 32)
    public String getColumnType() {
        return columnType;
    }

    public String getFieldType() {
        return fieldType;
    }

    @Column(length = 64)
    public String getGenerateMethodName() {
        return generateMethodName;
    }

    public String getGenerateClass() {
        return generateClass;
    }

    public String getInfoTip() {
        return infoTip;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public Boolean getIsEmail() {
        return isEmail;
    }

    public Boolean getIsMobile() {
        return isMobile;
    }

    public String getCheckMethod() {
        return checkMethod;
    }

    public Boolean getIsUsername() {
        return isUsername;
    }

    public Boolean getIsUrl() {
        return isUrl;
    }

    public Boolean getIsNumber() {
        return isNumber;
    }

    public Boolean getIsDigits() {
        return isDigits;
    }

    public Boolean getIsCreditcard() {
        return isCreditcard;
    }

    public Boolean getIsIp() {
        return isIp;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Boolean getIsFile() {
        return isFile;
    }

    public Boolean getIsDoc() {
        return isDoc;
    }

    public String getFileType() {
        return fileType;
    }

    public String getTreeSrvName() {
        return treeSrvName;
    }

    @Column(length = 64)
    public String getTreeMethodName() {
        return treeMethodName;
    }

    public String getTreeTargetClass() {
        return treeTargetClass;
    }

    public Boolean getIsCascade() {
        return isCascade;
    }

    public void setIsCascade(Boolean isCascade) {
        this.isCascade = isCascade;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public void setAddable(Boolean addable) {
        this.addable = addable;
    }

    public void setListable(Boolean listable) {
        this.listable = listable;
    }

    public void setIspassword(Boolean ispassword) {
        this.ispassword = ispassword;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public void setIsNullable(Boolean isNullable) {
        this.isNullable = isNullable;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public void setGenerateMethodName(String generateMethodName) {
        this.generateMethodName = generateMethodName;
    }

    public void setGenerateClass(String generateClass) {
        this.generateClass = generateClass;
    }

    public void setInfoTip(String infoTip) {
        this.infoTip = infoTip;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public void setIsEmail(Boolean isEmail) {
        this.isEmail = isEmail;
    }

    public void setIsMobile(Boolean isMobile) {
        this.isMobile = isMobile;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public void setIsUsername(Boolean isUsername) {
        this.isUsername = isUsername;
    }

    public void setIsUrl(Boolean isUrl) {
        this.isUrl = isUrl;
    }

    public void setIsNumber(Boolean isNumber) {
        this.isNumber = isNumber;
    }

    public void setIsDigits(Boolean isDigits) {
        this.isDigits = isDigits;
    }

    public void setIsCreditcard(Boolean isCreditcard) {
        this.isCreditcard = isCreditcard;
    }

    public void setIsIp(Boolean isIp) {
        this.isIp = isIp;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public void setIsFile(Boolean isFile) {
        this.isFile = isFile;
    }

    public void setIsDoc(Boolean isDoc) {
        this.isDoc = isDoc;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setTreeSrvName(String treeSrvName) {
        this.treeSrvName = treeSrvName;
    }

    public void setTreeMethodName(String treeMethodName) {
        this.treeMethodName = treeMethodName;
    }

    public void setTreeTargetClass(String treeTargetClass) {
        this.treeTargetClass = treeTargetClass;
    }

    public Integer getFieldSortSeq() {
        return fieldSortSeq;
    }

    public void setFieldSortSeq(Integer fieldSortSeq) {
        this.fieldSortSeq = fieldSortSeq;
    }

    @Column(length = 1024)
    public String getCascadeString() {
        return cascadeString;
    }

    @Column(length = 1024)
    public String getQueryParams() {
        return queryParams;
    }

    public void setCascadeString(String cascadeString) {
        this.cascadeString = cascadeString;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public Boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Boolean isVideo) {
        this.isVideo = isVideo;
    }

    @Column(length = 32)
    public String getDateFormat() {
        return dateFormat;
    }

    public Boolean getIsDetailView() {
        return isDetailView;
    }

    public Boolean getIsDetailEdit() {
        return isDetailEdit;
    }

    public Boolean getIsShowWholeTree() {
        return isShowWholeTree;
    }

    public Boolean getIsPopup() {
        return isPopup;
    }

    @Column(length = 16)
    public String getSingleModel() {
        return singleModel;
    }

    public Boolean getIsEncode() {
        return isEncode;
    }

    public String getFieldReturnClass() {
        return fieldReturnClass;
    }

    public Boolean getIsLocation() {
        return isLocation;
    }

    public Boolean getIsColor() {
        return isColor;
    }

    @Column(length = 1024)
    public String getExtAttrJson() {
        return extAttrJson;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public Boolean getIsSuppBreakpoint() {
        return isSuppBreakpoint;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setIsDetailView(Boolean isDetailView) {
        this.isDetailView = isDetailView;
    }

    public void setIsDetailEdit(Boolean isDetailEdit) {
        this.isDetailEdit = isDetailEdit;
    }

    public void setIsShowWholeTree(Boolean isShowWholeTree) {
        this.isShowWholeTree = isShowWholeTree;
    }

    public void setIsPopup(Boolean isPopup) {
        this.isPopup = isPopup;
    }

    public void setSingleModel(String singleModel) {
        this.singleModel = singleModel;
    }

    public void setIsEncode(Boolean isEncode) {
        this.isEncode = isEncode;
    }

    public void setFieldReturnClass(String fieldReturnClass) {
        this.fieldReturnClass = fieldReturnClass;
    }

    public void setIsLocation(Boolean isLocation) {
        this.isLocation = isLocation;
    }

    public void setIsColor(Boolean isColor) {
        this.isColor = isColor;
    }

    public void setExtAttrJson(String extAttrJson) {
        this.extAttrJson = extAttrJson;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public void setIsSuppBreakpoint(Boolean isSuppBreakpoint) {
        this.isSuppBreakpoint = isSuppBreakpoint;
    }

    @Column(length = 128)
    public String getShowNameKey() {
        return showNameKey;
    }

    public void setShowNameKey(String showNameKey) {
        this.showNameKey = showNameKey;
    }

    @Column(length = 32)
    public String getOssBucket() {
        return ossBucket;
    }

    public void setOssBucket(String ossBucket) {
        this.ossBucket = ossBucket;
    }

    @Column(length = 128)
    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    @Column(length = 128)
    public String getRegExpTip() {
        return regExpTip;
    }

    public void setRegExpTip(String regExpTip) {
        this.regExpTip = regExpTip;
    }

    @Column(length = 128)
    public String getRegExpTipKey() {
        return regExpTipKey;
    }

    public void setRegExpTipKey(String regExpTipKey) {
        this.regExpTipKey = regExpTipKey;
    }

    @Override
    public String toString() {
        return this.showName;
    }
}
