package com.bobocode.bring.testdata.di.positive.prototype;

import com.bobocode.bring.core.anotation.Scope;
import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.utils.BeanScopeUtils;
import lombok.Getter;

import java.util.UUID;

@Getter
@Service
@Scope(BeanScopeUtils.PROTOTYPE)
public class Barista {
    
    private final UUID uuid = UUID.randomUUID();
    
}
