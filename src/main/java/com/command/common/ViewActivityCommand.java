package com.command.common;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.Activity;
import com.entity.Review;
import com.service.ActivityService;
import com.service.ReviewService;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import com.validation.NumberValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ViewActivityCommand implements Command {
    private static Logger logger = LogManager.getLogger();
    private static ActivityService service = ServiceFactory.getInstance().getActivityService();

    /**
     * Retrieves activity's ID from request parameters, looks for activity with this ID
     * in the database, looks for reviews for that activity, sets them as request attributes and
     * forwards to the page with activity information
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        try {
            String activityId = requestContent.getParameter(RequestParameter.ACTIVITY_ID);
            if (NumberValidator.getInstance().validateNumber(activityId)) {
                Optional<Activity> found = service.findActivityById(Integer.parseInt(activityId));
                ReviewService reviewService = ServiceFactory.getInstance().getReviewService();
                List<Review> reviewList = reviewService.findReviewByActivityId(Integer.parseInt(activityId));
                if (found.isPresent()) {
                    requestContent.setAttribute(RequestParameter.ACTIVITY, found.get());
                    requestContent.setAttribute(RequestParameter.REVIEW_LIST, reviewList);
                    router.setPage(PageAddress.VIEW_ACTIVITY_PAGE);
                } else {
                    logger.log(Level.ERROR, "Couldn't find activity");
                    router.setPage(PageAddress.NOT_FOUND_ERROR_PAGE);
                }
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
