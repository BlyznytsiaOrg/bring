package com.bobocode.bring.web.servlet;

import com.bobocode.bring.web.http.HttpHeaders;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseServlet extends HttpServlet implements BringServlet {
    private final HttpHeaders requestHeaders;

    public BaseServlet() {
        this.requestHeaders = new HttpHeaders();
    }

    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getRequestHeaders(req);
    }

    private void getRequestHeaders(HttpServletRequest req) {
        req.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> processHeader(req, headerName));
    }

    private void processHeader(HttpServletRequest req, String headerName) {
            requestHeaders.getHeadersNameList()
                    .stream()
                    .filter(name -> name.equalsIgnoreCase(headerName))
                    .findFirst()
                    .ifPresent(name -> requestHeaders.set(name, req.getHeader(name)));
    }
}
