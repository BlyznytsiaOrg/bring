package com.bobocode.bring.web.configuration;

import com.bobocode.bring.web.embeddedTomcat.TomcatServletWebServerFactory;
import com.bobocode.bring.web.servlet.DispatcherServlet;

// @Configuration
public class WebServerConfiguration {

    //    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory(9090, "/app");
    }

    //    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
