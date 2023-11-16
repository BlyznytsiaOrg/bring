package com.bobocode.bring.core.context.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Slf4j
public class TypeResolverFactory {

    private final List<FieldValueTypeInjector> fieldValueTypeInjectors;
    private final List<ParameterValueTypeInjector> parameterValueTypeInjectors;

    private final Map<String, String> properties;

    public TypeResolverFactory(Map<String, String> properties) {
        this.properties = properties;
        this.fieldValueTypeInjectors = List.of(
                new PropertyFieldValueTypeInjector(properties)
        );
        this.parameterValueTypeInjectors = List.of(
                new PropertyParameterValueTypeInjector(properties)
        );
    }
}