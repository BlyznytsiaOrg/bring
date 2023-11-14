package com.bobocode.bring.testdata.di.positive.fullinjection;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestClient {
    
    private String url;
    
    private String username;

    @Override
    public String toString() {
        return "RestClient{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
    
}
