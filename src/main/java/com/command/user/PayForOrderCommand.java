package com.command.user;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.Order;
import com.entity.User;
import com.service.OrderService;
import com.service.UserService;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class PayForOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();
    private static UserService userService = ServiceFactory.getInstance().getUserService();

    /**
     * Retrieves user and order's ID from request and session parameters.
     * If user doesn't have enough money on his card to pay for the order, shows error message
     * Otherwise pays for the order
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
            String id = requestContent.getParameter(RequestParameter.ORDER_ID);
            int orderId = Integer.parseInt(id);
            Optional<Order> found = service.findOrderById(orderId);
            if (found.isPresent()) {
                if (found.get().getPrice().compareTo(
                        userService.findMoneyByCardNumber(user.getCardNumber())) <= 0) {
                    requestContent.setSessionAttribute(
                            RequestParameter.NOT_ENOUGH_MONEY, false);
                    service.payForOrder(Integer.parseInt(id));
                } else {
                    requestContent.setSessionAttribute(
                            RequestParameter.NOT_ENOUGH_MONEY, true);
                }
                router.setRedirect(true);
                router.setPage(PageAddress.VIEW_USER_ORDERS);
            } else {
                logger.log(Level.ERROR, "Couldn't find order");
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
