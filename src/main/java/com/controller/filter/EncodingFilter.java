package com.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private static final String HTML_CONTENT_TYPE = "text/html";
    private String code;

    public void init(FilterConfig filterConfig) {
        code = filterConfig.getInitParameter("encoding");
    }

    /**
     * Sets HTML content type and UTF-8 encoding
     *
     * @param request
     * @param response
     * @param chain
     *
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String codeRequest = request.getCharacterEncoding();
        if (code != null && !code.equalsIgnoreCase(codeRequest)) {
            request.setCharacterEncoding(code);
            response.setCharacterEncoding(code);
            response.setContentType(HTML_CONTENT_TYPE);
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
        code = null;
    }
}
