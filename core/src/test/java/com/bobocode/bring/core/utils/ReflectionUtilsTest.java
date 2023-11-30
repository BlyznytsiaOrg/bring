package com.bobocode.bring.core.utils;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.PostConstruct;
import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.exception.BeanPostProcessorConstructionLimitationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.bobocode.bring.core.utils.ReflectionUtils.getConstructorWithParameters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ReflectionUtilsTest {

    @SneakyThrows
    @Test
    @DisplayName("Should return true for autowired setter method with non autowired setter")
    void shouldReturnTrueForAutowiredSetterMethodWithNonAutowiredSetter() {
        //when
        Method method = MyClass.class.getMethod("setSomeValue", String.class);

        //then
        assertThat(ReflectionUtils.isAutowiredSetterMethod(method)).isTrue();
    }

    @SneakyThrows
    @Test
    @DisplayName("Should return false for isAutowiredSetterMethod with a non-autowired method")
    void shouldReturnFalseForAutowiredSetterMethodWithNonAutowiredSetter() {
        //when
        Method method = MyClass.class.getMethod("nonAutowiredMethod", String.class);
        //then
        assertThat(ReflectionUtils.isAutowiredSetterMethod(method)).isFalse();
    }

    @Test
    @DisplayName("Should get constructor with one parameter")
    void shouldGetConstructorWithOneParameter() {
        //given
        Class<?> clazz = MyClass.class;
        Class<?> parameterType = String.class;
        Object instance = "test";

        //when
        Object result = ReflectionUtils.getConstructorWithOneParameter(clazz, parameterType, instance);

        //then
        assertThat(result).isNotNull()
                .isInstanceOf(MyClass.class);
    }

    @Test
    @DisplayName("Should get parameter names with method")
    void shouldGetParameterNamesWithMethod() throws NoSuchMethodException {
        //given
        Method method = MyClass.class.getMethod("setSomeValue", String.class);

        //when
        List<String> parameterNames = ReflectionUtils.getParameterNames(method);

        //then
        assertThat(List.of("value")).isEqualTo(parameterNames);
    }

    @Test
    @DisplayName("Should extract parameter position with a parameter")
    void shouldExtractParameterPositionWithParameter() {
        //when
        Parameter parameter = Arrays.stream(MyClass.class.getMethods())
                .flatMap(method -> Arrays.stream(method.getParameters()))
                .findFirst()
                .orElse(null);

        //then
        assertThat(parameter).isNotNull();
        int position = ReflectionUtils.extractParameterPosition(parameter);
        assertThat(position).isZero();
    }

    @Test
    @DisplayName("Should create new instance with proxy option true")
    void shouldCreateNewInstanceWithProxyOptionTrue() {
        //given
        Constructor<?> constructor = mock(Constructor.class);
        Class<?> clazz = MyClass.class;
        Object[] args = new Object[]{};
        boolean proxy = true;

        //when
        Supplier<Object> supplier = ReflectionUtils.createNewInstance(constructor, args, clazz, proxy);

        //then
        assertThat(supplier).isNotNull();
    }

    @Test
    @DisplayName("Should create new instance with proxy option false")
    void shouldCreateNewInstanceWithProxyOptionFalse() {
        //given
        Constructor<?> constructor = mock(Constructor.class);
        Class<?> clazz = MyClass.class;
        Object[] args = new Object[]{};
        boolean proxy = false;

        //when
        Supplier<Object> supplier = ReflectionUtils.createNewInstance(constructor, args, clazz, proxy);

        //then
        assertThat(supplier).isNotNull();
    }

    @SneakyThrows
    @Test
    @DisplayName("Should invoke method with annotated method")
    void shouldProcessBeanPostProcessorAnnotationWithAnnotatedMethodAndInvokeMethod() {
        //given
        var bean = new Bean();
        Method[] declaredMethods = bean.getClass().getDeclaredMethods();

        //when
        ReflectionUtils.processBeanPostProcessorAnnotation(bean, declaredMethods, PostConstruct.class);

        //then
        assertThat(bean.getI()).isEqualTo(1);
    }

    @SneakyThrows
    @Test
    @DisplayName("Should get exception whe clazz not have correct number of parameters in constructor")
    void shouldGetExceptionWhenClazzNotHaveCorrectNumberOfParametersInConstructor() {
        //given
        var expectedMessage = "BeanProcessor 'Bean' should have constructor with parameters 'Reflections, AnnotationBringBeanRegistry, ClassPathScannerFactory'.";
        Class<?> clazz = Bean.class;
        Map<Class<?>, Object> parameterTypesToInstance = new LinkedHashMap<>();
        parameterTypesToInstance.put(Reflections.class, null);
        parameterTypesToInstance.put(AnnotationBringBeanRegistry.class, null);
        parameterTypesToInstance.put(ClassPathScannerFactory.class, null);

        Executable executable = () -> {
            //when
            getConstructorWithParameters(clazz, parameterTypesToInstance);
        };

        //then
        var noSuchBeanException = assertThrows(BeanPostProcessorConstructionLimitationException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);

    }


    static class MyClass {
        public MyClass(String arg) {
            // Constructor logic
        }
        @Autowired
        public void setSomeValue(String value) {}

        public void nonAutowiredMethod(String value) {}
    }

    static class Bean {
        private int i = 0;
        @PostConstruct
        public void annotatedMethod() {
            i = 1;
        }

        public void nonAnnotatedMethod() {
            i = 2;
        }

        public int getI() {
            return i;
        }
    }
}
