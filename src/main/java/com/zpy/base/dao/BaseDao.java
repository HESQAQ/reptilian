package com.zpy.base.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;



/**
 * 
 * <p>ClassName:BaseDao</p>
 * <p>Description:所有DAO的基类</p>
 * @author wangxiaobo
 * @date 2015-10-14
 */
@Repository
public abstract class BaseDao<T extends Serializable> {

    @Autowired
	protected SqlSession sqlSession;

	private static Logger logger = Logger.getLogger(BaseDao.class);



    /**
	 * 修改
	 * @param record
	 * @return
	 */
	public int update(Object record) {
		return sqlSession.update(record.getClass().getSimpleName() + ".update", record);
	}


	/**
	 * 修改
	 * @param statementName
	 * @return
	 */
	public int update(String statementName) {
		int rows = sqlSession.update(statementName);
		return rows;
	}

	/**
	 * 修改
	 * @param statementName
	 * @param record
	 * @return
	 */
	public int update(String statementName, Object record) {
		int rows = sqlSession.update(statementName, record);
		return rows;
	}

	/**
	 * 插入
	 * @param entity
	 * @return
	 */
	public int insert(Object entity) {
		int count = sqlSession.insert(entity.getClass().getSimpleName() + ".insert", entity);
		return count;
	}

	/**
	 * 插入
	 * @param statementName
	 * @param entity
	 * @return
	 */
	public int insert(String statementName, Object entity) {
		int count = sqlSession.insert(statementName, entity);
		return count;
	}


	/**
	 * 通过主键删除对象
	 * @param statementName
	 * @param id
	 * @return
	 */
	public int deleteById(String statementName, Long id) {
		return sqlSession.delete(statementName, id);
	}

	/**
	 * 删除
	 * @param <T>
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> int delete(Object entity) {
		int rows = sqlSession.delete(entity.getClass().getSimpleName() + ".delete", entity);
		return rows;
	}

	/**
	 * 删除
	 * @param statementName
	 * @return
	 */
	public int delete(String statementName) {
		int rows = sqlSession.delete(statementName);
		return rows;
	}

	/**
	 * 删除
	 * @param statementName
	 * @param record
	 * @return
	 */
	public int delete(String statementName, Object record) {
		int rows = sqlSession.delete(statementName, record);
		return rows;
	}

	
	/**
	 * 通过主键获取单个对象
	 * @param <T>
	 * @param statementName
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getById(String statementName, String id) {
		return (T) sqlSession.selectOne(statementName, id);
	}
	
	/**
	 * 通过主键获取单个对象
	 * @param <T>
	 * @param record
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getById(Object record, String id) {
		return (T) sqlSession.selectOne(record.getClass().getSimpleName() + ".getById", id);
	}

	/**
	 * 查询
	 * @param statementName
	 * @return
	 */
	public Object getObject(String statementName) {
		return sqlSession.selectOne(statementName);
	}

	/**
	 * 查询
	 * @param statementName
	 * @param object
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T  getObject(String statementName, Object object) {
		return (T)sqlSession.selectOne(statementName, object);
	}

	/**
	 * 查询
	 * @param statementName
	 * @param object
	 * @return
	 */
	@SuppressWarnings("hiding")
	public <T> T getSingleObject(String statementName, Object object) {
		List<T> list = sqlSession.selectList(statementName, object);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	/**
	 * 查询
	 * @param statementName
	 * @param object
	 * @return
	 */
	public List<T> list(String statementName, Object object) {
		return sqlSession.selectList(statementName, object);
	}
	
	/**
	 * 查询
	 * @param object
	 * @return
	 */

	public List<T> list(Object object) {
		return sqlSession.selectList(object.getClass().getSimpleName() + ".findAll", object);
	}


	
	/**
	 * 计算Mybatis查询语句的记录总数
	 * @param statementName
	 * @param parameterObject
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public int queryForCount(final String statementName, final Object parameterObject) throws DataAccessException {
		List list = sqlSession.selectList(statementName, parameterObject);
		int count = null != list && !list.isEmpty() ? list.size() : 0;
		return count;
	}
	

	
	/**
	 * 查询已list对象返回
	 * @param entity
	 * @return
	 */
	public  List<T> findAllList(T entity) {
		return sqlSession.selectList(entity.getClass().getSimpleName() + ".findAll", entity);
	}
	
	
	/**
	 * 
	 * 功能：批量删除对象<br>
	 * @param subListForDel
	 * @param statementName
	 */
	@SuppressWarnings("unchecked")
	public void batchDelete(final List subListForDel, final String statementName) throws Exception {
		try {
            sqlSession.delete(statementName, subListForDel);
		} catch (Exception e) {
			logger.error(e);
			throw e;// 必须抛异常才能回滚。
		}
	}

}