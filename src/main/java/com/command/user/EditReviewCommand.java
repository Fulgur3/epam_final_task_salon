package com.command.user;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.Review;
import com.service.ReviewService;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class EditReviewCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ReviewService service = ServiceFactory.getInstance().getReviewService();

    /**
     * Retrieves edited review's ID, looks for it in the database, sets it as session attribute and
     * redirects to the edit review page
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String reviewId = requestContent.getParameter(RequestParameter.REVIEW_ID);
            Optional<Review> found = service.findReviewById(Integer.parseInt(reviewId));
            if (found.isPresent()) {
                requestContent.setSessionAttribute(RequestParameter.REVIEW, found.get());
                router.setRedirect(true);
                router.setPage(PageAddress.EDIT_REVIEW_PAGE);
            } else {
                logger.log(Level.ERROR, "Couldn't find review");
                router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
