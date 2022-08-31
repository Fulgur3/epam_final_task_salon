package com.controller.filter;

import com.constant.RequestParameter;
import com.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UserPageFilter",
        urlPatterns = {"/addMoney", "/viewOrder", "/editReview", "/addOrder"})
public class UserPageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * Checks if user's status is user
     * If it's not, sends a 403 error
     * Otherwise proceeds with filtering
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(RequestParameter.USER);
        if (user == null || User.Status.BANNED.getName().equalsIgnoreCase(user.getStatus())) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}