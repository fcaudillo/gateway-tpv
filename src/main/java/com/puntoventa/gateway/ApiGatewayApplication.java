package com.puntoventa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.puntoventa.gateway.predicate.custom.QueryParamRoutePredicateFactory;
import com.puntoventa.gateway.predicate.custom.SetPathCustomGatewayFilterFactory;

@SpringBootApplication
@Configuration
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	
	
	@Bean
	public QueryParamRoutePredicateFactory getQueryParam(){
		return new QueryParamRoutePredicateFactory();
	}
	
	@Bean
	public SetPathCustomGatewayFilterFactory getPathCustom() {
		return new SetPathCustomGatewayFilterFactory();
	}
}