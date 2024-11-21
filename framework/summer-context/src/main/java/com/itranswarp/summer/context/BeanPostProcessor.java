package com.itranswarp.summer.context;

public interface BeanPostProcessor {

    /**
     * Invoked after new Bean().bean的预处理，在test也就是廖老师网站的测试步骤中具体实现
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     在 Bean 初始化之后执行，通常用于对 Bean 进行修改、包装、创建代理等操作。
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * Invoked before bean.setXyz() called.
     */
    //保存替换前的原始Bean
    default Object postProcessOnSetProperty(Object bean, String beanName) {
        return bean;
    }
}
