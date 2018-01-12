package com.bizi.sleuth.plugin;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SessionInfoCache {
    private static final Cache<Long, Map<String,String>> cache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();

    public static void buildAllHeaders(Long key,String[] headers,HttpServletRequest request){
        Map<String,String> cacheMap = cache.getIfPresent(key);
        if(cacheMap == null){
            cacheMap = new HashMap<>();
            cache.put(key,cacheMap);
        }
        for (String header : headers){

            cacheMap.put(header,request.getHeader(header));
        }
    }
    public static String getSessionInfo(Long key,String cacheKey) {
        Map<String,String> cacheMap = cache.getIfPresent(key);
        if(cacheMap == null){
            return null;
        }
        return cacheMap.get(cacheKey);
    }
    public static void setSessionInfo(Long key,String cacheKey,String cacheValue) {
        Map<String,String> cacheMap = cache.getIfPresent(key);
        if(cacheMap == null){
            cacheMap = new HashMap<>();
            cache.put(key,cacheMap);
        }
        cacheMap.put(cacheKey,cacheValue);
    }
    public static Map<String,String> getAllCaches(Long key){
        Map<String,String> cacheMap = cache.getIfPresent(key);
        if(cacheMap == null){
            return Collections.emptyMap();
        }
        return cacheMap;
    }
}
