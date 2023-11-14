package com.bobocode.bring.testdata.di.positive.fullinjection;

import com.bobocode.bring.core.anotation.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExternalService2 implements ExternalService {
    
    private final RestClient restClient2;

    public List<String> getInfo() {
        List<String> infoList = new ArrayList<>();
        infoList.add("Some info2");
        infoList.add(null);
        infoList.add("Other info2");

        return infoList;
    }
    
    @Override
    public String toString() {
        return "ExternalService2{" +
                "restClient2=" + restClient2 +
                '}';
    }
    
}
