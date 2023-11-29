package testdata.di.positive.fullinjection;

import com.bobocode.bring.core.annotation.Component;
import lombok.Data;

@Data
@Component
public class DefaultRestClient {
    
    private final String url = "url";
    
    private final String username = "username";
    
}
