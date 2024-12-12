package ru.tele2.govorova.otus.java.pro.web_app_servlets.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "MultiplyServlet", urlPatterns = "/calculator/multiply")
public class MultiplyServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(MultiplyServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter output = response.getWriter();
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        int result = a * b;
        logger.info("Multiplication request: {} * {} = {}", a, b, result);
        output.printf("<html><body><h1>%s</h1></body></html>", result);
        output.close();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
