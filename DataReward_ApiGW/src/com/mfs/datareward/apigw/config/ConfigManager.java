/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfs.datareward.apigw.config;

import com.elcom.util.miscellaneous.cfg.configloader.ConfigLoader;
import com.elcom.util.miscellaneous.cfg.configloader.ConfigObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author TrisTDT
 */
public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    public ConfigManager() {

    }

    public static final String configFile = System.getProperty("user.dir") + File.separator + "etc" + File.separator + "dataRewardApiGW.cfg";

    public static int param_nTimeOut;
    public static String DB_URL;
    public static String DB_User;
    public static String DB_Pass;
    public static String DBClassDriverName;

    public ConfigLoader configLoader = null;

    public String getDefaultContent() {
        String content = "";
        content += ""
                + "#Config File of module RequestRouter" + "\n"
                + "CONFIG: _" + "\n"
                + "Timeout = 10 _" + "\n"
                + "\n"
                + "LOG: _" + "\n"
                + "level = 1 _" + "\n"
                + "host = \"127.0.0.1\" _" + "\n"
                + "\n"
                + "DATABASE: _" + "\n"
                + "DBURL = \"jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=root)))\" _" + "\n"
                + "DBUser = \"autosms\" _" + "\n"
                + "DBPass = \"autosms\" _" + "\n"
                + "DBClassDriverName = \"oracle.jdbc.driver.OracleDriver\" _" + "\n"
                + "\n";

        return content;
    }

    public void loadConfigFromFile() {
        LOGGER.info("Need load config from: " + configFile);
        try {
            File file = new File(configFile);
            if (!file.exists()) {
                GenerateDefaultFile(getDefaultContent());
            }
            this.configLoader = new ConfigLoader();
            this.configLoader.loadFile(configFile);

            ConfigObject o_db = this.configLoader.findObject("DATABASE")[0];
            DB_URL = getStringValue(o_db, "DBURL");
            DB_User = getStringValue(o_db, "DBUser");
            DB_Pass = getStringValue(o_db, "DBPass");
            DBClassDriverName = getStringValue(o_db, "DBClassDriverName");
        } catch (Exception e) {
//            LOGGER.error("", e);
            LOGGER.error("Can not load Config");
            String message = e.toString();
            StackTraceElement[] stack = e.getStackTrace();
            for (StackTraceElement stack1 : stack) {
                message += "\n" + stack1.toString();
            }
            LOGGER.error("Exception catched!\n" + message);
        }
    }

    private String getStringValue(ConfigObject configObject, String key) throws Exception {
        try {
            String s = configObject.findParameter(key)[0].getValue();
            LOGGER.info("----- " + key + " = " + s);
            return s;
        } catch (Exception e) {
            LOGGER.error("Can not get: " + key + " from: " + configObject.getName());
            throw new Exception();
        }
    }

    private int getIntValue(ConfigObject configObject, String key) throws Exception {
        try {
            String s = getStringValue(configObject, key);
            return Integer.parseInt(s);
        } catch (Exception e) {
            LOGGER.error("Can not get: " + key + " from: " + configObject.getName());
            throw new Exception();
        }
    }

    private double getDoubleValue(ConfigObject configObject, String key) throws Exception {
        try {
            String s = getStringValue(configObject, key);
            return Double.parseDouble(s);
        } catch (Exception e) {
            LOGGER.error("Can not get: " + key + " from: " + configObject.getName());
            throw new Exception();
        }
    }

    private long getLongValue(ConfigObject configObject, String key) throws Exception {
        try {
            String s = getStringValue(configObject, key);
            return Long.parseLong(s);
        } catch (Exception e) {
            LOGGER.error("Can not get: " + key + " from: " + configObject.getName());
            throw new Exception();
        }
    }

    private void GenerateDefaultFile(String defaultContent) {
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(ConfigManager.configFile);

        } catch (FileNotFoundException e) {
        } finally {
            if (null != f) {
                try {
                    f.write(defaultContent.getBytes());
                } catch (IOException e) {
                } finally {
                    try {
                        f.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}
