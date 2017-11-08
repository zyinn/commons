package com.sumscope.optimus.commons.cachemanagement.annotation;

import java.lang.annotation.*;

/**
 * Created by fan.bai on 2016/4/8.
 * 注释用于通知缓冲管理中心，有数据变动可能需要更新缓存。
 * 使用注释的先决条件是预先注册相应的基于AOP的处理对象。建议使用Spring框架，然后在ApplicationContext.xml中加入：
 *       <bean id="notifyMyParamAop"
 *          class="com.sumscope.optimus.commons.cachemanagement.NotifyMyParameterAnnAOPProcess">
 *       </bean>
 *  由于基于AOP解析，该注释不能用在本地函数上直接调用。必须用在某被Spring管理的类的方法上。请参考Sample中的使用方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotifyMyParameter {
    /**
     * @return 对应的参数名，若未设置或无法在方法中找到参数名，则不处理
     */
    String parameter() default "";

}
