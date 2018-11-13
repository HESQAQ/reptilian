package com.zpy.base.domian;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * 
 * <p>ClassName:Base</p>
 * <p>Description:所有controller,service的基类</p>
 * <p>为了更方便的对某些方法的调用，把啊常用的一些方法直接写到整个系统的基类中</p>
 * @author wangxiaobo
 * @date 2015-11-23
 */

public abstract class Base<T> {
	
	/**
	 * list对象转化成json对象
	 * @param list
	 * @return
	 */
	public String listToJson(List<T> list){
		JSONArray json = new JSONArray();
		for (T object : list) {
			json.add((T)object);
		}
		return json.toString();
	}
}
