package com.sumscope.optimus.commons.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by simon.mao on 2016/2/17.
 * Write object to json or read json to object
 */
public class JsonUtil {
    public final static ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String writeValueAsString(Object object)  {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static <T> T readValue(String content, Class<T> valueType)  {
        try {
            return objectMapper.readValue(content, valueType);
        }catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef){
		try {
			return objectMapper.readValue(jsonStr, valueTypeRef);
		} catch (Exception e) {
			logger.error(e.getMessage());
            return null;
		}
	}
}
