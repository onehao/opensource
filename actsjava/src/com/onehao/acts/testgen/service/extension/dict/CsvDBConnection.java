/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.onehao.acts.testgen.service.extension.dict;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author yinxu
 *
 */
public class CsvDBConnection {
    
    protected Connection conn = null;
    protected String prevCsvPath = null;
    
    public CsvDBConnection() {
    }
    
    public CsvDBConnection(String csvPath) {
        if (csvPath != null) {
            this.prevCsvPath = csvPath;
        }
    }
    
    public boolean openConn() {
        if (this.prevCsvPath != null && !this.prevCsvPath.isEmpty()) {
            return this.openConn(prevCsvPath);
        } else {
            return false;
        }
    }
    
    public boolean openConn(String csvPath) {
        try {
            this.closeConn();
            Class.forName("org.relique.jdbc.csv.CsvDriver");
            this.conn = DriverManager.getConnection("jdbc:relique:csv:" + csvPath);
            this.prevCsvPath = csvPath;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void closeConn() {
        if (this.conn != null) {
            try {
                if (!this.conn.isClosed()) {
                    this.conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        this.conn = null;
    }
    
    public boolean refreshConn() {
        if (this.prevCsvPath != null) {
            return this.openConn(prevCsvPath);
        } else {
            return false;
        }
    }
    
    public ResultSet executeQuery(String sql) {
        if (null == this.conn) {
            return null;
        }
        
        try {
            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.getStatement().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isOpened() {
        if (this.conn != null) {
            try {
                if (this.conn.isClosed()) {
                    return false;
                } else {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
