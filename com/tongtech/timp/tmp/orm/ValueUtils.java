package com.tongtech.timp.tmp.orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 获取指定的值
 * @author Daniel
 *
 * @param <T>
 */
public class ValueUtils<T> {

	/**
	 * 获取值
	 * @param obj
	 * @param columnMeta
	 * @return
	 */
	public T getValue(Object obj,ColumnMeta columnMeta)
	{
		Method fieldMethod=null;
		if(obj!=null&&columnMeta!=null&&(fieldMethod=columnMeta.getFieldGetMethod())!=null)
		{
			try {
				return (T)fieldMethod.invoke(obj,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
