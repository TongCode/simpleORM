package com.tongtech.timp.tmp.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongtech.timp.tmp.bean.Tcomponent;

/**
 * 实现简单的ORM功能，减少SQL编写
 * @author Daniel
 *
 */
public class ORMUtils {
	
	private static Logger logger=LoggerFactory.getLogger(ORMUtils.class);
	
	
	
	private static Map<String,String> cacheSQLs=new HashMap<String,String>();
	private static Map<Class,ValueUtils> cacheValueUtils=new  HashMap<Class,ValueUtils>();
	
	/**
	 * 获取PreparedStatement
	 * @param clas
	 * @param type
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static PreparedStatement getPreparedStatement(Object obj,SQLType type,Connection connection) throws Exception
	{
		//生成语句，注入参数，执行结果
		String sql=toSql(obj.getClass(),type);
		logger.debug("[ORM]Sql:"+sql);
		if(sql!=null)
		{
			PreparedStatement pStatement=connection.prepareStatement(sql);
			
			List<ColumnMeta> params=MetaCache.getParams(obj.getClass(),type);
			
			if(params!=null&&params.size()>0)
			{
				for(int i=0;i<params.size();i++)
				{
					ColumnMeta columnMeta=params.get(i);
					
					//设置参数
					setPreparedStatementValues(obj,i+1,pStatement,columnMeta);
				}
			}
			
			return pStatement;
		}
		return null;
		
	}
	
	public static PreparedStatement getPreparedStatement(Object obj,SQLType type,Connection connection,String where) throws Exception
	{
		return null;
	}
	
	
	
	/**
	 * 执行查询并获得结果
	 * @param clas
	 * @return
	 * @throws Exception 
	 */
	public static List getResultList(Class clas,Connection connection) throws Exception
	{
		if(clas!=null)
		{
			//生成语句，转换结果
			String sql=toSql(clas,SQLType.select);
			return getResultList(clas,connection,sql);
		}		
		return null;
	}
	
	/**
	 * 可以通过条件查询
	 * @param clas
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public static List getResultList(Class clas,Connection connection,String select,String where) throws Exception
	{
		if(clas!=null)
		{
			//生成语句，转换结果
			String sql=toSql(clas,SQLType.select);
			//如果存在处理
			if(where!=null&&where.trim().length()>0)
			{
				sql+=" "+where;
			}
			return getResultList(clas,connection,sql);
			
		}		
		return null;
	}
	
	/**
	 * 执行查询语句
	 * @param clas
	 * @param connection
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List getResultList(Class clas,Connection connection,String sql) throws Exception{
		if(clas!=null)
		{
			logger.debug("[ORM]Sql:"+sql);
			if(sql!=null)
			{
				PreparedStatement pStatement=null;
				ResultSet rs=null;
				List resultList=null;
				try
				{
					pStatement=connection.prepareStatement(sql);
					rs=pStatement.executeQuery();
					
					//数据表模型
					TableMeta tableMeta=MetaCache.getTable(clas);//cacheTables.get(clas);
					resultList=new LinkedList();
					while (rs.next()) {
						
						//Class创建实体
						Object obj=clas.newInstance();
						
						for(ColumnMeta columnMeta : tableMeta.getColumns().values())
						{
							//根据类型获取函数
							columnMeta.getFieldSetMethod().invoke(obj, getResultSetValue(rs,columnMeta));
						}
						
						resultList.add(obj);
					}
					
					return resultList;
					
				}catch(SQLException ex){
					throw ex;
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}finally{
					if(rs!=null)
						rs.close();
					if(pStatement!=null)
						pStatement.close();
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据类型读取指定的参数值
	 * @param rs
	 * @param columnMeta
	 * @return
	 * @throws SQLException
	 */
	private static Object getResultSetValue(ResultSet rs,ColumnMeta columnMeta) throws SQLException
	{
		Object value=null;
		
		if(columnMeta.getFieldClass()==java.lang.String.class)
		{
			value=rs.getString(columnMeta.getColName());
		}
		else if(columnMeta.getFieldClass()==java.lang.Integer.class||columnMeta.getFieldClass()==int.class)
		{
			value=rs.getInt(columnMeta.getColName());
		}
		else if(columnMeta.getFieldClass()==java.util.Date.class)
		{
			value=rs.getString(columnMeta.getColName());
			if(value!=null&&!value.toString().equals("null"))
			{
				value=str2Date(value.toString());
			}
		}
		
		return value;
	}	
	
