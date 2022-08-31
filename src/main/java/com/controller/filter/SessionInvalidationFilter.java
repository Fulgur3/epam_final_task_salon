package com.controller.filter;

import com.command.CommandType;
import com.constant.RequestParameter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "SessionInvalidatorFilter",
        urlPatterns = {"/*"})
public class SessionInvalidationFilter implements Filter {
    private static Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * Invalidates session if logout command is called
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String command = servletRequest.getParameter(RequestParameter.COMMAND);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (CommandType.LOGOUT.getName().equalsIgnoreCase(command) &&
                request.getSession().getAttribute(RequestParameter.USER) != null) {
            request.getSession().removeAttribute(RequestParameter.USER);
            request.getSession().invalidate();
            logger.log(Level.INFO, "Destroyed session");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}