package com.tongtech.timp.tmp.service.impl.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tongtech.timp.tmp.orm.ORMUtils;
import com.tongtech.timp.tmp.orm.SQLBuilder;
import com.tongtech.timp.tmp.orm.SQLType;
import com.tongtech.timp.tmp.service.dao.TMPDao;
import com.tongtech.timp.tmp.utils.NullUtils;

/**
 * 基础通用Dao
 * @author Daniel
 *
 * @param <T> Bean类型
 * 
 * @modify 2015-12-14 Daniel 1:新增selectAll(String sql) 通过sql指定查询
 * 
 */
public class SimpleDao<T> implements TMPDao{
	
	private Logger logger = LoggerFactory.getLogger(SimpleDao.class);
	
	protected Connection conn;
	
	@Override
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * 查询信息
	 * @return
	 * @throws Exception
	 */
	public List<T> selectAll() throws Exception
	{
		try
		{
			Type[] types = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments();
			return selectAll((Class)types[0]);
		}catch(Exception ex)
		{
			throw ex;
		}
	}
	
	
	
	/**
	 * 查询信息转换为指定类型，在CommonDao中使用
	 * @return
	 * @throws Exception
	 */
	public List<T> selectAll(Class clas) throws Exception
	{
		return ORMUtils.getResultList(clas, conn);
	}
	
	/**
	 * 执行指定的查询语句
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<T> selectAll(String sql) throws Exception
	{
		try
		{
			Type[] types = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments();
			return ORMUtils.getResultList((Class)types[0], conn,sql);
		}catch(Exception ex)
		{
			throw ex;
		}
	}
	
	
	/**
	 * 查询指定的语句
	 * @param sqlBuilder
	 * @return
	 */
	private List<T> selectAll(Class clas,String select,String where) throws Exception
	{
		//
		
		return null;
	}
	
	/**
	 * 更新实体信息
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int update(T entity) throws Exception
	{
		if(!NullUtils.isNull(entity))
		{
			try
			{
				return executePreparedStatement(ORMUtils.getPreparedStatement(entity, SQLType.update, conn));
			}catch(Exception ex){
				throw ex;
			}
		}
		return -1;
	}
	
	/**
	 * 写入实体信息
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int insert(T entity) throws Exception
	{
		if(!NullUtils.isNull(entity))
		{
			try
			{
				return executePreparedStatement(ORMUtils.getPreparedStatement(entity, SQLType.insert, conn));
			}catch(Exception ex){
				throw ex;
			}
		}
		return -1;
	}
	
	
	
	/**
	 * 删除实体信息
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public int delete(T entity) throws Exception
	{
		if(!NullUtils.isNull(entity))
		{
			try
			{
				return executePreparedStatement(ORMUtils.getPreparedStatement(entity, SQLType.delete, conn));
			}catch(Exception ex){
				throw ex;
			}
		}
		return -1;
	}
	
	//delete Entity where Id=
	//from Entity where Id=
	//select Column from Entity where Id=
	//
	
	/**
	 * 执行指定的SQL语句
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int execteSql(String sql) throws Exception
	{
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
		}catch(Exception ex){
			throw ex;
		}finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("close PreparedStatement error.", e);
                }
            }
        }
		return -1;
	}
	
	/**
	 * 执行指定的SQL语句
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int execteSql(String sql,String... params) throws Exception
	{
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			
			if(params!=null)
			{
				for(int i=0;i<params.length;i++)
				{
					pstmt.setObject(i+1, params[i]);
				}
			}
			
            pstmt.executeUpdate();
		}catch(Exception ex){
			throw ex;
		}finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("close PreparedStatement error.", e);
                }
            }
        }
		return -1;
	}
	
	/**
	 * 执行指定的PreparedStatement，主要针对 Insert Update Delete
	 * @param pStatement
	 * @return
	 * @throws Exception
	 */
	private int executePreparedStatement(PreparedStatement pStatement) throws Exception
	{
		if(pStatement!=null)
		{
			try
			{
				return pStatement.executeUpdate();
			}catch(Exception ex){
				throw ex;
			}finally {
	            if (pStatement != null) {
	                try {
	                	pStatement.close();
	                } catch (SQLException e) {
	                    logger.error("close PreparedStatement error.", e);
	                }
	            }
	        }
		}
		return -1;
	}
	
}
