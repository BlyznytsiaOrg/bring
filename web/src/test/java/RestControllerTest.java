import com.bobocode.bring.web.configuration.WebServerConfiguration;
import com.bobocode.bring.web.dto.ErrorResponse;
import com.bobocode.bring.web.embeddedTomcat.TomcatServletWebServerFactory;
import com.bobocode.bring.web.embeddedTomcat.TomcatWebServer;
import com.bobocode.bring.web.http.HttpStatus;
import com.bobocode.bring.web.servlet.DispatcherServlet;
import com.bobocode.bring.web.servlet.JsonExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.controller.ExampleRestController;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RestControllerTest {
    public static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    public static final String URL_PATTERN = "/";
    public static final String HELLO_PATH = "/example/hello";
    public static final String NUMBER_PATH = "/example/number";
    public static final String NOT_EXIST_PATH = "/not-exist";
    public static final String CUSTOM_EXCEPTION_PATH = "/example/custom-exception";
    public static final String DEFAULT_EXCEPTION_PATH = "/example/default-exception";
    public static int port;
    private static TomcatWebServer webServer;
    private static ObjectMapper objectMapper;
    private HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        port = ThreadLocalRandom.current().nextInt(8000, 9000);
        WebServerConfiguration webServerConfiguration = new WebServerConfiguration();
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(port);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(List.of(new ExampleRestController()));
        webServer = (TomcatWebServer)factory.getWebServer();
        JsonExceptionHandler jsonExceptionHandler = webServerConfiguration.jsonExceptionHandler();
        objectMapper = webServerConfiguration.objectMapper();
        Context context = factory.getContext();
        Tomcat tomcat = webServer.getTomcat();

        tomcat.addServlet(factory.getContextPath(), DISPATCHER_SERVLET_NAME, dispatcherServlet);
        context.addServletMappingDecoded(URL_PATTERN, DISPATCHER_SERVLET_NAME);
        context.getPipeline().addValve(jsonExceptionHandler);
    }

    @BeforeEach
    void setUp() {
         httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("should return 'Hello'")
    void hello_ok() throws URISyntaxException, IOException, InterruptedException {
        String url = getHost() + HELLO_PATH;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals("Hello", actualResponse);
    }

    @Test
    @DisplayName("should return '200'")
    void number_ok() throws IOException, InterruptedException, URISyntaxException {
        String url = getHost() + NUMBER_PATH;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals("200", actualResponse);
    }

    @Test
    @DisplayName("should return 'This application has no explicit mapping for 'path'")
    void notExistingPath_notOk() throws URISyntaxException, IOException, InterruptedException {
        String url = getHost() + NOT_EXIST_PATH;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals(String.format("This application has no explicit mapping for '%s'",
                NOT_EXIST_PATH), actualResponse);
    }

    @Test
    @DisplayName("should return the value of path variable")
    void pathVariable_ok() throws URISyntaxException, IOException, InterruptedException {
        String pathVariable = "20";
        String url = getHost() + "/example/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals(pathVariable, actualResponse);
    }

    @Test
    @DisplayName("should return custom message error")
    @SneakyThrows
    void shouldThrowCustomException() {
        String url = getHost() + CUSTOM_EXCEPTION_PATH;
        String defaultMessage = "Bad Request";
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        Assertions.assertEquals(errorResponse.getMessage(), defaultMessage);
        Assertions.assertEquals(errorResponse.getCode(), HttpStatus.BAD_REQUEST.getValue());
    }

    @Test
    @DisplayName("should return default message error")
    @SneakyThrows
    void shouldThrowDefaultException() {
        String url = getHost() + DEFAULT_EXCEPTION_PATH;
        String defaultMessage = "TestDefaultException";
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        Assertions.assertEquals(errorResponse.getMessage(), defaultMessage);
        Assertions.assertEquals(errorResponse.getCode(), HttpStatus.INTERNAL_SERVER_ERROR.getValue());
    }

    @Test
    @DisplayName("should return boolean 'true'")
    void shouldReturnBooleanTrue() throws URISyntaxException, IOException, InterruptedException {
        String pathVariable = "true";
        String url = getHost() + "/example/variable1/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Assertions.assertEquals(pathVariable, actualResponse);
    }

    @Test
    @DisplayName("should throw exception with message")
    void shouldThrowException() throws URISyntaxException, IOException, InterruptedException {
        String pathVariable = "non_boolean";
        String expectedMessage = String.format("Failed to convert value of type 'java.lang.String' "
                + "to required type 'boolean'; Invalid boolean value [%s]", pathVariable);
        String url = getHost() + "/example/variable1/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        Assertions.assertEquals(expectedMessage, errorResponse.getMessage());
    }

    @AfterAll
    static void afterAll() {
        webServer.stop();
    }

    private HttpRequest getHttpGetRequest(String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url))
                .build();
    }

    private String getHost() {
        return String.format("http://localhost:%s", port);
    }
}
