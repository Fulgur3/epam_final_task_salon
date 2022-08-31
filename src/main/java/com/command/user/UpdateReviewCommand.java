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

public class UpdateReviewCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ReviewService service = ServiceFactory.getInstance().getReviewService();

    /**
     * Retrieves review's ID and new properties from request parameters, then updates the review
     * in the database and redirects to the page of activity that the review was for
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String id = requestContent.getParameter(RequestParameter.REVIEW_ID);
            String newMark = requestContent.getParameter(RequestParameter.REVIEW_MARK);
            String newMessage = requestContent.getParameter(RequestParameter.REVIEW_MESSAGE).trim();
            Optional<Review> found = service.findReviewById(Integer.parseInt(id));
            if (found.isPresent()) {
                if (service.updateReview(Integer.parseInt(id), Integer.parseInt(newMark),
                        newMessage)) {
                    requestContent.setSessionAttribute(RequestParameter.REVIEW, null);
                    router.setRedirect(true);
                    router.setPage(PageAddress.VIEW_ACTIVITY + found.get().getActivityId());
                } else {
                    logger.log(Level.ERROR, "Couldn't update review");
                    router.setPage(PageAddress.BAD_REQUEST_ERROR_PAGE);
                }
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