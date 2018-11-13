package com.zpy.QButil;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;

/**
 * 数据库查询器
 * 
 * @author Alfred
 *
 */
public class DBQuerier {

    private QueryRunner runner;
    private boolean available;
    private ComboPooledDataSource dataSource;

    private static Logger logger = LoggerFactory.getLogger(DBQuerier.class);

    private static DBQuerier instance = new DBQuerier();

    public static DBQuerier getInstance() {
        return instance;
    }
    
    private DBQuerier() {
        available = init();
    }
    
    public boolean isAvailable() {
        return available;
    }

    public QueryRunner getQueryRunner() {
        return runner;
    }

    private boolean init() {
        logger.info("init_db");
        dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
        } catch (PropertyVetoException e) {
            logger.error(e.toString());
            logger.error("init_db_fail:cause=BAD_DATASOURCE_CONFIG");
            return false;
        }
        dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:ORCL");
        dataSource.setUser("zpy");
        dataSource.setPassword("zpy");
        dataSource.setInitialPoolSize(Integer.parseInt("3"));
        dataSource.setMaxPoolSize(Integer.parseInt("30"));
        dataSource.setMinPoolSize(Integer.parseInt("3"));
        dataSource.setMaxIdleTime(Integer.parseInt("120"));
        dataSource.setIdleConnectionTestPeriod(Integer.parseInt("60"));
        dataSource.setMaxStatements(Integer.parseInt("0"));
        dataSource.setTestConnectionOnCheckout(true);
        runner = new QueryRunner(dataSource);
        logger.info("连接成功");
        return true;
    }
}
