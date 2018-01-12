package com.bizi.sleuth.plugin;

import feign.Request;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.sleuth.Span;

import java.util.*;

public class SessionInfoBuilder {
    public static void buildSessionInfo(Request request, Span span) {

        Map<String, Collection<String>> header = request.headers();
        if (header == null) {
            header = new HashMap<>();
        }
        Map<String,String> cacheMap = SessionInfoCache.getAllCaches(span.getTraceId());
        for (String key : cacheMap.keySet()){
            String value = cacheMap.get(key);
            if (StringUtils.isEmpty(value)){
                continue;
            }
            header.put(key, Arrays.asList());
        }

    }
}
