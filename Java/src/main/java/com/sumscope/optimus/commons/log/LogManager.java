package com.sumscope.optimus.commons.log;


import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.BusinessRuntimeExceptionType;
import com.sumscope.optimus.commons.zabbix.SSZabbixSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 日志工具类, 统一进行日志输出, 日后需要自定义格式日志可以在此扩展
 * <p>
 * Created by Qikai.Yu on 2016/4/28.
 */
public class LogManager {
    private static LogManager instance = null;
    private static SSZabbixSender zabbixSender = null;
    private static String appInfo = null;
    private static final Log logger = LogFactory.getLog(LogManager.class);

    public static void initZabbix(String ip, int port, String hostName, String info) {
        if (zabbixSender == null) {
            zabbixSender = new SSZabbixSender(ip, port, hostName);
            appInfo = info;
        }
    }

    public static void warnWithZabbixNode(String zabbixNode, String content) {
        logger.warn(content);
        if (zabbixSender != null) {
            zabbixSender.send(zabbixNode, content);
        }else {
            throw new RuntimeException("zabbix使用前需初始化");
        }
    }

    public static void infoWithZabbixNode(String zabbixNode, String content) {
        logger.info(content);
        if (zabbixSender != null) {
            zabbixSender.send(zabbixNode, content);
        }else {
            throw new RuntimeException("zabbix使用前需初始化");
        }
    }

    public static void onlineNum(String onlineNume) {
        if (zabbixSender != null) {
            zabbixSender.send(appInfo + "_"+ "Info", "当前在线人数:"+onlineNume);
            zabbixSender.send(appInfo + "_"+ "OnlineNum", onlineNume);
        }else {
            throw new RuntimeException("zabbix使用前需初始化");
        }
    }

    public static void errorWithZabbixNode(String zabbixNode, String content) {
        logger.error(content);
        if (zabbixSender != null) {
            zabbixSender.send(zabbixNode, content);
        }else {
            throw new RuntimeException("zabbix使用前需初始化");
        }
    }


    public static void warn(Log logger, String content) {
        logger.warn(content);
    }

    public static void info(Log logger, String content) {
        logger.info(content);
    }

    public static void debug(Log logger, String content) {
        logger.debug(content);
    }

    public static void trace(Log logger, String content) {
        logger.trace(content);
    }

    public static void warn(String content) {
        logger.warn(content);
        if(zabbixSender != null){
            zabbixSender.send(appInfo + "_"+ "Warning", content);
        }
    }

    public static void info(String content) {
        logger.info(content);
        if(zabbixSender != null){
            zabbixSender.send(appInfo + "_"+ "Info", content);
        }
    }

    public static void debug(String content) {
        logger.debug(content);
    }

    public static void trace(String content) {
        logger.trace(content);
    }

    public static void error(Log logger, BusinessRuntimeException e) {
        logger.error(e.getMessage(), e);
    }

    public static void error(BusinessRuntimeException e) {
        error(logger, e);
    }

    public static void error(String content) {
        logger.error(content);
        if(zabbixSender != null){
            zabbixSender.send(appInfo + "_"+ "Error", content);
        }
    }

    public static void error(Log logger, String content) {
        logger.error(content);
    }
}
