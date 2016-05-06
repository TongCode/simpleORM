package com.tongtech.timp.tmp.orm;

import java.util.LinkedList;
import java.util.List;

/**
 * SQL基本默认语句创建工具类
 * @author Daniel
 *
 */
public class SQLCreateUtils {
	
	/**
	 * 根据指定的表生成写入语句
	 * @param tableMeta
	 * @return
	 */
	public static String getInsertSql(TableMeta tableMeta,List<ColumnMeta> outParams)
	{
		//存在可用的列
		if(tableMeta!=null&&tableMeta.getColumns().size()>0)
		{
			//insert into TableName (Columns) values (Values)
			StringBuffer sqlBuffer=new StringBuffer("insert into ");
			sqlBuffer.append(tableMeta.getTableName());
			
			//列值处理
			StringBuffer columnsBuffer=new StringBuffer("(");
			StringBuffer valuessBuffer=new StringBuffer("(");
			
			for(ColumnMeta columnMeta : tableMeta.getColumns().values())
			{
				columnsBuffer.append(columnMeta.getColName());
				columnsBuffer.append(",");
				//String values=new ValueUtils<String>().getValue(entity, columnMeta);
				//valuessBuffer.append("'");
				//valuessBuffer.append(values);
				//valuessBuffer.append("',");
				valuessBuffer.append("?,");
				
				outParams.add(columnMeta);
			}
			
			//处理完成
			columnsBuffer.deleteCharAt(columnsBuffer.length()-1);
			valuessBuffer.deleteCharAt(valuessBuffer.length()-1);
			
			columnsBuffer.append(")");
			valuessBuffer.append(")");
			
			sqlBuffer.append(columnsBuffer);
			sqlBuffer.append(" values ");
			sqlBuffer.append(valuessBuffer);
			
			return sqlBuffer.toString();
		}
		return null;
	}
	
	/**
	 * 根据指定的表生成查询语句
	 * @param tableMeta
	 * @return
	 */
	public static String getSelectSql(TableMeta tableMeta)
	{
		//存在可用的列
		if(tableMeta!=null&&tableMeta.getColumns().size()>0)
		{
			//select Columns from TableName
			StringBuffer sqlBuffer=new StringBuffer("select ");
			
			//列值处理
			StringBuffer columnsBuffer=new StringBuffer();
			
			for(ColumnMeta columnMeta : tableMeta.getColumns().values())
			{
				columnsBuffer.append(columnMeta.getColName());
				columnsBuffer.append(",");
			}
			
			//处理完成
			columnsBuffer.deleteCharAt(columnsBuffer.length()-1);
			
			sqlBuffer.append(columnsBuffer);
			sqlBuffer.append(" from ");
			sqlBuffer.append(tableMeta.getTableName());
			
			return sqlBuffer.toString();
		}
		return null;
	}

	/**
	 * 根据指定的表生成更新语句
	 * @param tableMeta
	 * @return
	 */
	public static String getUpdateSql(TableMeta tableMeta,List<ColumnMeta> outParams)
	{
		//存在可用的列
		if(tableMeta!=null&&tableMeta.getColumns().size()>0)
		{
			//update TableName set Column=Value, where PK=?
			StringBuffer sqlBuffer=new StringBuffer("update ");
			sqlBuffer.append(tableMeta.getTableName());
			sqlBuffer.append(" set ");
			
			//列值处理
			StringBuffer columnsBuffer=new StringBuffer();
			StringBuffer whereBuffer=new StringBuffer();
			
			List<ColumnMeta> whereParams=new LinkedList<ColumnMeta>();
			
			for(ColumnMeta columnMeta : tableMeta.getColumns().values())
			{
				//主键列处理
				if(columnMeta.isPrimaryKey())
				{
					if(whereBuffer.length()==0)
						whereBuffer.append(" where ");
					else
						whereBuffer.append(" and ");

					whereBuffer.append(columnMeta.getColName());
					whereBuffer.append("=? ");
					
					whereParams.add(columnMeta);
				}
				else
				{
					columnsBuffer.append(columnMeta.getColName());
					columnsBuffer.append("=?,");
					
					outParams.add(columnMeta);
				}
			}
			
			//处理完成
			columnsBuffer.deleteCharAt(columnsBuffer.length()-1);
			
			sqlBuffer.append(columnsBuffer);
			sqlBuffer.append(whereBuffer);
			
			outParams.addAll(whereParams);
			whereParams.clear();
			
			return sqlBuffer.toString();
		}
		return null;
	}
	
	/**
	 * 根据指定的表生成删除语句
	 * @param tableMeta
	 * @return
	 */
	public static String getDeleteSql(TableMeta tableMeta,List<ColumnMeta> outParams)
	{
		//存在可用的列
		if(tableMeta!=null&&outParams!=null&&tableMeta.getColumns().size()>0)
		{
			//delete from TableName where PK=?
			StringBuffer sqlBuffer=new StringBuffer("delete from ");
			sqlBuffer.append(tableMeta.getTableName());
			
			//列值处理
			StringBuffer whereBuffer=new StringBuffer();
			for(ColumnMeta columnMeta : tableMeta.getColumns().values())
			{
				//主键列处理
				if(columnMeta.isPrimaryKey())
				{
					if(whereBuffer.length()==0)
						whereBuffer.append(" where ");
					else
						whereBuffer.append(" and ");

					whereBuffer.append(columnMeta.getColName());
					whereBuffer.append("=? ");
					
					outParams.add(columnMeta);
				}
			}
			
			sqlBuffer.append(whereBuffer);
			
			
			return sqlBuffer.toString();
		}
		return null;
	}
	
}
