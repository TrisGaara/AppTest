/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfs.datareward.apigw.config;

import com.mfs.datareward.apigw.database.OraClientBase;
import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author TrisTDT
 */
public class ConfigBase {

    private static final Logger log = LoggerFactory.getLogger(ConfigBase.class);

    private OraClientBase oraClientBase;
    private String filename = "";
    
    public static String LONG_CODE = "";
    public static String USERNAME = "";
    public static String PASSWORD = "";
    public static String x_ibm_client_id = "";
    public static String URL_PLUS_DATA = "";

    public static String IP_ACCESS = "";
    public static String HOST_API = "";
    public static String PORT_API = "";
    public static String MODULE_NAME = "";

    public static int TIME_OUT;

    public static int HTTP_CONNECT_TIMEOUT = 1000;
    public static int HTTP_READ_TIMEOUT = 1000;
    Properties properties = new Properties();

    public ConfigBase(String filename, OraClientBase oraClientBase) {
        this.filename = filename;
        this.oraClientBase = oraClientBase;
    }

    public void reloadConfig() {
        loadProperties(filename);
    }

    private void loadProperties(String configFilePath) {
        try {
            FileInputStream propertiesFile = new FileInputStream(configFilePath);
            properties.load(propertiesFile);
            propertiesFile.close();

            LONG_CODE = properties.getProperty("LONGCODE");
            USERNAME = properties.getProperty("USERNAME");
            PASSWORD = properties.getProperty("PASSWORD");
            x_ibm_client_id = properties.getProperty("x_ibm_client_id");
            URL_PLUS_DATA = properties.getProperty("URL_PLUS_DATA");

            IP_ACCESS = properties.getProperty("IP_ACCESS");
            HOST_API = properties.getProperty("HOST_API");
            PORT_API = properties.getProperty("PORT_API");
            MODULE_NAME = properties.getProperty("MODULE_NAME");
            HTTP_CONNECT_TIMEOUT = Integer.parseInt(properties.getProperty("HTTP_CONNECT_TIMEOUT"));
            HTTP_READ_TIMEOUT = Integer.parseInt(properties.getProperty("HTTP_READ_TIMEOUT"));

            TIME_OUT = Integer.parseInt(properties.getProperty("TIME_OUT"));
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
