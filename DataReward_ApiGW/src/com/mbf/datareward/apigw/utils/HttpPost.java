/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mbf.datareward.apigw.utils;

import com.mfs.datareward.apigw.config.ConfigBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author TrisTDT
 */
public class HttpPost {

    private static final Logger logger = LoggerFactory.getLogger(HttpPost.class);

    @SuppressWarnings("deprecation")
    public static String httpPostData(String url, String xml, String soapAction) throws Exception {
        logger.info("Request: " + xml);

        HttpClient client = new HttpClient();
        HttpState state = client.getState();

        client.setState(state);
        client.setTimeout(ConfigBase.TIME_OUT);
        client.setConnectionTimeout(ConfigBase.TIME_OUT);

        PostMethod method = new PostMethod(url);
        method.setDoAuthentication(true);
        method.getHostAuthState().setAuthAttempted(true);
        method.getHostAuthState().setAuthRequested(true);
        method.getHostAuthState().setPreemptive();
        method.addRequestHeader("Content-Type", "text/xml; charset=utf-8");
        method.addRequestHeader("SOAPAction", soapAction);
        method.addRequestHeader("x-ibm-client-id", ConfigBase.x_ibm_client_id);

        try {
            method.setRequestBody(xml);

        } catch (Exception e) {
            try {
                ByteArrayRequestEntity entity = new ByteArrayRequestEntity(xml.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new Exception("Impossible to set the xml in the post");
            }

        }
        long timeRequest = System.currentTimeMillis();
        int iRes = 0;
        try {
            iRes = client.executeMethod(method);
        } catch (IOException e) {
            logger.error("", e);
        }

        long timeResponse = System.currentTimeMillis();
        logger.info("Thoi gian xu ly lenh: " + (timeResponse - timeRequest) / 1000 + "s");

        InputStream inputStream = method.getResponseBodyAsStream();
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
        } finally {

        }
        String textResponse = sb.toString();
        logger.info("Response: " + textResponse);
        if (textResponse.trim().length() == 0) {
            return null;
        }
        return textResponse;
    }

    public static String getResult(String str, String tag_Start, String tag_End) {
        try {
            String result = str.substring(str.indexOf(tag_End) + tag_Start.length(), str.indexOf(tag_End));
            return result;
        } catch (Exception e) {
            logger.error("", e);
        }
        return "";
    }

    public static String getXMLfile(String path) {
        File file = new File(path);
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null) {
                contents.append(text);
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
            return contents.toString();
        }
    }

}
