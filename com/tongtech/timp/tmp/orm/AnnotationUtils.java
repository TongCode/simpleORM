package com.tongtech.timp.tmp.orm;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;

import com.tongtech.timp.tmp.bean.Tcomponent;
import com.tongtech.timp.tmp.bean.TnextId;

/**
 * 注解工具类
 * @author Daniel
 *
 */
public class AnnotationUtils {
	
	/**
	 * 查询指定的类是否存在指定标记
	 * @param clas
	 * @param annClas
	 * @return
	 */
	public static boolean hasAnnotation(Class clas,Class annotationClas)
	{
		return getAnnotation(clas,annotationClas)!=null;
	}
	
	/**
	 * 获取指定的标记
	 * @param clas
	 * @param annotationClas
	 * @return
	 */
	public static Annotation getAnnotation(Class clas,Class annotationClas)
	{
		return clas.getAnnotation(annotationClas);
	}
	
	public static void main(String[] args)
	{
		Tcomponent tobj=new Tcomponent();
		
		TnextId nobj=new TnextId();
		
		System.out.println("has:"+hasAnnotation(tobj.getClass(),Entity.class));
		System.out.println("has:"+hasAnnotation(nobj.getClass(),Entity.class));
		
	}
}
