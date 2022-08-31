package com.command;

import com.controller.PageRouter;
import com.controller.SessionRequestContent;

public interface Command {

    PageRouter execute(SessionRequestContent requestContent);
}
