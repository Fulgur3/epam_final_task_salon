package com.command.user;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.User;
import com.service.UserService;
import com.util.MailComposer;
import com.util.MailSenderThread;
import com.util.PasswordGenerator;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ResetPasswordCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * Retrieves email from request parameters, looks for a user with this email in the database
     * If he doesn't exists, shows error message
     * If he does, changes this user's password to a new generated one and sends an email with it
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String email = requestContent.getParameter(RequestParameter.EMAIL);
            Optional<User> found = service.findUserByEmail(email);
            if (found.isPresent()) {
                String newPassword = PasswordGenerator.generatePassword();
                service.updateUser(found.get().getId(), newPassword,
                        found.get().getUserName());
                found.get().setPassword(newPassword);
                requestContent.setSessionAttribute(RequestParameter.OPERATION_SUCCESS,
                        true);
                router.setRedirect(true);
                new MailSenderThread(email, MailComposer.getResetPasswordMessageTheme(),
                        MailComposer.getResetPasswordMessage(newPassword)).start();
            } else {
                requestContent.setAttribute(RequestParameter.NO_EMAIL_FOUND, true);
            }
            router.setPage(PageAddress.RESET_PASSWORD_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setRedirect(false);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
