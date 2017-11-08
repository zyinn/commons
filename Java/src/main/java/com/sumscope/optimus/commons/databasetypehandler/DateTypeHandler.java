package com.sumscope.optimus.commons.databasetypehandler;


import com.sumscope.optimus.commons.log.LogManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * MyBatis的Date数据转换
 * 
 * @author xingyue.wang
 *
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {
    private static Log logger = LogFactory.getLog(DateTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setDate(i, new java.sql.Date((parameter.getTime())));
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        java.sql.Date sqlDate = null;
        try{
            sqlDate = rs.getDate(columnName);
        }
        catch(Exception e){
            LogManager.error("get date error");
        }
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        java.sql.Date sqlDate = null;
        try{
            sqlDate = rs.getDate(columnIndex);
        }
        catch(Exception e){
            logger.error("get date error", e);
        }
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        java.sql.Date sqlDate = null;
        try{
            sqlDate = cs.getDate(columnIndex);
        }
        catch(Exception e){
            logger.error("get date error", e);
        }
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }

}
