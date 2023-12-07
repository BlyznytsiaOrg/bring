package io.github.blyznytsiaorg.bring.web.servlet.mapping.response;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.ResponseStatus;
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
