package com.banking.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator bankingRoutes(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route( p -> (
						p.path("/booking/movies/**")
								.filters(f -> f.rewritePath("/booking/movies/(?<segment>.*)","/${segment}")
										.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
												.setKeyResolver(keyResolver())))
						).uri("lb://MOVIES"))
				.route( p -> (
						p.path("/booking/show/**")
								.filters(f -> f.rewritePath("/booking/show/(?<segment>.*)","/${segment}")
										.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
												.setKeyResolver(keyResolver())))
				).uri("lb://SHOWS"))
				.route( p -> (
						p.path("/booking/payments/**")
								.filters(f -> f.rewritePath("/booking/payments/(?<segment>.*)","/${segment}")
										.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
												.setKeyResolver(keyResolver())))
				).uri("lb://PAYMENTS"))
				.route( p -> (
						p.path("/booking/ticket/**")
								.filters(f -> f.rewritePath("/booking/ticket/(?<segment>.*)","/${segment}")
										.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
												.setKeyResolver(keyResolver())))
				).uri("lb://TICKETBOOKING"))
				.build();
	}


	@Bean
	public RedisRateLimiter redisRateLimiter(){
		return new RedisRateLimiter(1,1,1);
	}

	@Bean
	public KeyResolver keyResolver(){
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("USER"))
				.defaultIfEmpty("ANONYMOUS");
	}

}