	/**
	 * 依据类型设置指定的参数值
	 * @param obj
	 * @param index
	 * @param pStatement
	 * @param columnMeta
	 * @throws SQLException
	 */
	private static void setPreparedStatementValues(Object obj,int index,PreparedStatement pStatement,ColumnMeta columnMeta) throws SQLException
	{
		//根据类型获取值并赋值
		Class clas=columnMeta.getFieldClass();
		
		if(clas== java.lang.String.class)
		{
			ValueUtils<String> valueUtil=null;
			if(cacheValueUtils.containsKey(java.lang.String.class))
				valueUtil=cacheValueUtils.get(java.lang.String.class);
			else
				cacheValueUtils.put(java.lang.String.class,valueUtil=new ValueUtils<String>());
			
			String value=valueUtil.getValue(obj, columnMeta);
			logger.debug("[ORM]Index:"+index+",Value:"+value);
			//System.out.println("[ORM]Index:"+index+",Value:"+value);
			pStatement.setString(index, value);
		}
		else if(clas== java.lang.Integer.class||clas==int.class)
		{
			ValueUtils<Integer> valueUtil=null;
			if(cacheValueUtils.containsKey(java.lang.Integer.class))
				valueUtil=cacheValueUtils.get(java.lang.Integer.class);
			else
				cacheValueUtils.put(java.lang.Integer.class,valueUtil=new ValueUtils<Integer>());
			Integer value = valueUtil.getValue(obj, columnMeta);
			logger.debug("[ORM]Index:"+index+",Value:"+value);
			pStatement.setInt(index,value==null?0:value);
			
		}
		else if(clas== java.lang.Double.class)
		{
			ValueUtils<Double> valueUtil=null;
			if(cacheValueUtils.containsKey(java.lang.Double.class))
				valueUtil=cacheValueUtils.get(java.lang.Double.class);
			else
				cacheValueUtils.put(java.lang.Double.class,valueUtil=new ValueUtils<Double>());
			pStatement.setDouble(index, valueUtil.getValue(obj, columnMeta));
			
		}
		else if(clas== java.util.Date.class)
		{
			ValueUtils<Date> valueUtil=null;
			if(cacheValueUtils.containsKey(java.util.Date.class))
				valueUtil=cacheValueUtils.get(java.util.Date.class);
			else
				cacheValueUtils.put(java.util.Date.class,valueUtil=new ValueUtils<Date>());
			
			Date value=valueUtil.getValue(obj, columnMeta);
			logger.debug("[ORM]Index:"+index+",Value:"+value);
			if(value==null)
				pStatement.setString(index, null);
			else
				pStatement.setString(index, date2Str(value));
			
		}
		else
		{
			System.out.println("无效类型："+columnMeta.getFieldClass());
		}
		
	}
	
	
	/**
	 * 转换指定对象为SQL
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	public static String toSql(Class clas,SQLType type) throws Exception
	{
		if(clas!=null)
		{
			TableMeta tableMeta=MetaCache.getTable(clas);
			
			//获取指定语句
			String sqlKey=clas+"$$"+type;
			if(cacheSQLs.containsKey(sqlKey))
				return cacheSQLs.get(sqlKey);
			
			String sql=null;
			List<ColumnMeta> params=new LinkedList<ColumnMeta>();
			if(type==SQLType.insert)
			{ 
				sql=SQLCreateUtils.getInsertSql(tableMeta,params);
			}
			else if(type==SQLType.select)
			{
				sql=SQLCreateUtils.getSelectSql(tableMeta);
			}
			else if(type==SQLType.update)
			{
				sql=SQLCreateUtils.getUpdateSql(tableMeta,params);
			}
			else if(type==SQLType.delete)
			{				
				sql=SQLCreateUtils.getDeleteSql(tableMeta,params);				
			}
			cacheSQLs.put(sqlKey, sql);
			if(params.size()>0)
				MetaCache.addParams(sqlKey, params);
			return sql;
			
		}
		return null;
	}
	
	/**
	 * 当前版本仅处理Where语句
	 * @param clas
	 * @param tql
	 * @return
	 * @throws Exception
	 */
	private static String toSql(Class clas,String tql) throws Exception
	{
		//field=xxx and field in () or field=XXX
		//获取指定的TableMeta
		TableMeta tableMeta=MetaCache.getTable(clas);
		
		
		tql=tql.toLowerCase();
		
		int odIndex=-1;
		if((odIndex=tql.indexOf("order by"))>-1)//存在排序
		{
			//处理排序逻辑
			String odsql=tql.substring(odIndex+9);
			if(odsql.indexOf(",")>0)//存在多个条件
			{
				
			}
			else
			{
				//if()
			}
		}
		
		StringBuffer sqlBuffer=new StringBuffer();
		
		//处理语句条件
		String[] tqls=tql.substring(0,odIndex).split("=");
		
		for(String tqlItem : tqls)
		{
			tqlItem=tqlItem.trim();
			
			System.out.println(tqlItem);
			int bsIndex=-1;
			if((bsIndex=tqlItem.lastIndexOf(" "))>-1)
			{
				//存在
				String fName=tqlItem.substring(bsIndex+1);
				ColumnMeta cm=tableMeta.getColumns().get(fName);
				if(cm!=null)
				{
					sqlBuffer.append(tqlItem.substring(0,bsIndex+1));
					sqlBuffer.append(cm.getColName()).append(" = ");
				}else
					sqlBuffer.append(tqlItem);
			}
			else
			{
				//不存在
				ColumnMeta cm=tableMeta.getColumns().get(tqlItem);
				if(cm!=null)
					sqlBuffer.append(cm.getColName()).append(" = ");
				else
					sqlBuffer.append(tqlItem);
			}
		}
		
		
		return sqlBuffer.toString();
	}
	
