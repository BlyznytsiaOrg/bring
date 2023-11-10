package com.bobocode.bring.core.context;

import com.bobocode.bring.core.exception.BeansException;

import java.util.Map;

public interface BringBeanFactory {

    <T> T getBean(Class<T> type) throws BeansException;

    <T> T getBean(Class<T> type, String name) throws BeansException;

    <T> Map<String, T> getBeans(Class<T> type) throws BeansException;

    <T> Map<String, T> getAllBeans();
}
