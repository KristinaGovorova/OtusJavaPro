package web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "SubtractServlet", urlPatterns = "/calculator/subtract")
public class SubtractServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(SubtractServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter output = response.getWriter();
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));

        logger.info("Subtraction request: {} - {} = {}", a, b, (a - b));
        output.printf("<html><body><h1>Result is %s</h1></body></html>", (a - b));
        output.close();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
