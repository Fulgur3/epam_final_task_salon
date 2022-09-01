package com.command.user;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.User;
import com.service.UserService;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * Retrieves login and password from request parameters, looks for a user with the same login
     * and password in the database.
     * If user is not found, shows error message.
     * If user is found but his status is banned, shows error message.
     * If user is found and his status isn't banned, sets found user as session attribute
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String login = requestContent.getParameter(RequestParameter.LOGIN);
            String password = requestContent.getParameter(RequestParameter.PASSWORD);
            Optional<User> found = service.findUserByLoginAndPassword(login, password);
            if (found.isPresent()) {
                User user = found.get();
                if (User.Status.BANNED.getName().equalsIgnoreCase(user.getStatus())) {
                    requestContent.setSessionAttribute(RequestParameter.USER_IS_BANNED, true);
                    router.setPage(PageAddress.LOGIN_PAGE);
                } else {
                    requestContent.setSessionAttribute(RequestParameter.USER_IS_BANNED, false);
                    requestContent.setSessionAttribute(RequestParameter.USER, user);
                    router.setPage(PageAddress.HOME_PAGE);
                }
                router.setRedirect(true);
            } else {
                requestContent.setAttribute(RequestParameter.AUTH_FAIL, true);
                router.setPage(PageAddress.LOGIN_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}