package com.tongtech.timp.tmp.orm.exception;

/**
 * 异常
 * @author Daniel
 *
 */
public class ORMNoFoundEntityException extends Exception {
	public ORMNoFoundEntityException()
	{
		
	}

	@Override
	public String getMessage() {
		return "不是有效的实体，无法找到@Entity或@Table标记！";
	}
	
	
}
