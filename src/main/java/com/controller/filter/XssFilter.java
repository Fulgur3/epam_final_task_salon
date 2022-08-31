package com.controller.filter;

import org.owasp.esapi.ESAPI;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

@WebFilter(filterName = "XssFilter",
        urlPatterns = {"/app"})
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
    }

    static class XssRequestWrapper extends HttpServletRequestWrapper {
        private static final String EMPTY = "";
        private static final String NULL_TERMINATOR = "\0";
        private static Pattern[] patterns = {
                Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
                Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
                Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
                Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
                Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
                Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
        };

        XssRequestWrapper(HttpServletRequest servletRequest) {
            super(servletRequest);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);
            if (values == null) {
                return new String[0];
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = stripXss(values[i]);
            }
            return encodedValues;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = super.getParameterMap();
            if (parameterMap == null) {
                return new TreeMap<>();
            }
            Map<String, String[]> encodedParameterMap = new TreeMap<>();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = stripXss(entry.getKey());
                String[] values = entry.getValue();
                String[] encodedValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    encodedValues[i] = stripXss(values[i]);
                }
                encodedParameterMap.put(key, encodedValues);
            }
            return encodedParameterMap;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            return stripXss(value);
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return stripXss(value);
        }

        /**
         * Removes suspicious content from request parameters
         *
         * @param value Value to check
         *
         * @return Cleaned string
         */
        private String stripXss(String value) {
            if (value != null) {
                value = ESAPI.encoder().canonicalize(value);
                value = value.replaceAll(NULL_TERMINATOR, EMPTY);
                for (Pattern scriptPattern : patterns) {
                    value = scriptPattern.matcher(value).replaceAll(EMPTY);
                }
            }
            return value;
        }
    }
}