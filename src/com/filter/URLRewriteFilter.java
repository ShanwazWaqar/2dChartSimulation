package com.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class URLRewriteFilter implements Filter {
    private static final String OLD_URL = "http://192.168.1.248:8080/Simulation/";
    private static final String NEW_URL = "/Simulation/";

    public void init(FilterConfig filterConfig) {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        CharResponseWrapper responseWrapper = new CharResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, (HttpServletResponse) responseWrapper);

        String originalContent = responseWrapper.toString();
        String modifiedContent = originalContent.replace(OLD_URL, NEW_URL);

        response.getWriter().write(modifiedContent);
    }

    public void destroy() {}

    // Wrapper class to intercept response
    private static class CharResponseWrapper extends HttpServletResponseWrapper {
        private CharArrayWriter output;

        public CharResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new CharArrayWriter();
        }

        public PrintWriter getWriter() {
            return new PrintWriter(output);
        }

        public String toString() {
            return output.toString();
        }
    }
}
