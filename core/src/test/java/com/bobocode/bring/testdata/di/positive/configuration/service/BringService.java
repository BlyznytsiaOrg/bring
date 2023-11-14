package com.bobocode.bring.testdata.di.positive.configuration.service;

import com.bobocode.bring.testdata.di.positive.configuration.client.RestClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BringService {
    
    private final RestClient bringRestClient;
    
}
