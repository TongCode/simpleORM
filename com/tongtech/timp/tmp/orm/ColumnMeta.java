package com.tongtech.timp.tmp.orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 列数据描述
 * @author Daniel
 *
 */
public class ColumnMeta {
	
	private String ColName;
	
	private String fieldName;
	private Class fieldClass;//Type
	
	private Method fieldGetMethod;
	private Method fieldSetMethod;
	
	private boolean isPrimaryKey;//PK
	
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getColName() {
		return ColName;
	}
	public void setColName(String colName) {
		ColName = colName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Class getFieldClass() {
		return fieldClass;
	}
	public void setFieldClass(Class fieldClass) {
		this.fieldClass = fieldClass;
	}
	public Method getFieldGetMethod() {
		return fieldGetMethod;
	}
	public void setFieldGetMethod(Method fieldGetMethod) {
		this.fieldGetMethod = fieldGetMethod;
	}
	public Method getFieldSetMethod() {
		return fieldSetMethod;
	}
	public void setFieldSetMethod(Method fieldSetMethod) {
		this.fieldSetMethod = fieldSetMethod;
	}


}
