package com.dodo.common.sqlreport;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.annotation.report.ReportFieldType;
import com.dodo.common.annotation.report.ReportQueryType;
import com.dodo.privilege.entity.report_3.config_1.ReportField;
import com.dodo.utils.CommonUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ReportQueryUtil {
    public static Map<String, Object> parseQueryFields(List<ReportField> reportFields, HttpServletRequest request) {
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (reportFields == null || reportFields.size() == 0) {
            return queryParams;
        }

        String queryField = null;
        String queryFieldValue = null;
        String[] queryFieldValues = null;
        for (ReportField reportField : reportFields) {
            if (reportField.getQueryType() == null) {
                continue;
            }
            if (reportField.getQueryType() == ReportQueryType.eq || reportField.getQueryType() == ReportQueryType.ne
                    || reportField.getQueryType() == ReportQueryType.isNull
                    || reportField.getQueryType() == ReportQueryType.isNotNull
                    || reportField.getQueryType() == ReportQueryType.gt
                    || reportField.getQueryType() == ReportQueryType.lt
                    || reportField.getQueryType() == ReportQueryType.ge
                    || reportField.getQueryType() == ReportQueryType.le
                    || reportField.getQueryType() == ReportQueryType.like
                    || reportField.getQueryType() == ReportQueryType.notLike) {
                queryField = "queryField_" + reportField.getId();
                queryFieldValue = request.getParameter(queryField);
                if (StringUtils.isNotBlank(queryFieldValue)) {
                    queryParams.put(queryField, queryFieldValue.trim());
                }
            } else if (reportField.getQueryType() == ReportQueryType.between
                    || reportField.getQueryType() == ReportQueryType.notBetween) {
                queryField = "queryField_" + reportField.getId() + "_begin";
                queryFieldValue = request.getParameter(queryField);
                if (StringUtils.isNotBlank(queryFieldValue)) {
                    queryParams.put(queryField, queryFieldValue.trim());
                }
                queryField = "queryField_" + reportField.getId() + "_end";
                queryFieldValue = request.getParameter(queryField);
                if (StringUtils.isNotBlank(queryFieldValue)) {
                    queryParams.put(queryField, queryFieldValue.trim());
                }
            } else if (reportField.getQueryType() == ReportQueryType.in
                    || reportField.getQueryType() == ReportQueryType.notIn) {
                queryField = "queryField_" + reportField.getId();
                queryFieldValues = request.getParameterValues(queryField);
                if (queryFieldValues != null && queryFieldValues.length > 0) {
                    List<String> list = new ArrayList<String>();
                    for (String s : queryFieldValues) {
                        if (StringUtils.isNotBlank(s)) {
                            list.add(s);
                        }
                    }
                    if (list.size() > 0) {
                        queryParams.put(queryField, list);
                    }
                } else {
                    queryField = "queryField_" + reportField.getId() + "[]";
                    queryFieldValues = request.getParameterValues(queryField);
                    if (queryFieldValues != null && queryFieldValues.length > 0) {
                        List<String> list = new ArrayList<String>();
                        for (String s : queryFieldValues) {
                            if (StringUtils.isNotBlank(s)) {
                                list.add(s);
                            }
                        }
                        if (list.size() > 0) {
                            queryParams.put(StringUtils.substringBefore(queryField, "["), list);
                        }
                    }
                }
            }
        }

        return queryParams;
    }

    @SuppressWarnings("unchecked")
    public static String getWhereClause(List<ReportField> reportFields, Map<String, Object> parsedQueryFields,
            List<Object> whereClauseVars) {
        if (reportFields == null || parsedQueryFields == null) {
            return "";
        }
        StringBuilder whereClause = new StringBuilder();
        String queryField = null;
        String queryFieldValue = null;
        String queryField2 = null;
        String queryFieldValue2 = null;
        List<String> queryFieldValues = null;
        for (ReportField reportField : reportFields) {
            if (reportField.getQueryType() == null) {
                continue;
            }
            if (reportField.getQueryType() == ReportQueryType.eq || reportField.getQueryType() == ReportQueryType.ne
                    || reportField.getQueryType() == ReportQueryType.gt
                    || reportField.getQueryType() == ReportQueryType.lt
                    || reportField.getQueryType() == ReportQueryType.ge
                    || reportField.getQueryType() == ReportQueryType.le) {
                queryField = "queryField_" + reportField.getId();
                queryFieldValue = (String) parsedQueryFields.get(queryField);
                if (queryFieldValue != null) {
                    whereClause.append(" and ");
                    whereClause.append(MessageFormat.format(reportField.getQueryType().getExpression(),
                            CommonUtil.unEscapeHtml(reportField.getQueryField())));
                    if (reportField.getFieldType() == ReportFieldType.String) {
                        whereClauseVars.add(queryFieldValue);
                    } else if (reportField.getFieldType() == ReportFieldType.Boolean) {
                        whereClauseVars.add("1".equals(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.BigDecimal) {
                        whereClauseVars.add(new BigDecimal(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.Byte) {
                        whereClauseVars.add(Byte.parseByte(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.Short) {
                        whereClauseVars.add(Short.parseShort(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.Integer) {
                        whereClauseVars.add(Integer.parseInt(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.Long) {
                        whereClauseVars.add(Long.parseLong(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.Float) {
                        whereClauseVars.add(Float.parseFloat(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.Double) {
                        whereClauseVars.add(Double.parseDouble(queryFieldValue));
                    } else if (reportField.getFieldType() == ReportFieldType.Date) {
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue, java.sql.Date.class));
                    } else if (reportField.getFieldType() == ReportFieldType.Time) {
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue, java.sql.Time.class));
                    } else if (reportField.getFieldType() == ReportFieldType.Timestamp) {
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue, java.sql.Timestamp.class));
                    }
                }
            } else if (reportField.getQueryType() == ReportQueryType.isNull
                    || reportField.getQueryType() == ReportQueryType.isNotNull) {
                if (parsedQueryFields.get("queryField_" + reportField.getId()) != null) {
                    whereClause.append(" and ");
                    whereClause.append(MessageFormat.format(reportField.getQueryType().getExpression(),
                            CommonUtil.unEscapeHtml(reportField.getQueryField())));
                }
            } else if (reportField.getQueryType() == ReportQueryType.like
                    || reportField.getQueryType() == ReportQueryType.notLike) {
                queryField = "queryField_" + reportField.getId();
                queryFieldValue = (String) parsedQueryFields.get(queryField);
                if (queryFieldValue != null) {
                    whereClause.append(" and ");
                    whereClause.append(MessageFormat.format(reportField.getQueryType().getExpression(),
                            CommonUtil.unEscapeHtml(reportField.getQueryField())));
                    whereClauseVars.add("%%" + queryFieldValue + "%%");
                }
            } else if (reportField.getQueryType() == ReportQueryType.between
                    || reportField.getQueryType() == ReportQueryType.notBetween) {
                queryField = "queryField_" + reportField.getId() + "_begin";
                queryField2 = "queryField_" + reportField.getId() + "_end";
                queryFieldValue = (String) parsedQueryFields.get(queryField);
                queryFieldValue2 = (String) parsedQueryFields.get(queryField2);
                if (queryFieldValue != null && queryFieldValue2 != null) {
                    whereClause.append(" and ");
                    whereClause.append(MessageFormat.format(reportField.getQueryType().getExpression(),
                            CommonUtil.unEscapeHtml(reportField.getQueryField())));
                    if (reportField.getFieldType() == ReportFieldType.BigDecimal) {
                        whereClauseVars.add(new BigDecimal(queryFieldValue));
                        whereClauseVars.add(new BigDecimal(queryFieldValue2));
                    } else if (reportField.getFieldType() == ReportFieldType.Byte) {
                        whereClauseVars.add(Byte.parseByte(queryFieldValue));
                        whereClauseVars.add(Byte.parseByte(queryFieldValue2));
                    } else if (reportField.getFieldType() == ReportFieldType.Short) {
                        whereClauseVars.add(Short.parseShort(queryFieldValue));
                        whereClauseVars.add(Short.parseShort(queryFieldValue2));
                    } else if (reportField.getFieldType() == ReportFieldType.Integer) {
                        whereClauseVars.add(Integer.parseInt(queryFieldValue));
                        whereClauseVars.add(Integer.parseInt(queryFieldValue2));
                    } else if (reportField.getFieldType() == ReportFieldType.Long) {
                        whereClauseVars.add(Long.parseLong(queryFieldValue));
                        whereClauseVars.add(Long.parseLong(queryFieldValue2));
                    } else if (reportField.getFieldType() == ReportFieldType.Float) {
                        whereClauseVars.add(Float.parseFloat(queryFieldValue));
                        whereClauseVars.add(Float.parseFloat(queryFieldValue2));
                    } else if (reportField.getFieldType() == ReportFieldType.Double) {
                        whereClauseVars.add(Double.parseDouble(queryFieldValue));
                        whereClauseVars.add(Double.parseDouble(queryFieldValue2));
                    } else if (reportField.getFieldType() == ReportFieldType.Date) {
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue, java.sql.Date.class));
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue2, java.sql.Date.class));
                    } else if (reportField.getFieldType() == ReportFieldType.Time) {
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue, java.sql.Time.class));
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue2, java.sql.Time.class));
                    } else if (reportField.getFieldType() == ReportFieldType.Timestamp) {
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue, java.sql.Timestamp.class));
                        whereClauseVars.add(CommonUtil.parseSpecialDate(queryFieldValue2, java.sql.Timestamp.class));
                    }
                }
            } else if (reportField.getQueryType() == ReportQueryType.in
                    || reportField.getQueryType() == ReportQueryType.notIn) {
                queryField = "queryField_" + reportField.getId();
                queryFieldValues = (List<String>) parsedQueryFields.get(queryField);
                if (queryFieldValues != null && queryFieldValues.size() > 0) {
                    StringBuilder sbBuilder = new StringBuilder();
                    for (String string : queryFieldValues) {
                        if (reportField.getFieldType() == ReportFieldType.String) {
                            whereClauseVars.add(string);
                        } else if (reportField.getFieldType() == ReportFieldType.Boolean) {
                            whereClauseVars.add("1".equals(string));
                        } else if (reportField.getFieldType() == ReportFieldType.BigDecimal) {
                            whereClauseVars.add(new BigDecimal(string));
                        } else if (reportField.getFieldType() == ReportFieldType.Byte) {
                            whereClauseVars.add(Byte.parseByte(string));
                        } else if (reportField.getFieldType() == ReportFieldType.Short) {
                            whereClauseVars.add(Short.parseShort(string));
                        } else if (reportField.getFieldType() == ReportFieldType.Integer) {
                            whereClauseVars.add(Integer.parseInt(string));
                        } else if (reportField.getFieldType() == ReportFieldType.Long) {
                            whereClauseVars.add(Long.parseLong(string));
                        } else if (reportField.getFieldType() == ReportFieldType.Float) {
                            whereClauseVars.add(Float.parseFloat(string));
                        } else if (reportField.getFieldType() == ReportFieldType.Double) {
                            whereClauseVars.add(Double.parseDouble(string));
                        }
                        sbBuilder.append("?,");
                    }
                    whereClause.append(" and ");
                    whereClause.append(MessageFormat.format(reportField.getQueryType().getExpression(),
                            CommonUtil.unEscapeHtml(reportField.getQueryField()),
                            sbBuilder.deleteCharAt(sbBuilder.length() - 1).toString()));
                }
            }
        }
        return whereClause.toString();
    }
}
