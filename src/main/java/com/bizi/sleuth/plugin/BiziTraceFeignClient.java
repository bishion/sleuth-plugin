package com.bizi.sleuth.plugin;

import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.cloud.netflix.feign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.trace.DefaultTracer;

import java.io.IOException;

public class BiziTraceFeignClient implements Client{
    private LoadBalancerFeignClient feignClient;
    private DefaultTracer defaultTracer;

    public BiziTraceFeignClient(DefaultTracer defaultTracer, CachingSpringLoadBalancerFactory cachingFactory,
                                SpringClientFactory clientFactory) {
        this.defaultTracer = defaultTracer;
        this.feignClient = new LoadBalancerFeignClient(new Default(null,null), cachingFactory, clientFactory);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        Span span = defaultTracer.getCurrentSpan();
        SessionInfoBuilder.buildSessionInfo(request,span);
        return feignClient.execute(request,options);
    }
}
