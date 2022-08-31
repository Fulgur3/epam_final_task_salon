package com.command.admin;

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
import com.validation.NumberValidator;

import java.util.List;

public class ViewOrdersCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static OrderService service = ServiceFactory.getInstance().getOrderService();
    private static final int RECORDS_PER_PAGE = 10;

    /**
     * Retrieves table page parameters from request, then looks for orders in the database
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String pageNumberParameter = requestContent.getParameter(RequestParameter.PAGE_NUMBER);
            if (NumberValidator.getInstance().validateNumber(pageNumberParameter)) {
                int pageNumber = Integer.parseInt(requestContent.getParameter
                        (RequestParameter.PAGE_NUMBER));
                int numberOfRecords = service.countOrders();
                int numberOfPages = (int) Math.ceil(numberOfRecords * 1.0 / RECORDS_PER_PAGE);
                if (pageNumber > numberOfPages) {
                    pageNumber = numberOfPages;
                }
                if (numberOfPages == 0){
                    pageNumber = 1;
                }
                List<Order> orderList = service.findAllOrders(
                        (pageNumber - 1) * RECORDS_PER_PAGE,
                        RECORDS_PER_PAGE);
                requestContent.setAttribute(RequestParameter.ORDER_LIST, orderList);
                requestContent.setAttribute(RequestParameter.NUMBER_OF_PAGES, numberOfPages);
                requestContent.setAttribute(RequestParameter.CURRENT_TABLE_PAGE_NUMBER, pageNumber);
                router.setPage(PageAddress.ALL_ORDERS_PAGE);
            } else {
                router.setPage(PageAddress.BAD_REQUEST_ERROR_PAGE);
            }

        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}