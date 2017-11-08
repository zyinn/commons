package com.sumscope.optimus.commons.cachemanagement.sample;

import com.sumscope.optimus.commons.cachemanagement.annotation.CacheMe;
import com.sumscope.optimus.commons.cachemanagement.annotation.NotifyMyParameter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan.bai on 2016/4/5.
 * 缓存使用的例子，注意为使用注释，该类不能被new 操作实例化，必须使用Autowired或者通过applicationContext.getBean()方法
 * 从Spring框架中获取实例。否则注释解释器将无法获得对切面的解析
 */
@Service
public class SampleService {

    @CacheMe(timeout = 2,synchornizeUpdate = false)
    public List<String> testAsynUpdate(int arg1) {
        System.out.println("not hit! long time method invocation!");
        try {
            Thread.currentThread().sleep(arg1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> value = new ArrayList<>();
        value.add("Useless Cache Test!");
        value.add(String.valueOf(arg1));
        return value;
    }

    @CacheMe(timeout = 10, notifyResolver = BusinessResolverSample.class)
    public List<String> testResolverMethod(String arg1, int arg2) {
        System.out.println("not hit! long time method invocation!");
        try {
            Thread.currentThread().sleep(arg2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> value = new ArrayList<>();
        value.add(arg1);
        value.add(String.valueOf(arg2));
        return value;
    }

    @CacheMe(timeout = 2)
    public List<String> testUselessCacheMethod(int arg1) {
        System.out.println("not hit! long time method invocation!");
        try {
            Thread.currentThread().sleep(arg1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> value = new ArrayList<>();
        value.add("Useless Cache Test!");
        value.add(String.valueOf(arg1));
        return value;
    }

    @CacheMe(timeout = 10)
    public List<String> testGetStringMethod(String arg1, int arg2) {
        System.out.println("not hit! long time method invocation!");
        try {
            Thread.currentThread().sleep(arg2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> value = new ArrayList<>();
        value.add(arg1);
        value.add(String.valueOf(arg2));
        return value;
    }

    @NotifyMyParameter(parameter = "obj")
    public void testNotifyParame(String obj){
        notifyMyParame(obj);

    }

    private void notifyMyParame(String obj) {

    }

    @CacheMe
    public void testVoid() {
        System.out.println("long time void method invocation!");
    }

}
