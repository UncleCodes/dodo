package com.dodo.privilege.action.report;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dodo.common.annotation.report.ReportFieldType;
import com.dodo.common.annotation.report.ReportQueryType;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.common.framework.service.JdbcService;
import com.dodo.common.framework.service.SqlReportService;
import com.dodo.common.sqlreport.EasyuiResultBean;
import com.dodo.common.sqlreport.ReportDesignBean;
import com.dodo.common.sqlreport.ReportQueryUtil;
import com.dodo.common.sqlreport.SqlReportLimit;
import com.dodo.common.sqlreport.SqlReportQueryResult;
import com.dodo.common.sqlreport.SqlReportResultSetExtractor;
import com.dodo.privilege.entity.report_3.config_1.ReportEntity;
import com.dodo.privilege.entity.report_3.config_1.ReportField;
import com.dodo.privilege.entity.report_3.config_1.ReportMenu;
import com.dodo.privilege.security.DodoSecurityService;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.ExcelUtils;
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
@Controller
@RequestMapping("${dodo.backmanage.view.rootPath}/sqlreport")
public class ReportDesignAction {

    private Pattern             pattern = Pattern.compile("(\\d+)");

    @Autowired
    private JdbcService         jdbcService;

    @Autowired
    private HqlHelperService    hqlHelperService;

    @Autowired
    private SqlReportService    sqlReportService;

    @Autowired
    private DodoSecurityService dodoSecurityService;

    /**
     * 报表设计 - 开始
     * **/
    @RequestMapping("/design.jhtml")
    public String design(Model model, String entityIds, String entityId) {
        HqlHelper helper = HqlHelper.queryFrom(ReportMenu.class);
        helper.fetch("id", "menuName");
        model.addAttribute("menus", hqlHelperService.getRecords(helper, Boolean.FALSE).getRawData());

        String updateReportId = null;
        if (StringUtils.isNotBlank(entityId)) {
            updateReportId = entityId;
        } else if (StringUtils.isNotBlank(entityIds)) {
            Matcher matcher = pattern.matcher(entityIds);
            if (matcher.find()) {
                updateReportId = matcher.group(1);
            }
        }
        ReportEntity reportEntity = null;
        if (updateReportId != null) {
            reportEntity = hqlHelperService.get(DodoFrameworkConfigUtil.getRightTypeIdValue(updateReportId),
                    ReportEntity.class);
        }
        if (reportEntity != null) {
            model.addAttribute("updateReportId", updateReportId);
            model.addAttribute("reportEntity", reportEntity);
        }
        return "reportdesign";
    }

