package com.bobocode.bring.web.server.properties;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Value;
import lombok.Data;

@Data
@Component
public class ServerProperties {

    @Value("server.port")
    private int port;

    @Value("server.contextPath")
    private String contextPath;

    @Value("server.withStackTrace")
    private boolean withStackTrace;

    @Value("server.staticFolder")
    private String staticFolder;

    @Value("server.regexStaticUrl")
    private String regexStaticUrl;

    @Value("server.urlDelimiter")
    private String urlDelimiter;
}
