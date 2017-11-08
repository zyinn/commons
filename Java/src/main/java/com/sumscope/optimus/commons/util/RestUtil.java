package com.sumscope.optimus.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Created by simon.mao on 2016/2/17.
 */
@Component
public class RestUtil {
    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);

    public static <T> T getForObject(String url, Class<T> responseType, Map<String, Object> params){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null){
            for (Map.Entry<String, Object> entry : params.entrySet()){
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        try {
            ResponseEntity<T> responseEntity = SimpleRestClient.getRestTemplate().exchange(builder.build().toUriString(), HttpMethod.POST, null, responseType);
            return responseEntity.getBody();
        }catch (RestClientException e){
            logger.error(e.getMessage());
            return (T)("{\"errorMessage\":" + "\"" + e.getMessage() + "\"}");
        }catch (Exception e){
            logger.error(e.getMessage());
            return (T)("{\"errorMessage\":" + "\"" + e.getMessage() + "\"}");
        }
    }

    public static <T> T getForObject2(String url, Class<T> responseType, String param){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        try {
            ResponseEntity<T> responseEntity = SimpleRestClient.getRestTemplate().exchange(param, HttpMethod.POST, null, responseType);
            return responseEntity.getBody();
        }catch (RestClientException e){
            logger.error(e.getMessage());
            return null;
        }
    }

}
