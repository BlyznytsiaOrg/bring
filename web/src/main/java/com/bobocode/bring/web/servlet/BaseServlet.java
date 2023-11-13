package com.bobocode.bring.web.servlet;

import com.bobocode.bring.web.http.HttpHeaders;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseServlet extends HttpServlet {
    private final HttpHeaders headers;

    public BaseServlet() {
        this.headers = new HttpHeaders();
    }

    public HttpHeaders getHeaders() {
        return headers;
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
        List<String> valuesList = new ArrayList<>();
        req.getHeaders(headerName).asIterator().forEachRemaining(valuesList::add);
        headers.set(headerName, valuesList);
    }
}
