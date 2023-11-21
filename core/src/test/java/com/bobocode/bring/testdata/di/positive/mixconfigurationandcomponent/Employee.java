package com.bobocode.bring.testdata.di.positive.mixconfigurationandcomponent;

import com.bobocode.bring.core.anotation.Component;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
@Getter
@AllArgsConstructor
public class Employee {
    private String name;
}
