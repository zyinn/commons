package com.sumscope.optimus.commons.cachemanagement.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by simon.mao on 2016/2/16.
 * 使用缓存的例子。为运行该例子需要配置ApplicationContext.xml文件。文件中需要配置如下信息：
 <?xml version="1.0" encoding="UTF-8"?>
 <beans xmlns="http://www.springframework.org/schema/beans"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:context="http://www.springframework.org/schema/context"
 xmlns:tx="http://www.springframework.org/schema/tx"
 xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
 http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd
 http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd"
 default-autowire="byName" default-lazy-init="true">
 <context:component-scan base-package="com.sumscope.optimus.commons.cachemanagement.sample" >
 </context:component-scan>
 <bean id="cacheMeAop"
 class="com.sumscope.optimus.commons.cachemanagement.CacheMeAnnAOPProcess">
 </bean>

 <bean id="notifyMyParamAop"
 class="com.sumscope.optimus.commons.cachemanagement.NotifyMyParameterAnnAOPProcess">
 </bean>

 <bean id="mainClass"
 class="com.sumscope.optimus.commons.cachemanagement.sample.MainStart">
 </bean>

 </beans>
 */
@SpringBootApplication
public class MainStart {
    @Autowired
    private SampleService mainService;

    public static void main(String[] args) {
        SpringApplication.run(MainStart.class, args);
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        MainStart ms = (MainStart) applicationContext.getBean("mainClass");
        ms.testAsynUpdateTwoThreadsUseSameArgs();
    }
    //  测试异步更新。应显示两个线程不断获取数据，间或更新一次缓存
    public void testAsynUpdateTwoThreadsUseSameArgs() {
        Timer timer1 = new Timer();
        AsynCacheTask task1 = new AsynCacheTask("A", "String A", 3000);
        timer1.schedule(task1, 100, 200);

        Timer timer2 = new Timer();
        AsynCacheTask task2 = new AsynCacheTask("B", "String A", 3000);
        timer2.schedule(task2, 300, 300);
    }


    //  两个线程使用相同方法调用。结果应显示两个线程被同步。当方法实际调用时另一线程在等待。
    public void testTwoThreadsUseSameArgs() {
        Timer timer1 = new Timer();
        CacheTask task1 = new CacheTask("A", "String A", 2000);
        timer1.schedule(task1, 300, 6000);

        Timer timer2 = new Timer();
        CacheTask task2 = new CacheTask("B", "String A", 2000);
        timer2.schedule(task2, 300, 300);
    }

    //  两个线程使用不同参数进行方法调用。结果应该显示两个线程相互之间不被同步。即当A线程调用实际方法时B线程仍然可以从缓存中命中数据。
    public void testTwoThreadsUseDifferentArgs() {
        Timer timer1 = new Timer();
        CacheTask task1 = new CacheTask("A", "String A", 2000);
        timer1.schedule(task1, 300, 3000);

        Timer timer2 = new Timer();
        CacheTask task2 = new CacheTask("B", "String B", 200);
        timer2.schedule(task2, 300, 500);

    }
//  测试Resolver实现，应显示逐步增多的列表。每次过期后从新开始刷新
    public void testBusinessResolverSample(){
        Timer timer1 = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                List<String> strings = mainService.testResolverMethod("SampleMethod",200);
                System.out.println(" get result!" + strings);
            }
        };
        timer1.schedule(task1,200,1000);
        final IntContainer i = new IntContainer();

        Timer time2 = new Timer();
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                String s  = "Notifies: " + i.getIntvalue() + "times";
                mainService.testNotifyParame(s);
//                SSCacheManagementFactory.notifyUpdateResult("Notifies: " + i.getIntvalue() + "times");
                i.accumulate();
            }
        };
        time2.schedule(task2,100,600);

    }

    private class IntContainer {
        private int intvalue;

        public int getIntvalue() {
            return intvalue;
        }

        public void accumulate(){
            this.intvalue ++;
        }
    }

    //  测试自动删除无效缓存。结果应显示调用都不命中缓存。
    public void testUselessCache() {
        Timer timer1 = new Timer();
        UselessCacheTask task1 = new UselessCacheTask("A", 200);
        timer1.schedule(task1, 300, 12000);

    }

    private class UselessCacheTask extends TimerTask {
        String key;
        int i;

        public UselessCacheTask(String k, int i) {
            this.key = k;
            this.i = i;
        }

        @Override
        public void run() {
            List<String> strings = mainService.testUselessCacheMethod(i);
            System.out.println(key + " get result!" + strings);
        }
    }

    private class CacheTask extends AbstractCacheTask {

        public CacheTask(String k, String s, int i) {
            super(k,s,i);
        }

        @Override
        public void run() {
            List<String> strings = mainService.testGetStringMethod(s, i);
            System.out.println(key + " get result!" + strings);
        }
    }

    private class AsynCacheTask extends AbstractCacheTask {

        public AsynCacheTask(String k, String s, int i) {
            super(k,s,i);
        }

        @Override
        public void run() {
            List<String> strings = mainService.testAsynUpdate(i);
            System.out.println(key + " get result!" + strings);
        }
    }

    private abstract class AbstractCacheTask extends TimerTask {
        String key;
        String s;
        int i;

        public AbstractCacheTask(String k, String s, int i) {
            this.key = k;
            this.s = s;
            this.i = i;
        }
    }


}
