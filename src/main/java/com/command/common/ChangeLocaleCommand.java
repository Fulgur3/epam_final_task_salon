package com.command.common;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.constant.RequestParameter;
import com.controller.SessionRequestContent;


public class ChangeLocaleCommand implements Command {

    @Override
    public PageRouter execute(SessionRequestContent requestContent){
        PageRouter router= new PageRouter();
        String lang= requestContent.getParameter(RequestParameter.LANGUAGE);
        router.setRedirect(true);
        router.setPage(constructRedirectAddress(requestContent));
        requestContent.setSessionAttribute(RequestParameter.LOCAL, lang);
        return router;
    }

    /**
     * Constructs redirect address using request parameters
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the page
     */
    private String constructRedirectAddress(SessionRequestContent requestContent) {
        String page = requestContent.getParameter(RequestParameter.PAGE);
        String query = requestContent.getParameter(RequestParameter.QUERY);
        return (query.isEmpty()) ? page : PageAddress.SERVLET_NAME + "?" + query;
    }
}
