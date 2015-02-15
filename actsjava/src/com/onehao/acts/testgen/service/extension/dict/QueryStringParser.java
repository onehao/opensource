/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.onehao.acts.testgen.service.extension.dict;

/**
 * @author yinxu
 *
 */
public class QueryStringParser {
    
    private String field = null;
    private String table = null;
    private String query = null;
    private int count = 20;
    
    private QueryStringParser() {
    }
    
    public static QueryStringParser Parse(String queryStr) {
        QueryStringParser ret = new QueryStringParser();
        if (null == queryStr || queryStr.isEmpty()) {
            return ret;
        }
        
        String[] tokens = queryStr.split(":");
        
        switch (tokens.length) {
            case 1:
                ret.parseFieldAndTable(tokens[0]);
                break;
            case 2:
                ret.parseFieldAndTable(tokens[0]);
                ret.parserCondition(tokens[1]);
                break;
            default:
                ret.parseFieldAndTable(tokens[0]);
                ret.parserCondition(tokens[1]);
                ret.parserCount(tokens[2]);
                break;
        }

        return ret;
    }
    
    protected void parseFieldAndTable(String str) {
        String[] tokens = str.split("@");
        if (tokens.length != 2) {
            return;
        }
        
        this.field = tokens[0];
        this.table = tokens[1];
    }
    
    protected void parserCondition(String condition) {
        if (!condition.isEmpty()) {
            this.query = condition;
        }
    }
    
    protected void parserCount(String count) {
        try {
            this.count = Integer.parseInt(count);
        } catch (Exception ex) {
            // Ignore if count is not integer.
        }
    }
    
    public String getField() {
        return field;
    }
    public String getTable() {
        return table;
    }
    public String getQuery() {
        return query;
    }
    public int getCount() {
        return count;
    }
}