	private static SimpleDateFormat defaultsdf=null;
	/**
	 * 日期类型转换为字符串
	 * @param date
	 * @return
	 */
	private static Date str2Date(String date)
	{
		if(date==null||date.length()<=0)return null;
		//if(defaultsdf==null)defaultsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 字符串转换为日期类型
	 * @param date
	 * @return
	 */
	private static String date2Str(Date value)
	{
		if(value==null)return null;
		//if(defaultsdf==null)defaultsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			Tcomponent tobj=new Tcomponent();
			
			
			//生成语句，转换结果
			String sql=toSql(tobj.getClass(),SQLType.select);
			
			String where ="order by id desc";
			
			//如果存在处理
			if(where!=null&&where.trim().length()>0)
			{
				sql+=" "+where;
			}
			
			System.out.println(sql);
			
//			System.out.println("Insert："+toSql(tobj.getClass(),SQLType.insert));
//			System.out.println("Select："+toSql(tobj.getClass(),SQLType.select));
//			System.out.println("Update："+toSql(tobj.getClass(),SQLType.update));
//			System.out.println("Delete："+toSql(tobj.getClass(),SQLType.delete));
//			
//			//获取Update需要的参数列表
//			List<ColumnMeta> params=MetaCache.getParams(tobj.getClass(),SQLType.update);
//			System.out.println("更新需要参数："+params.size());
//			for(int i=0;i<params.size();i++)
//			{
//				System.out.println("Index:"+i+",Name:"+params.get(i).getFieldName());
//			}		
			
//			String s=toSql(tobj.getClass(),"componentName='111' and registerDate='222' order by registerDate desc");
//			System.out.println(s);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	
}
