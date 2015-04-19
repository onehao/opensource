/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.onehao.acts.testgen.service.extension.dict;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.onehao.acts.testgen.service.extension.Utils;

/**
 * @author yinxu
 *
 */
public class DictDataManager {
    
    private static DictDataManager inst = null;
    
    private CsvDBConnection conn = null;
    
    private static String baseSql = "SELECT %s FROM %s WHERE %s";

    protected DictDataManager(String dataPath) {
        this.conn = new CsvDBConnection(dataPath);
    }
    
    public static DictDataManager getInst() {
        if (null == inst) {
            String path=System.getProperty("path");
            inst = new DictDataManager(path + "/data");
        }
        
        return inst;
    }
    
    protected ResultSet query(String fieldName, String table, String condition) {
        if (null == fieldName || null == table || fieldName.isEmpty() || table.isEmpty()) {
            return null;
        }
        
        boolean ignoreCondition = null == condition || condition.isEmpty();
        
        String sql = String.format(baseSql, fieldName, table, ignoreCondition ? "1=1" : condition);
        
        return this.executeQuery(sql);
    }
    
    protected ResultSet executeQuery(String sql) {
        if (!this.conn.isOpened()) {
            if (!this.conn.openConn()) {
                return null;
            }
        }
        
        return this.conn.executeQuery(sql);
    }
    
    public Collection<String> pickRandomValues(String queryStr) {
        Collection<String> retValues = new HashSet<String>();
        QueryStringParser parser = QueryStringParser.Parse(queryStr);
        ResultSet rs = this.query(parser.getField(), parser.getTable(), parser.getQuery());
        if (null == rs) {
            return null;
        }
        
        int avgStep = this.getAvgStep(rs, parser.getCount());
        try {
            rs.first();
            int index = 0;
            while (index < parser.getCount() && rs.relative(Utils.getRandomInt(avgStep))) {
                retValues.add(rs.getString(1));
                index++;
                if (rs.getRow() - 1 < index * avgStep) {
                    rs.relative(index * avgStep - rs.getRow() + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        this.conn.closeResultSet(rs);
        
        return retValues;
    }
    
    protected int getAvgStep(ResultSet rs, int count) {
        count = count <= 0 ? 20 : count;
        try {
            rs.last();
            int rowLen = rs.getRow();
            rs.first();
            int avg = rowLen/count;
            return avg <= 0 ? 1 : avg;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
