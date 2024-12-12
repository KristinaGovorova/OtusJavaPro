package ru.tele2.govorova.java.pro.webapp;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class LoggerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String remoteAddr = request.getRemoteAddr();
        String requestURI = request.getServletContext().getContextPath();
        logger.info("Incoming request from IP: {} to URI: {}", remoteAddr, requestURI);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
