package com.itranswarp.summer.context;

import java.util.List;

import jakarta.annotation.Nullable;
//framework级别的，有关bean的生命周期
/**
 * Used for BeanPostProcessor.
 */
public interface ConfigurableApplicationContext extends ApplicationContext {
//这个方法用于根据 Bean 类型（type）来查找所有匹配的 Bean 定义（BeanDefinition）
    List<BeanDefinition> findBeanDefinitions(Class<?> type);
//这个方法根据 Bean 类型来查找 单个 Bean 定义。如果找到与指定类型匹配的 Bean，返回相应的 BeanDefinition；如果找不到，返回 null。
    @Nullable
    BeanDefinition findBeanDefinition(Class<?> type);
//这个方法根据 Bean 名称查找相应的 BeanDefinition。返回与给定名称匹配的 BeanDefinition，如果找不到相应的 Bean，返回 null。
    @Nullable
    BeanDefinition findBeanDefinition(String name);
//这个方法根据 Bean 名称和所需的 Bean 类型来查找对应的 BeanDefinition。如果 Bean 名称匹配且类型正确，则返回相应的 BeanDefinition，否则返回 null。
    @Nullable
    BeanDefinition findBeanDefinition(String name, Class<?> requiredType);
//处理强依赖；这个方法用于创建一个 早期单例（early singleton）。它根据提供的 BeanDefinition 定义，提前实例化 Bean 实例，并返回该实例
    Object createBeanAsEarlySingleton(BeanDefinition def);
}
