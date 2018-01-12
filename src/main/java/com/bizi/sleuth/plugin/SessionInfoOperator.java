package com.bizi.sleuth.plugin;

import org.springframework.cloud.sleuth.trace.DefaultTracer;

public class SessionInfoOperator {
    private DefaultTracer defaultTracer;

    public SessionInfoOperator(DefaultTracer defaultTracer) {
        this.defaultTracer = defaultTracer;
    }
    public String getSessionInfo(String cacheKey){
        return SessionInfoCache.getSessionInfo(defaultTracer.getCurrentSpan().getTraceId(),cacheKey);
    }
    public void setSessionInfo(String cacheKey,String cacheValue){

        Long key = defaultTracer.getCurrentSpan().getTraceId();
        SessionInfoCache.setSessionInfo(key,cacheKey,cacheValue);
    }
}
