/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfs.datareward.apigw.database;

import com.mfs.datareward.apigw.config.ConfigManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author TrisTDT
 */
public class OraClientBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(OraClientBase.class);
    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private String url_db = "";
    private String user_db = "";
    private String pass_db = "";

    private static final String ORA_DATE_FORMAT_DEFAULT = "yyyymmdd hh24miss";
    private static final String CONNECTION_NOT_ESTABLISH = "Connection is not established.";

    private static class SQL_TEMPLATE {

        public static final String TEST_CONNECTION = "select 1 from dual";

    }

    protected Object m_lock_conn = new Object();
    protected Connection m_conn = null;

    public OraClientBase(String url, String user, String password) {
        this.url_db = url;
        this.user_db = user;
        this.pass_db = password;

    }

    public void create() {
        try {
            Properties properties = new Properties();
            properties.setProperty("url", ConfigManager.DB_URL);
            properties.setProperty("classDriverName", ConfigManager.DBClassDriverName);
            properties.setProperty("password", ConfigManager.DB_Pass);
            properties.setProperty("testConnectionOnCheckin", "true");
            properties.setProperty("testConnectionOnCheckout", "true");
            properties.setProperty("idleConnectionTestPeriod", "15");
            properties.setProperty("maxConnectionAge", "15");

            DbAccess.getInstance().setProperties(properties);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    public void destroy() {

    }

    public void recreate() {
        try {
            LOGGER.info("Start Destroy connection...");
            destroy();
            LOGGER.info("End Destroy connection...");
            Thread.sleep(5 * 1000);

            LOGGER.info("Start Create connection...");
            create();
            LOGGER.info("End create connection...");
        } catch (InterruptedException ex) {
            LOGGER.error("", ex);
        }
    }

    public Connection getOraConnection() {
        try {
            return DbAccess.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.error("", e);
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException ex) {
                LOGGER.error("", ex);
            }
            return getOraConnection();
        }
    }

    public void testOraConnection() throws SQLException {
        Connection c = getOraConnection();
        if (c == null) {
            throw new SQLException(CONNECTION_NOT_ESTABLISH);
        }
        Statement statement = null;
        try {
            statement = c.createStatement();
            statement.execute(SQL_TEMPLATE.TEST_CONNECTION);

        } catch (SQLException e) {
            statement.close();
            if (null != c) {
                c.close();
            }
        }
    }

    public boolean isOraDisconnected() {
        try {
            testOraConnection();
        } catch (SQLException e) {
            return true;
        }
        return false;
    }

    public void set_nls_date_format(String datetime) throws SQLException {
        String sql = "alter session set nls_date_format = " + "'" + datetime + "'";

        Connection c = getOraConnection();
        try {
            if (c == null) {
                throw new SQLException(CONNECTION_NOT_ESTABLISH);
            }

            try (Statement statement = c.createStatement()) {
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    
    public void set_nls_date_format_default() throws SQLException{
        set_nls_date_format(ORA_DATE_FORMAT_DEFAULT);
    }
    
}
