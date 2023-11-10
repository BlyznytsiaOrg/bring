package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class DispatcherServlet extends HttpServlet {
    public static final String BRING_CONTEXT = "BRING_CONTEXT";

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("I am in init() method  of DispatcherServlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ThreadLocal<String> id = new ThreadLocal<>();
        id.set(UUID.randomUUID().toString());
        req.setAttribute("id", id);
        System.out.println("I am in doGet of DispatcherServlet");
        var bringContext = (BringApplicationContext) req.getServletContext().getAttribute(BRING_CONTEXT);


        Map<String, Object> allBeans = bringContext.getAllBeans();
        StringBuilder mapAsString = new StringBuilder();
        allBeans.keySet().stream().map(key -> key + "=" + allBeans.get(key) + ", ").forEach(mapAsString::append);

        resp.addHeader("Trace-Id", id.get());

        var writer = resp.getWriter();
        writer.println(mapAsString);
        writer.flush();
    }
}
