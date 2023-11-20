package com.bobocode.bring.core.utils;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@UtilityClass
public class ProxyUtils {

    @SneakyThrows
    public static Object createProxy(Class<?> clazz, Constructor<?> constructor, Object[] args) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(clazz);
        
        MethodHandler methodHandler = new Handler(constructor, args);
        
        return proxyFactory.create(new Class<?>[0], new Object[0], methodHandler);
    }

    @RequiredArgsConstructor
    public static class Handler implements MethodHandler {
        
        private final Constructor<?> constructor;
        
        private final Object[] constructorArgs;

        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            Object obj = constructor.newInstance(constructorArgs);

            return thisMethod.invoke(obj, args);
        }
    }
    
}
