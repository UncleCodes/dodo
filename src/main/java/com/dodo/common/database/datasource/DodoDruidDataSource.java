package com.dodo.common.database.datasource;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.dodo.common.framework.destroyer.Destroyable;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoDruidDataSource extends DruidDataSource implements Destroyable {
    private static final Logger LOGGER           = LoggerFactory.getLogger(DodoDruidDataSource.class);
    private static final long   serialVersionUID = -3355408747188080038L;

    @PostConstruct
    public void init() throws SQLException {
        super.init();
    }

    @Override
    public void destroy() {
        super.close();
        ClassLoader webappClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == webappClassLoader) {
                try {
                    LOGGER.info("Deregistering JDBC driver {}", driver);
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    LOGGER.error("Error deregistering JDBC driver {}", driver, ex);
                }
            } else {
                LOGGER.info("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader",
                        driver);
            }
        }

        //  关闭mysql的cleanup线程
        LOGGER.info("{Mysql cleanup thread} closing..............");
        AbandonedConnectionCleanupThread.checkedShutdown();
        LOGGER.info("{Mysql cleanup thread} closed..............");
    }
}
