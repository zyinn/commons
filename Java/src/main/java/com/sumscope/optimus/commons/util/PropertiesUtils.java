/**
 *
 */
package com.sumscope.optimus.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sijin.luo
 * 用于UM工具
 */
public class PropertiesUtils {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * 解析属性文件，放入Map当中
     *
     * @param filePath 属性文件名
     * @return Map
     * @throws IOException
     */
    public static Map<String, String> converToMap(String filePath) {
        InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(filePath);
        Properties properties = new Properties();
        Map<String, String> result = new ConcurrentHashMap<String, String>();
        try {
            properties.load(inputStream);
            if (properties != null && !properties.isEmpty() && properties.size() > 0) {
                logger.debug("Begin parse properties file: " + filePath);
                for (Object keyObject : properties.keySet()) {
                    if (null == keyObject)
                        continue;
                    String key = keyObject.toString();
                    String value = (String) properties.get(key);
                    value = value == null ? "" : value.trim();
                    logger.debug("key:" + key + ",value: " + value);
                    result.put(key, value);
                }
                logger.info("Properties:" + result);
                logger.info("parse end");
            }
        } catch (IOException e) {
            logger.error("load " + filePath + " Exception:No Such file.", e);
            throw new RuntimeException("Invalid file path!", e);
        }
        return result;
    }
}
