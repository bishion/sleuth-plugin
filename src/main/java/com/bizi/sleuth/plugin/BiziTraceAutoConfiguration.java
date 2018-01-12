package com.bizi.sleuth.plugin;

import feign.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.sleuth.trace.DefaultTracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "bizi.sleuth.config.enabled", matchIfMissing = true)
public class BiziTraceAutoConfiguration {

	@Value("${bizi.sleuth.config.headers:lang_info}")
	private String[] headers;
	@Bean
	public SessionInfoFilter sessionInfoFilter() {
		return new SessionInfoFilter(headers);
	}

	@Bean
	public SessionInfoOperator sessionInfoOperator(DefaultTracer defaultTracer){
		return new SessionInfoOperator(defaultTracer);
	}
	@Bean
	public Client biziTraceFeignClient(DefaultTracer defaultTracer, CachingSpringLoadBalancerFactory cachingFactory,
									   SpringClientFactory clientFactory){
		return new BiziTraceFeignClient(defaultTracer,cachingFactory,clientFactory);
	}
}
