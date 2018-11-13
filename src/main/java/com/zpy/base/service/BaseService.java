package com.zpy.base.service;

import java.io.Serializable;
import java.util.List;

import com.zpy.base.dao.BaseDao;
import com.zpy.base.domian.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * 
 * <p>ClassName:BaseService</p>
 * <p>Description:所有Service的基类</p>
 * @author libo
 * @date 2017-11-15
 */
@Repository("baseService")
public abstract class BaseService<T extends Serializable>  extends Base {

	@Autowired
	private BaseDao<T> baseDao;

	
	/**
	 * 修改
	 * @param record
	 * @return
	 */
	public int update(Object record) {
		return baseDao.update(record);
	}


	/**
	 * 修改
	 * @param statementName
	 * @return
	 */
	public int update(String statementName) {
		return baseDao.update(statementName);
	}

	/**
	 * 修改
	 * @param statementName
	 * @param record
	 * @return
	 */
	public int update(String statementName, Object record) {
		return baseDao.update(statementName, record);
	}

	/**
	 * 插入
	 * @param entity
	 * @return
	 */
	public int insert(Object entity) {
		return baseDao.insert(entity);
	}

	/**
	 * 插入
	 * @param statementName
	 * @param entity
	 * @return
	 */
	public int insert(String statementName, Object entity) {
		return baseDao.insert(statementName, entity);
	}


	/**
	 * 通过主键删除对象
	 * @param statementName
	 * @param id
	 * @return
	 */
	public int deleteById(String statementName, Long id) {
		return baseDao.deleteById(statementName, id);
	}

	/**
	 * 删除
	 * @param <T>
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> int delete(Object entity) {
		return baseDao.delete(entity);
	}

	/**
	 * 删除
	 * @param statementName
	 * @return
	 */
	public int delete(String statementName) {
		return baseDao.delete(statementName);
	}

	/**
	 * 删除
	 * @param statementName
	 * @param record
	 * @return
	 */
	public int delete(String statementName, Object record) {
		return baseDao.delete(statementName, record);
	}

	
	/**
	 * 通过主键获取单个对象
	 * @param
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getById(String statementName, String id) {
		return (T) baseDao.getById(statementName, id);
		
	}
	
	/**
	 * 通过主键获取单个对象
	 * @param
	 * @param id
	 * @return
	 */

	@SuppressWarnings({ "hiding", "unchecked" })
	public <T> T getById(Object record, String id) {
		return (T)  baseDao.getById(record, id);
	}

	/**
	 * 查询
	 * @param statementName
	 * @return
	 */
	public Object getObject(String statementName) {
		return baseDao.getObject(statementName);
		
	}

	/**
	 * 查询
	 * @param statementName
	 * @param object
	 * @return
	 */
	@SuppressWarnings({ "hiding", "unchecked" })
	public <T> T  getObject(String statementName, Object object) {
		return (T) baseDao.getObject(statementName, object);
		
	}

	/**
	 * 查询
	 * @param statementName
	 * @param object
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getSingleObject(String statementName, Object object) {
		return (T) baseDao.getSingleObject(statementName, object);
	}


	/**
	 * 查询
	 * @param statementName
	 * @param object
	 * @return
	 */
	public List<T> list(String statementName, Object object) {
		return baseDao.list(statementName, object);
	}
	
	/**
	 * 查询
	 * @param
	 * @param object
	 * @return
	 */
	public List<T> list(Object object) {
		return baseDao.list(object);
	}



	
	/**
	 * 查询已list对象返回
	 * @param entity
	 * @return
	 */
	public  List<T> findAllList(T entity) {
		return baseDao.findAllList(entity);
	}
	
	
	/**
	 * 
	 * 功能：批量删除对象<br>
	 * @param subListForDel
	 * @param statementName
	 */
	@SuppressWarnings("unchecked")
	public void batchDelete(final List subListForDel, final String statementName) throws Exception {
		baseDao.batchDelete(subListForDel, statementName);
	}

}