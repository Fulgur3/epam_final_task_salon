package com.tag;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class WelcomeTag extends TagSupport {
    private String name;
    private String message;


    @Override
    public int doStartTag() throws JspException {
        try {
            StringBuilder result = new StringBuilder();
            result.append(message);
            result.append(", ");
            result.append(name);
            result.append("!");
            pageContext.getOut().write(result.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
