package testdata.di.positive.fullinjection;

import io.github.blyznytsiaorg.bring.core.annotation.Service;
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
