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

public class AddMoneyToCardCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static UserService service = ServiceFactory.getInstance().getUserService();

    /**
     * Retrieves user, card number and money from request and session parameters,
     * checks if user's card number is correct.
     * If it is, adds money to the card, if it isn't, shows error message
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            User user = (User) requestContent.getSessionAttribute(RequestParameter.USER);
            String cardNumber = requestContent.getParameter(RequestParameter.CARD_NUMBER);
            String moneyToAdd = requestContent.getParameter(RequestParameter.MONEY);
            Optional<User> found = service.findUserByIdAndCard(user.getId(), cardNumber);
            if (found.isPresent() && found.get().getId() == user.getId()) {
                requestContent.setSessionAttribute(RequestParameter.NO_CARD_FOUND, false);
                if (!service.addMoneyToCard(cardNumber, moneyToAdd)) {
                    logger.log(Level.ERROR, "Encountered an error while adding money to card");
                }
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_USER_INFO);
            } else {
                requestContent.setAttribute(RequestParameter.NO_CARD_FOUND, true);
                router.setPage(PageAddress.ADD_MONEY_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}