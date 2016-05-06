package com.tongtech.timp.tmp.orm;
/**
 * Sql语句构建
 * @author Daniel
 *
 */
public class SQLBuilder {
	
	private SQLBuilder(){}
	
	///*************///构建语句属性代码
	private Class entityClass;
	
	/**
	 * 通过Class构建
	 * @param clas
	 * @return
	 */
	public static SQLBuilder build(Class clas)
	{
		SQLBuilder builder=new SQLBuilder();
		builder.entityClass=clas;
		return builder;
	}
	
	/**
	 * 通过实体构建
	 * @param entity
	 * @return
	 */
	public static SQLBuilder build(Object entity)
	{
		return build(entity.getClass());
	}
	
	
	/**
	 * 输出Insert SQL语句
	 */
	public String toInsertSQL()
	{
		return null;
	}
	
	/**
	 * 输出Update SQL语句
	 */
	public String toUpdateSQL()
	{
		return null;
	}
	
	/**
	 * 输出Delete SQL语句
	 */
	public String toDeleteSQL()
	{
		return null;
	}
	
	/**
	 * 输出Select SQL语句
	 */
	public String toSelectSQL()
	{
		return null;
	}
	
	/**
	 * Select条件
	 * @param sql
	 * @return
	 */
	public SQLBuilder Select(String sql)
	{
		return this;
	}
	
	///*************///构建语句Where代码
	/**
	 * Where条件
	 * @param param
	 * @return
	 */
	public SQLBuilder Where(String sql)
	{
		
		return this;
	}
	
	/**
	 * Where And 
	 * @param filder
	 * @return
	 */
	public SQLBuilder WhereAnd(String sql)
	{
		
		return this;
	}
	
	/**
	 * Where Or 
	 * @param filder
	 * @return
	 */
	public SQLBuilder WhereOr(String sql)
	{
		return this;
	}
	
	/**
	 * Where In 
	 * @param filder
	 * @return
	 */
	public SQLBuilder WhereIn(String sql,String[] values)
	{
		
		return this;
	}
	
	/**
	 * Where Not In 
	 * @param filder
	 * @return
	 */
	public SQLBuilder WhereNotIn(String sql,String[] values)
	{
		
		return this;
	}
	
	
	
}
