package testdata.di.positive.fullinjection;

import com.bobocode.bring.core.annotation.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class GetInfoFromExternalServicesUseCase {

    private final ExternalService externalService1;

    private final ExternalService externalService2;

    public List<String> getInfoFromService() {
        return Stream.concat(externalService1.getInfo().stream(), externalService2.getInfo().stream())
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public String toString() {
        return "GetInfoFromExternalService2UseCase{" +
                "externalService=" + externalService1 +
                ", externalService2=" + externalService2 +
                '}';
    }
    
}
