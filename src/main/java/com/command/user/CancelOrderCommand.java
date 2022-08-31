package com.command.user;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.Order;
import com.entity.User;
import com.service.OrderService;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CancelOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();

    /**
     * Retrieves user and cancelled order's ID from request and session parameters and
     * cancels order
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
            Optional<Order> found = service.findOrderById(Integer.parseInt(id));
            if (found.isPresent()) {
                if (user.getId() == found.get().getUserId()) {
                    service.cancelOrder(Integer.parseInt(id));
                    router.setRedirect(true);
                    router.setPage(PageAddress.VIEW_USER_ORDERS);
                } else {
                    router.setPage(PageAddress.FORBIDDEN_ERROR_PAGE);
                    logger.log(Level.ERROR, "User's id and order's user's id do not match");
                }
            } else {
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                logger.log(Level.ERROR, "Couldn't find order");
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}