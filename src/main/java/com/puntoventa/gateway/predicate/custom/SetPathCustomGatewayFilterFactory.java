package com.puntoventa.gateway.predicate.custom;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import reactor.core.publisher.Mono;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriTemplate;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.getUriTemplateVariables;

/**
 * @author Spencer Gibb
 */
public class SetPathCustomGatewayFilterFactory extends AbstractGatewayFilterFactory<SetPathCustomGatewayFilterFactory.Config> {

	/**
	 * Template key.
	 */
	public static final String TEMPLATE_KEY = "template";

	public SetPathCustomGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(TEMPLATE_KEY);
	}

	@Override
	public GatewayFilter apply(Config config) {
		UriTemplate uriTemplate = new UriTemplate(config.template);

		return new GatewayFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
				ServerHttpRequest req = exchange.getRequest();
				addOriginalRequestUrl(exchange, req.getURI());

				Map<String, String> uriVariables = getUriTemplateVariables(exchange);
				
				URI uri = uriTemplate.expand(uriVariables);
				String newPath = uri.getPath();

				exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, uri);

				ServerHttpRequest request = req.mutate().path(newPath).uri(uri).build();

				return chain.filter(exchange.mutate().request(request).build());
			}

			@Override
			public String toString() {
				return filterToStringCreator(SetPathCustomGatewayFilterFactory.this).append("template", config.getTemplate())
						.toString();
			}
		};
	}

	public static class Config {

		private String template;

		public String getTemplate() {
			return template;
		}

		public void setTemplate(String template) {
			this.template = template;
		}

	}

}