    /**
     * SQL报表设计器 高亮显示表名 并悬浮提示使用
     * 
     */
    @RequestMapping("/dbmeta.jhtml")
    @ResponseBody
    public List<DBMeta> dbMetadata() {
        List<DBMeta> metadata = new ArrayList<DBMeta>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = jdbcService.getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            resultSet = dbMetaData.getTables(connection.getCatalog(), null, "%", new String[] { "TABLE", "VIEW" });
            DBMeta dbMeta = null;
            ResultSet rs = null;
            while (resultSet.next()) {
                dbMeta = new DBMeta(resultSet.getString("TABLE_NAME"), "table");
                metadata.add(dbMeta);
                try {
                    rs = dbMetaData.getColumns(connection.getCatalog(), null, dbMeta.getName(), "%");
                    while (rs.next()) {
                        dbMeta = new DBMeta(rs.getString("COLUMN_NAME"), "column");
                        if (StringUtils.isNotBlank(dbMeta.getName())) {
                            metadata.add(dbMeta);
                        }
                    }
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return metadata;
    }

    /**
     * 查询类型
     */
    @RequestMapping("/queryType.jhtml")
    @ResponseBody
    public Map<String, String> queryType(HttpServletRequest request) {
        ReportQueryType[] reportQueryTypes = ReportQueryType.values();
        Map<String, String> qMap = new HashMap<String, String>(reportQueryTypes.length);
        for (ReportQueryType queryType : reportQueryTypes) {
            qMap.put(queryType.name(), SpringUtil.getMessageBack(queryType.getNameKey(), request));
        }
        return qMap;
    }

    /**
     * 字段类型
     */
    @RequestMapping("/fieldType.jhtml")
    @ResponseBody
    public Map<String, String> fieldType(HttpServletRequest request) {
        ReportFieldType[] reportFieldTypes = ReportFieldType.values();
        Map<String, String> qMap = new HashMap<String, String>(reportFieldTypes.length);
        for (ReportFieldType fieldType : reportFieldTypes) {
            qMap.put(fieldType.name(), SpringUtil.getMessageBack(fieldType.getNameKey(), request));
        }
        return qMap;
    }

    /**
     * 查询数据 - 分页
     * **/
    @RequestMapping("/query.jhtml")
    @ResponseBody
    public Object getQueryResult(String execSQL, Integer pageNumber, Integer pageSize) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        Long currTime = System.currentTimeMillis();
        try {
            SqlReportLimit sqlReportLimit = hqlHelperService.getLimitSQL(execSQL, null, null, pageNumber, pageSize);
            SqlReportQueryResult sqlReportQueryResult = null;
            if (sqlReportLimit.getPreparedStatementSetter() == null) {
                sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                        new SqlReportResultSetExtractor());
            } else {
                sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                        sqlReportLimit.getPreparedStatementSetter(), new SqlReportResultSetExtractor());
            }
            sqlReportQueryResult.setPageNumber(sqlReportLimit.getPageNumber());
            sqlReportQueryResult.setPageSize(sqlReportLimit.getPageSize());
            sqlReportQueryResult.setTotalCount(jdbcService.queryForInt(hqlHelperService.getCountSQL(execSQL, null)));
            sqlReportQueryResult.setTimeMills(System.currentTimeMillis() - currTime);
            return sqlReportQueryResult;
        } catch (Exception e) {
            e.printStackTrace();
            SqlReportQueryResult sqlReportQueryResult = new SqlReportQueryResult();
            sqlReportQueryResult.setIsSuccess(Boolean.FALSE);
            sqlReportQueryResult.setMessage(e.getMessage());
            sqlReportQueryResult.setTimeMills(System.currentTimeMillis() - currTime);
            return sqlReportQueryResult;
        }
    }

    /**
     * 查询数据 - 分页 - easyui
     * **/
    @RequestMapping("/easyui/query.jhtml")
    @ResponseBody
    public Object getQueryResultEasyui(String execSQL, Integer page, Integer rows) {
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        try {
            SqlReportLimit sqlReportLimit = hqlHelperService.getLimitSQL(execSQL, null, null, page, rows);
            SqlReportQueryResult sqlReportQueryResult = null;
            if (sqlReportLimit.getPreparedStatementSetter() == null) {
                sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                        new SqlReportResultSetExtractor());
            } else {
                sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                        sqlReportLimit.getPreparedStatementSetter(), new SqlReportResultSetExtractor());
            }

            EasyuiResultBean easyuiResultBean = new EasyuiResultBean();
            easyuiResultBean.setTotal(jdbcService.queryForInt(hqlHelperService.getCountSQL(execSQL, null)));

            for (List<String> list : sqlReportQueryResult.getQueryDatas()) {
                Map<String, String> map = new HashMap<String, String>(list.size());
                for (int i = 0; i < list.size(); i++) {
                    map.put(i + "", list.get(i));
                }
                easyuiResultBean.addRow(map);
            }

            return easyuiResultBean;
        } catch (Exception e) {
            e.printStackTrace();
            SqlReportQueryResult sqlReportQueryResult = new SqlReportQueryResult();
            sqlReportQueryResult.setIsSuccess(Boolean.FALSE);
            sqlReportQueryResult.setMessage(e.getMessage());
            return sqlReportQueryResult;
        }
    }

    /**
     * 保存设计方案
     * **/
    @RequestMapping("/saveOrUpdate.jhtml")
    @ResponseBody
    public Map<String, Object> addOneReport(ReportDesignBean designBean, HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<String, Object>(4);
        returnMap.put("isSuccess", Boolean.FALSE);
        returnMap.put("message", SpringUtil.getMessageBack("dodo.sqlreport.design.submit.fail", request));
        try {
            sqlReportService.saveOrUpdateReport(designBean, request);
            dodoSecurityService.refreshCurrLoginAdmin();
            returnMap.put("isSuccess", Boolean.TRUE);
            returnMap.put("message", SpringUtil.getMessageBack("dodo.sqlreport.design.submit.success", request));
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }

    /**
     * 报表查看 - 没有配置权限入口
     * **/
    @RequestMapping("/report.jhtml")
    public String reportViewerRedirect(Model model, String entityId, Integer pageNumber, Integer pageSize,
            String queryFlag, Integer page, Integer rows, HttpServletRequest request) {
        return reportViewer(model, entityId, pageNumber, pageSize, queryFlag, page, rows, "reportviewer_design",
                request);
    }

    /**
     * 报表查看 - 开始
     * **/
    @RequestMapping("/report/{reportId}.jhtml")
    public String reportViewer(Model model, @PathVariable String reportId, Integer pageNumber, Integer pageSize,
            String queryFlag, Integer page, Integer rows, String selfView, HttpServletRequest request) {
        // for easyui
        if (page != null) {
            pageNumber = page;
        }
        // for easyui
        if (rows != null) {
            pageSize = rows;
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }

        if (pageSize == null) {
            pageSize = 20;
        }
        try {
            ReportEntity reportEntity = hqlHelperService.get(DodoFrameworkConfigUtil.getRightTypeIdValue(reportId),
                    ReportEntity.class);
            model.addAttribute("reportEntity", reportEntity);
            model.addAttribute("queryFlag", queryFlag);
            Map<String, Object> parsedQueryFields = ReportQueryUtil.parseQueryFields(reportEntity.getReportFields(),
                    request);
            model.addAttribute("parsedQueryFields", parsedQueryFields);
            List<Object> whereClauseVars = new ArrayList<Object>();
            String whereClause = ReportQueryUtil.getWhereClause(reportEntity.getReportFields(), parsedQueryFields,
                    whereClauseVars);

            if (!"0".equals(queryFlag)) {

                SqlReportLimit sqlReportLimit = hqlHelperService.getLimitSQL(reportEntity.getExecSql(), whereClause,
                        whereClauseVars, pageNumber, pageSize);
                SqlReportQueryResult sqlReportQueryResult = null;
                if (sqlReportLimit.getPreparedStatementSetter() == null) {
                    sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                            new SqlReportResultSetExtractor());
                } else {
                    sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                            sqlReportLimit.getPreparedStatementSetter(), new SqlReportResultSetExtractor());
                }
                sqlReportQueryResult.setPageNumber(sqlReportLimit.getPageNumber());
                sqlReportQueryResult.setPageSize(sqlReportLimit.getPageSize());
                if (whereClauseVars.size() == 0) {
                    sqlReportQueryResult.setTotalCount(jdbcService.queryForInt(hqlHelperService.getCountSQL(
                            reportEntity.getExecSql(), whereClause)));
                } else {
                    sqlReportQueryResult.setTotalCount(jdbcService.queryForInt(
                            hqlHelperService.getCountSQL(reportEntity.getExecSql(), whereClause),
                            whereClauseVars.toArray(new Object[whereClauseVars.size()])));
                }
                model.addAttribute("sqlReportQueryResult", sqlReportQueryResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("dialogType", "error");
            model.addAttribute("infoTip", SpringUtil.getMessageBack("dodo.sqlreport.view.exception", request));
            return "info";
        }
        return selfView == null ? "reportviewer" : selfView;
    }

    /**
     * 报表导出- 开始
     * **/
    @RequestMapping("/reportexcel/{reportId}.jhtml")
    public void reportexcel(Model model, @PathVariable String reportId, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ReportEntity reportEntity = hqlHelperService.get(DodoFrameworkConfigUtil.getRightTypeIdValue(reportId),
                    ReportEntity.class);
            Workbook workbook = WorkbookFactory.create(true);
            String exportFileName = CommonUtil.getDownloadFilename(
                    request,
                    reportEntity.getName()
                            + "_"
                            + CommonUtil.getSpecialDateStr(new java.sql.Date(System.currentTimeMillis()),
                                    "yyyy-MM-dd_HH-mm-ss"))
                    + ".xlsx";
            List<String> fileTitle = new LinkedList<String>();
            List<ReportField> reportFields = reportEntity.getReportFields();
            List<Integer> hiddenFields = new ArrayList<Integer>(reportFields.size());
            ReportField field = null;
            for (int i = 0; i < reportFields.size(); i++) {
                field = reportFields.get(i);
                if (field.getIsShow()) {
                    fileTitle.add(StringUtils.isBlank(field.getShowName()) ? field.getQueryField() : field
                            .getShowName());
                } else {
                    hiddenFields.add(i);
                }
            }

            Map<String, Object> parsedQueryFields = ReportQueryUtil.parseQueryFields(reportEntity.getReportFields(),
                    request);
            List<Object> whereClauseVars = new ArrayList<Object>();
            String whereClause = ReportQueryUtil.getWhereClause(reportEntity.getReportFields(), parsedQueryFields,
                    whereClauseVars);

            SqlReportLimit sqlReportLimit = null;
            SqlReportQueryResult sqlReportQueryResult = null;

            int excelFetchRow = 100;
            int sheetMaxRow = 100 * 500;
            int beginRow = 2;
            int sheetIndex = -1;
            List<List<String>> queryDatas = null;
            for (int i = 1;; i++) {
                sqlReportLimit = hqlHelperService.getLimitSQL(reportEntity.getExecSql(), whereClause, whereClauseVars,
                        i, excelFetchRow);
                if (sqlReportLimit.getPreparedStatementSetter() == null) {
                    sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                            new SqlReportResultSetExtractor());
                } else {
                    sqlReportQueryResult = jdbcService.query(sqlReportLimit.getLimitSql(),
                            sqlReportLimit.getPreparedStatementSetter(), new SqlReportResultSetExtractor());
                }
                queryDatas = sqlReportQueryResult.getQueryDatas();
                if (queryDatas == null || queryDatas.size() == 0) {
                    break;
                }

                if (hiddenFields.size() > 0) {
                    for (List<String> queryData : queryDatas) {
                        for (int j = hiddenFields.size() - 1; j >= 0; j--) {
                            queryData.remove(hiddenFields.get(j).intValue());
                        }
                    }
                }

                if (((i - 1) * excelFetchRow) % sheetMaxRow == 0) {
                    ++sheetIndex;
                    beginRow = 2;
                    workbook = ExcelUtils.writeWorkbookTitle(workbook, fileTitle, null);
                }
                workbook = ExcelUtils.writeWorkbookContent(workbook, sheetIndex, queryDatas, beginRow);
                beginRow = beginRow + queryDatas.size();
            }
            if (sheetIndex == -1) {
                workbook = ExcelUtils.writeWorkbookTitle(workbook, fileTitle, null);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + exportFileName + "\"");
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class DBMeta {
    private String name;
    private String type;

    public DBMeta(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}