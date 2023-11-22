package com.bobocode.bring.testdata.di.positive.prototype.off;

import java.util.UUID;

import com.bobocode.bring.core.anotation.Scope;
import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.domain.BeanScope;
import com.bobocode.bring.core.domain.ProxyMode;
import lombok.Getter;

@Getter
@Service
@Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.OFF)
public class Barista {
    
    private final UUID uuid = UUID.randomUUID();
    
}
