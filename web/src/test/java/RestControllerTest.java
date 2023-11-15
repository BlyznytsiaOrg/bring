import com.bobocode.bring.web.embeddedTomcat.TomcatServletWebServerFactory;
import com.bobocode.bring.web.embeddedTomcat.TomcatWebServer;
import com.bobocode.bring.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.ExampleServlet;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RestControllerTest {
    public static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    public static final String URL_PATTERN = "/";
    public static final String HELLO_PATH = "/example/hello";
    public static final String NUMBER_PATH = "/example/number";
    public static final String NOT_EXIST_PATH = "/example/not-exist";
    public static final int PORT = 9090;
    private static TomcatWebServer webServer;
    private HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(PORT);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(List.of(new ExampleServlet()));
        webServer = (TomcatWebServer)factory.getWebServer();
        Context context = factory.getContext();
        Tomcat tomcat = webServer.getTomcat();

        tomcat.addServlet(factory.getContextPath(), DISPATCHER_SERVLET_NAME, dispatcherServlet);
        context.addServletMappingDecoded(URL_PATTERN, DISPATCHER_SERVLET_NAME);
    }

    @BeforeEach
    void setUp() {
         httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("should return 'Hello'")
    void hello_ok() throws URISyntaxException, IOException, InterruptedException {
        String url = getHost() + HELLO_PATH;
        HttpRequest request = getHttpRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals("Hello", actualResponse);
    }

    @Test
    @DisplayName("should return '200'")
    void number_ok() throws IOException, InterruptedException, URISyntaxException {
        String url = getHost() + NUMBER_PATH;
        HttpRequest request = getHttpRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals("200", actualResponse);
    }

    @Test
    @DisplayName("should return 'This application has no explicit mapping for 'path'")
    void notExistingPath_notOk() throws URISyntaxException, IOException, InterruptedException {
        String url = getHost() + NOT_EXIST_PATH;
        HttpRequest request = getHttpRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals(String.format("This application has no explicit mapping for '%s'",
                NOT_EXIST_PATH), actualResponse);
    }

    @AfterAll
    static void afterAll() {
        webServer.stop();
    }

    private HttpRequest getHttpRequest(String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url))
                .build();
    }

    private String getHost() {
        return String.format("http://localhost:%s", PORT);
    }
}
