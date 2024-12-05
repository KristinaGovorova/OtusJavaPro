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


@WebServlet(name = "AddServlet", urlPatterns = "/calculator/add")
public class AddServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddServlet.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter output = response.getWriter();
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        int result = a + b;
        logger.info("Addition request: {} + {} = {}", a, b, result);
        output.printf("<html><body><h1>Result is %s</h1></body></html>", result);
        output.close();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
