package com.controller.filter;

import com.command.CommandFactory;
import com.command.CommandAccessLevel;
import com.constant.RequestParameter;
import com.entity.User;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebFilter(filterName = "CommandAccessCheckFilter",
        urlPatterns = {"/app"})
public class CommandAccessCheckFilter implements Filter {
    private static Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) {

    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String command = servletRequest.getParameter(RequestParameter.COMMAND);
        if (command == null) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(RequestParameter.USER);
        Optional<CommandAccessLevel> commandRightOptional = CommandFactory.getInstance()
                .getCommandRight(command);
        if (commandRightOptional.isPresent()) {
            CommandAccessLevel commandAccessLevel = commandRightOptional.get();
            if (accessGranted(commandAccessLevel, user)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                logger.log(Level.ERROR, "Tried to call command " + command + " without access");
            }
        } else {
            logger.log(Level.ERROR, "No such command: " + command);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    private boolean accessGranted(CommandAccessLevel commandAccessLevel, User user) {
        if (user == null  || user.getUserStatus() == User.Status.BANNED) {
            return commandAccessLevel == CommandAccessLevel.GUEST;
        } else {
            User.Status userStatus = user.getUserStatus();
            return userStatus == User.Status.USER && (commandAccessLevel == CommandAccessLevel.USER
            || commandAccessLevel == CommandAccessLevel.GUEST) ||
            userStatus == User.Status.ADMIN;
        }
    }

    @Override
    public void destroy() {

    }
}