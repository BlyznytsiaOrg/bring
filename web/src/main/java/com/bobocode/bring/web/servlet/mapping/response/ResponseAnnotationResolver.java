package com.bobocode.bring.web.servlet.mapping.response;

import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface ResponseAnnotationResolver {

    Class<? extends Annotation> getAnnotation();

    HttpServletResponse handleAnnotation(HttpServletResponse response, Method method);
}
