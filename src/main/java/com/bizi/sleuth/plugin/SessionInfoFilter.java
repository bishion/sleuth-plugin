package com.bizi.sleuth.plugin;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
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
    private String[] headers;
    private Tracer tracer;

    public SessionInfoFilter(String[] headers,Tracer tracer) {
        this.headers = headers;
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("Filter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean skip = skipPattern.matcher(httpRequest.getRequestURI()).matches();
        if (!skip) {
            Span span = tracer.getCurrentSpan();
            for (String head : headers){
                String value = httpRequest.getHeader(head);
                if(StringUtils.isNotEmpty(value)){
                    span.setBaggageItem(head,value);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
