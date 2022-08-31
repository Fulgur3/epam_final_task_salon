package com.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Calendar;

public class CopyrightTag extends TagSupport {
    private String year;
    private String projectName;
    private String message;


    @Override
    public int doStartTag() throws JspException {
        try {
            StringBuilder result = new StringBuilder();
            result.append("© ");
            result.append(year);
            result.append(" ");
            result.append(projectName);
            result.append(". ");
            if (message != null){
                result.append(message);
            }
            pageContext.getOut().write(result.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    public void setYear(String year) {
        this.year = year;
        String current = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        if (!current.equalsIgnoreCase(year)) {
            this.year += " – " + current;
        }

    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setMessage(String message) {
        if (message != null){
            this.message = message;
        }
    }
}
