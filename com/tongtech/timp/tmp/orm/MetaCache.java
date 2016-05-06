package com.tongtech.timp.tmp.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongtech.timp.tmp.orm.exception.ORMNoFoundEntityException;

/**
 * 元数据管理类
 * 主要负责管理Table和Column元数据
 * @author Daniel
 *
 */
class MetaCache {	
	
	private static Logger logger=LoggerFactory.getLogger(MetaCache.class);
	
	//构建相关数据缓存
	private static Map<Class,TableMeta> cacheTables=new HashMap<Class,TableMeta>();//数据表结构缓存
	
	private static Map<String,List<ColumnMeta>> cacheParams=new HashMap<String,List<ColumnMeta>>();//
	
	/**
	 * 获取指定的表结构元数据
	 * @param clas
	 * @return
	 * @throws Exception 
	 */
	public static TableMeta getTable(Class clas) throws Exception
	{
		if(cacheTables.containsKey(clas))
		{
			logger.debug("Entity Class "+clas+" is exist.");
			return cacheTables.get(clas);
		}
		
		//构建生成缓存
		TableMeta createNewTableMeta=createTableMeta(clas);
		logger.debug("Create Entity Class "+clas+" Meta.");
		if(createNewTableMeta!=null)//存在标记
		{
			cacheTables.put(clas, createNewTableMeta);
		}
		else
			throw new ORMNoFoundEntityException();
		
		return createNewTableMeta;
	}
	
	
	/**
	 * 获取需要的参数
	 * @param clas
	 * @param type
	 * @return
	 */
	public static List<ColumnMeta> getParams(Class clas,SQLType type)
	{
		String sqlKey=clas+"$$"+type;
		if(cacheParams.containsKey(sqlKey))
			return cacheParams.get(sqlKey);
		return null;
	}
	
	/**
	 * 缓存需要的参数
	 * @param sqlkey
	 * @param params
	 */
	public static void addParams(String sqlkey,List<ColumnMeta> params)
	{
		cacheParams.put(sqlkey, params);
	}
	
	/**
	 * 转换指定的实体
	 * @param clas
	 * @throws Exception 
	 */
	private static TableMeta createTableMeta(Class clas) throws Exception
	{
		if(AnnotationUtils.hasAnnotation(clas,Entity.class)&&AnnotationUtils.hasAnnotation(clas,Table.class))
		{
			TableMeta tableMeta=new TableMeta();
			//读取TableName
			Table tableAnnotation=(Table)AnnotationUtils.getAnnotation(clas, Table.class);
			tableMeta.setTableName(tableAnnotation.name());
			
			//List<ColumnMeta> columnMetas=createColumnMetas(clas,tableMeta);
			Map<String,ColumnMeta> columnMetas=createColumnMetas(clas,tableMeta);
			if(columnMetas!=null)
				tableMeta.setColumns(columnMetas);			
			return tableMeta;
		}
		return null;
	}
	
	/**
	 * 转换指定的实体字段
	 * @param clas
	 * @return
	 * @throws Exception 
	 */
	private static Map<String,ColumnMeta> createColumnMetas(Class clas,TableMeta tableMeta) throws Exception//List<ColumnMeta>
	{
		//遍历处理实体字段，获取字段的注解信息
		Field[] fields = clas.getDeclaredFields();
		//List<ColumnMeta> columnMetas=null;
		Map<String,ColumnMeta> columnMetas=null;
		if(fields!=null&&fields.length>0)
		{
			//columnMetas=new ArrayList<ColumnMeta>();
			columnMetas=new LinkedHashMap<String,ColumnMeta>();
			for(Field itemField : fields)
			{
				Annotation[] fieldAnnotations=itemField.getAnnotations();
				if(fieldAnnotations!=null&&fieldAnnotations.length>0)
				{
					ColumnMeta columnMeta = new ColumnMeta();
					//基本属性设置
					columnMeta.setFieldName(itemField.getName());
					columnMeta.setFieldClass(itemField.getType());
					//columnMetas.add(columnMeta);
					columnMetas.put(itemField.getName(), columnMeta);
					
					String methodName=itemField.getName().substring(0,1).toUpperCase()+itemField.getName().substring(1);
					
					String getMethodName="get";
					if(itemField.getType()==java.lang.Boolean.class)
					{
						getMethodName="is";
					}
					getMethodName+=methodName;
					try {
						columnMeta.setFieldGetMethod(clas.getMethod(getMethodName, null));
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					
					String setMethodName="set";
					setMethodName+=methodName;
					try {
						columnMeta.setFieldSetMethod(clas.getMethod(setMethodName, columnMeta.getFieldClass()));
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					
					//注解处理
					for(Annotation annotation : fieldAnnotations)
					{
						if(annotation instanceof Id)//处理主键
						{
							//tableMeta.getPrimaryKeys().add(columnMeta);
							columnMeta.setPrimaryKey(true);
						}
						else if(annotation instanceof Column)//处理列信息
						{
							Column column = (Column)annotation;
							String name=column.name();
							if(name!=null&&name.trim().length()>0)
								columnMeta.setColName(name);
							else
								throw new Exception("实体的"+itemField.getName()+"属性注解name无效！");
						}
					}					
				}
			}
		}
		
		return columnMetas;
	}
	
}
