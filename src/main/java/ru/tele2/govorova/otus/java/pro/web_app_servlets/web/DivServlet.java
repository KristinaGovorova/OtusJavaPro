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


@WebServlet(name = "DivServlet", urlPatterns = "/calculator/div")
public class DivServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(DivServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter output = response.getWriter();
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        if (b == 0) {
            logger.warn("Division by zero attempted: {} / {}", a, b);
            output.println("<html><body><h1>Cannot divide by zero.</h1></body></html>");
            output.close();
        } else {
            int result = a / b;
            logger.info("Division request: {} / {} = {}", a, b, result);
            output.printf("<html><body><h1>Result is %s</h1></body></html>", result);
        }
        output.close();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
