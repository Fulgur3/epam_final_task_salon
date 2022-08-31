package com.command.user;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.Order;
import com.service.OrderService;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddOrderCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();

    /**
     * Retrieves added order from session attributes, adds it to the database and removes it
     * from session attributes
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            Order order = (Order) requestContent.getSessionAttribute(RequestParameter.ORDER);
            if (service.orderExists(order.getUserId(), order.getDateTime()) ||
                    !service.addOrder(order.getUserId(), order.getDateTime(),
                            order.getActivityList())) {
                logger.log(Level.ERROR, "Couldn't add order");
            }
            requestContent.removeSessionAttribute(RequestParameter.ORDER);
            router.setRedirect(true);
            router.setPage(PageAddress.VIEW_USER_ORDERS);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
