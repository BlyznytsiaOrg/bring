package testdata.di.positive.fullinjection;

import io.github.blyznytsiaorg.bring.core.annotation.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExternalService1 implements ExternalService {
    
    private final RestClient restClient1;
    
    public List<String> getInfo() {
        List<String> infoList = new ArrayList<>();
        infoList.add("Some info");
        infoList.add(null);
        infoList.add("Other info");
        
        return infoList;
    }
    
    @Override
    public String toString() {
        return "ExternalService1{" +
                "restClient=" + restClient1 +
                '}';
    }
    
}
