package com.bobocode.bring.core.context;

public interface BeanRegistry {

    <T> void registerBean(Class<T> tClass);
}
