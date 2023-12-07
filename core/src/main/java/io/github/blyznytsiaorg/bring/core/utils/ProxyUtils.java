package io.github.blyznytsiaorg.bring.core.utils;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Utility class providing methods to create proxies.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public class ProxyUtils {

    /**
     * Creates a proxy instance for the given class using a specified constructor and arguments.
     *
     * @param clazz       The class to create a proxy for
     * @param constructor The constructor to be used for proxy creation
     * @param args        The arguments to pass to the constructor
     * @return The proxy object for the specified class
     */
    @SneakyThrows
    public Object createProxy(Class<?> clazz, Constructor<?> constructor, Object[] args) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(clazz);
        
        MethodHandler methodHandler = new Handler(constructor, args);
        
        return proxyFactory.create(new Class<?>[0], new Object[0], methodHandler);
    }

    /**
     * Handler class implementing MethodHandler for proxy invocation.
     */
    @RequiredArgsConstructor
    public class Handler implements MethodHandler {
        
        private final Constructor<?> constructor;
        
        private final Object[] constructorArgs;

        /**
         * Invokes the method on the proxy object and delegates to the actual object.
         *
         * @param self     The proxy object
         * @param thisMethod The method being invoked on the proxy
         * @param proceed  The proceed method
         * @param args     The arguments for the method invocation
         * @return The result of the method invocation on the actual object
         * @throws Throwable If an error occurs during method invocation
         */
        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            Object obj = constructor.newInstance(constructorArgs);

            return thisMethod.invoke(obj, args);
        }
    }
    
}
