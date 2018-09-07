/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfs.datareward.apigw.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author TrisTDT
 */
public abstract class BaseDbAccess {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDbAccess.class);

    protected DataSource dataSource;
    protected Properties properties = new Properties();

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            dataSource = setupDataSource();
        }

        Connection conn = dataSource.getConnection();

        return conn;
    }

    protected synchronized DataSource setupDataSource() {
        try {
            LOGGER.info("Setting up datasource");

            String url = properties.getProperty("url", "jdbc:oracle:thin:@localhost:mca");
            String classDriverName = properties.getProperty("classDriverName", "oracle.jdbc.OracleDriver");
            String user = properties.getProperty("user", "livescreen");
            String password = properties.getProperty("password", "livescreen");

            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setTestConnectionOnCheckin(Boolean.parseBoolean(properties.getProperty("testConnectionOnCheckin", "true")));
            cpds.setTestConnectionOnCheckout(Boolean.parseBoolean(properties.getProperty("testConnectionOnCheckout", "false")));
            cpds.setIdleConnectionTestPeriod(Integer.parseInt(properties.getProperty("idleConnectionTestPeriod", "15")));
            cpds.setMaxConnectionAge(Integer.parseInt(properties.getProperty("maxConnectionAge", "0")));
            cpds.setProperties(properties);
            cpds.setDriverClass(classDriverName);
            cpds.setJdbcUrl(url);
            cpds.setUser(user);
            cpds.setPassword(password);

            return cpds;
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
    }

    @Override
    public synchronized void finalize() throws Throwable {
        super.finalize();
        DataSources.destroy(dataSource);
        dataSource = null;
        LOGGER.info("DB Connection Pool destroyed");
    }

    public synchronized void setProperties(Properties properties) {
        if (dataSource == null) {
            this.properties = properties;
            dataSource = setupDataSource();
        }
    }

}
