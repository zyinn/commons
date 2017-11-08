package com.sumscope.optimus.commons.facade;

import com.sumscope.optimus.commons.exceptions.BusinessRuntimeException;
import com.sumscope.optimus.commons.exceptions.ExceptionConverter;
import com.sumscope.optimus.commons.exceptions.ExceptionDto;
import com.sumscope.optimus.commons.exceptions.ExceptionType;
import com.sumscope.optimus.commons.log.LogManager;
import com.sumscope.optimus.commons.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 各个 Facade 实现的抽象父类
 * 提供一些处理 HTTP 请求的公用方法
 * <p>
 * Created by qikai.yu on 2016/4/21.
 */
abstract public class AbstractExceptionCatchedFacadeImpl {

    public static final String NEW_LINE_STRING = "\r\n";

    /**
     * 执行服务，获取服务的返回值进行包装。如果有错则返回前端错误信息
     * @param response HttpServletResponse
     * @param process ExceptionCatchableProcessWithResult 接口
     */
    final protected void performWithExceptionCatch(HttpServletResponse response, ExceptionCatchableProcessWithResult process) {
        try {
            processServiceResult(response, process.performBusinessLogicWithReturn());
        } catch (Exception e) {
            processException(response, e);
        }
    }

    /**
     * 执行服务，并由服务自身进行返回值的包装。如果有错误返回前端错误信息
     * @param response HttpServletResponse
     * @param process ExceptionCatchableProcess 接口
     */
    final protected void performWithExceptionCatch(HttpServletResponse response, ExceptionCatchableProcess process) {
        try {
            processResponse(response, process.performBusinessLogicAndWrap());
        } catch (Exception e) {
            processException(response, e);
        }
    }

    /**
     * 将对象转换为 string, 并且写入 response
     */
    private void processResponse(HttpServletResponse response, Map map) {
        String str = JsonUtil.writeValueAsString(map);
        processResponse(response, str);
    }

    /**
     * 将 string 字符串写入 response 中
     */
    private void processResponse(HttpServletResponse response, String resultString) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            Writer out = response.getWriter();
            out.append(resultString);
            out.flush();
        } catch (IOException e) {
            LogManager.error("写入response出现错误。"+e.getLocalizedMessage());
        }

    }

    /**
     * 处理 service 返回的对象, 将结果写入 response
     */
    private void processServiceResult(HttpServletResponse response, Object obj) {
        processResponse(response, processServiceResult(obj));
    }

    /**
     * 处理 调用 service 出现的异常, 将结果返回 response
     */
    private void processException(HttpServletResponse response, Exception e) {
        LogManager.error("Facade 捕捉到程序异常！ "+ e);
        ExceptionDto exceptionDto = ExceptionConverter.convertException(e);
        processResponse(response, processServiceException(exceptionDto));
    }

    /**
     * 处理 service 返回的对象, 将结果返回
     */
    private Map processServiceResult(Object obj) {
        Map<String, Object> map = new HashMap<>();
        List<Object> list;
        if (obj instanceof List) {
            list = (List) obj;
        } else {
            list = new ArrayList<>();
            list.add(obj);
        }
        map.put("return_code", 0);
        map.put("return_message", "Success");
        map.put("result_count", list.size());
        map.put("result", list);
        return map;
    }

    private Map processServiceException(ExceptionDto exceptionDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("return_code", -1);
        map.put("return_message", exceptionDto);
        map.put("result_count", 0);

        return map;
    }



    protected interface ExceptionCatchableProcessWithResult {
        Object performBusinessLogicWithReturn();
    }

    protected interface ExceptionCatchableProcess {
        Map performBusinessLogicAndWrap();
    }


}
