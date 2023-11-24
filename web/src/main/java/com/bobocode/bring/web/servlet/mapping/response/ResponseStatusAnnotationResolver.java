package com.bobocode.bring.web.servlet.mapping.response;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ResponseStatusAnnotationResolver implements ResponseAnnotationResolver {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return ResponseStatus.class;
    }

    @Override
    public HttpServletResponse handleAnnotation(HttpServletResponse resp, Method method) {
        ResponseStatus annotation = method.getAnnotation(ResponseStatus.class);
        int statusValue = annotation.value().getValue();
        resp.setStatus(statusValue);
        return resp;
    }
}
