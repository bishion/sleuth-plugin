package com.bizi.sleuth.plugin;

import org.springframework.cloud.sleuth.Tracer;

public class SessionInfoOperator {
    private Tracer tracer;

    public SessionInfoOperator(Tracer tracer) {
        this.tracer = tracer;
    }

    public String getSessionInfo(String key){
        return tracer.getCurrentSpan().getBaggageItem(key);
    }
    public void setSessionInfo(String key,String value){
        tracer.getCurrentSpan().setBaggageItem(key,value);
    }

}
