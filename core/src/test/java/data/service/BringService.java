package data.service;

import data.client.RestClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BringService {
    
    private final RestClient bringRestClient;
    
}
