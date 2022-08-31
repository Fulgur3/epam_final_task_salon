package com.command.user;

import com.command.Command;
import com.controller.PageRouter;
import com.constant.PageAddress;
import com.controller.SessionRequestContent;


public class LogoutCommand implements Command {

    /**
     * Redirects to the home page
     *
     * @param requestContent Request and session parameters and attributes
     *
     * @return Address of the next page
     */
    @Override
    public PageRouter execute(SessionRequestContent requestContent) {
        PageRouter router = new PageRouter();
        router.setRedirect(true);
        router.setPage(PageAddress.HOME_PAGE);
        return router;
    }
}