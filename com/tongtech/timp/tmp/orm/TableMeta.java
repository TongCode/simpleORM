package com.tongtech.timp.tmp.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据表描述信息
 * @author Daniel
 *
 */
public class TableMeta {
	
	public TableMeta()
	{
		//columns=new ArrayList<ColumnMeta>();
		columns=new LinkedHashMap<String,ColumnMeta>();
		//primaryKeys=new ArrayList<ColumnMeta>();
	}
	
	private Class entityClass;//Type
	
	private String tableName;
	
	private Map<String,ColumnMeta> columns;
	//private List<ColumnMeta> columns;
	
	//private List<ColumnMeta> primaryKeys;//主键	
	
	//public List<ColumnMeta> getPrimaryKeys() {
	//	return primaryKeys;
	//}

	//public void setPrimaryKeys(List<ColumnMeta> primaryKeys) {
	//	this.primaryKeys = primaryKeys;
	//}

	public Class getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, ColumnMeta> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, ColumnMeta> columns) {
		this.columns = columns;
	}
	
//	public List<ColumnMeta> getColumns() {
//		return columns;
//	}
//
//	public void setColumns(List<ColumnMeta> columns) {
//		this.columns = columns;
//	}
	
	
	
}
