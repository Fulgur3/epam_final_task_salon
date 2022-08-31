package com.command.common;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.entity.Activity;
import com.service.ActivityService;
import com.service.ServiceException;
import com.service.ServiceFactory;
import com.controller.SessionRequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ViewActivitiesCommand implements Command{
    private static Logger logger= LogManager.getLogger();


    @Override
    public PageRouter execute(SessionRequestContent requestContent){
        PageRouter router = new PageRouter();
        try {
            ActivityService service = ServiceFactory.getInstance().getActivityService();
            List<Activity> activityList = service.findAllActivities();
            requestContent.setAttribute(RequestParameter.ACTIVITY_LIST, activityList);
            router.setPage(PageAddress.ACTIVITIES_PAGE);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, e);
            router.setPage(PageAddress.ERROR_PAGE);
        }
        return router;
    }
}
