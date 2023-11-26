package testdata.di.positive.configuration.client;

import lombok.*;

import java.util.UUID;

@Builder(toBuilder = true)
@Data
public class RestClient {
    
    private String url;
    
    private String key;
    
    public UUID getUuid() {
        return UUID.randomUUID();
    }
    
}
