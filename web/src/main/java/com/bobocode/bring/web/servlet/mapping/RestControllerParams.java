package com.bobocode.bring.web.servlet.mapping;

import com.bobocode.bring.web.servlet.annotation.RequestMethod;
import java.lang.reflect.Method;

public record RestControllerParams(Object instance, Method method, RequestMethod requestMethod, String path) {
}
