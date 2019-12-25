package com.dodo.common.sqlreport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.dodo.common.annotation.report.ReportFieldType;
import com.dodo.utils.CommonUtil;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SqlReportFactory {
	public static ReportFieldType convertSQLType2ReportFieldType(int sqlType){
		if(sqlType==Types.CHAR||sqlType==Types.VARCHAR||sqlType==Types.LONGVARCHAR){
			return ReportFieldType.String;
		}
		if(sqlType==Types.NUMERIC||sqlType==Types.DECIMAL){
			return ReportFieldType.BigDecimal;
		}
		if(sqlType==Types.BIT){
			return ReportFieldType.Boolean;
		}
		if(sqlType==Types.TINYINT){
			return ReportFieldType.Byte;
		}
		if(sqlType==Types.SMALLINT){
			return ReportFieldType.Short;
		}
		if(sqlType==Types.INTEGER){
			return ReportFieldType.Integer;
		}
		if(sqlType==Types.BIGINT){
			return ReportFieldType.Long;
		}
		if(sqlType==Types.REAL){
			return ReportFieldType.Float;
		}
		if(sqlType==Types.FLOAT||sqlType==Types.DOUBLE){
			return ReportFieldType.Double;
		}
		if(sqlType==Types.BINARY||sqlType==Types.VARBINARY||sqlType==Types.LONGVARBINARY){
			return ReportFieldType.ByteArray;
		}
		if(sqlType==Types.DATE){
			return ReportFieldType.Date;
		}
		if(sqlType==Types.TIME){
			return ReportFieldType.Time;
		}
		if(sqlType==Types.TIMESTAMP){
			return ReportFieldType.Timestamp;
		}
		return ReportFieldType.Object;
		
		
	}
	public static String convertData2String(ResultSet resultSet,int i,int sqlType) throws SQLException{
		Object returnStrObj = null;
		if(sqlType==Types.CHAR||sqlType==Types.VARCHAR||sqlType==Types.LONGVARCHAR){
			returnStrObj = resultSet.getString(i);
		}else if(sqlType==Types.NUMERIC||sqlType==Types.DECIMAL){
			returnStrObj = resultSet.getBigDecimal(i);
		}else if(sqlType==Types.BIT){
			returnStrObj = resultSet.getBoolean(i);
		}else if(sqlType==Types.TINYINT){
			returnStrObj = resultSet.getByte(i);
		}else if(sqlType==Types.SMALLINT){
			returnStrObj = resultSet.getShort(i);
		}else if(sqlType==Types.INTEGER){
			returnStrObj = resultSet.getInt(i);
		}else if(sqlType==Types.BIGINT){
			returnStrObj = resultSet.getLong(i);
		}else if(sqlType==Types.REAL){
			returnStrObj = resultSet.getFloat(i);
		}else if(sqlType==Types.FLOAT||sqlType==Types.DOUBLE){
			returnStrObj = resultSet.getDouble(i);
		}else if(sqlType==Types.BINARY||sqlType==Types.VARBINARY||sqlType==Types.LONGVARBINARY){
			returnStrObj = resultSet.getObject(i);
		}else if(sqlType==Types.DATE){
			java.sql.Date obj = resultSet.getDate(i);
			if(obj!=null){
				returnStrObj = CommonUtil.getSpecialDateStr(obj, "yyyy-MM-dd");
			}
		}else if(sqlType==Types.TIME){
			java.sql.Time obj = resultSet.getTime(i);
			if(obj!=null){
				returnStrObj = CommonUtil.getSpecialDateStr(obj, "HH:mm:ss");
			}
		}else if(sqlType==Types.TIMESTAMP){
			java.sql.Timestamp obj = resultSet.getTimestamp(i);
			if(obj!=null){
				returnStrObj = CommonUtil.getSpecialDateStr(obj, "yyyy-MM-dd HH:mm:ss");
			}
		}
		if(returnStrObj==null){
			return "";
		}
		return returnStrObj.toString();
	}
}
