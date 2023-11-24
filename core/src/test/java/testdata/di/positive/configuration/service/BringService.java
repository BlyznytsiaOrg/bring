package testdata.di.positive.configuration.service;

import testdata.di.positive.configuration.client.RestClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BringService {
    
    private final RestClient bringRestClient;
    
}
