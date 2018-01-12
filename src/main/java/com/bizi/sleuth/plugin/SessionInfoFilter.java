package com.bizi.sleuth.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.instrument.web.SleuthWebProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Order
public class SessionInfoFilter extends GenericFilterBean {
    private Pattern skipPattern = Pattern.compile(SleuthWebProperties.DEFAULT_SKIP_PATTERN);
    private static final Log log = LogFactory.getLog(SessionInfoFilter.class);
    private String[] headers;

    public SessionInfoFilter(String[] headers) {
        this.headers = headers;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("Filter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean skip = skipPattern.matcher(httpRequest.getRequestURI()).matches();
        if (!skip) {
            Span span = (Span) request.getAttribute("org.springframework.cloud.sleuth.instrument.web.TraceFilter.TRACE");
            Long traceId = span.getTraceId();
            SessionInfoCache.buildAllHeaders(traceId, headers, httpRequest);

        }
        chain.doFilter(request, response);
    }
}
