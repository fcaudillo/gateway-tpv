package com.puntoventa.gateway.predicate.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Shamelessly inspired by QueryRoutePredicateFactory
 * This will store the first value of the query parameter as a template variable under the same name
 */
public class QueryParamRoutePredicateFactory extends AbstractRoutePredicateFactory<QueryParamRoutePredicateFactory.Config> {

    public static final String PARAM_KEY = "param";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public QueryParamRoutePredicateFactory() {
        super(QueryParamRoutePredicateFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARAM_KEY);
    }

    @Override
    public Predicate<ServerWebExchange> apply(QueryParamRoutePredicateFactory.Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                if (exchange.getRequest().getQueryParams().containsKey(config.param)) {
                    String value = exchange.getRequest().getQueryParams().getFirst(config.param);
                    logger.debug("Saving {}={}", config.param, value);
                    
                    ServerWebExchangeUtils.putUriTemplateVariables(exchange, Map.of(config.param, value));
                    return true;
                }
                logger.debug("Query parameter {} not found.", config.param);
                return false;
            }

            @Override
            public String toString() {
                return String.format("Query: param=%s", config.getParam());
            }
        };
    }

    @Validated
    public static class Config {

        @NotEmpty
        private String param;

        public String getParam() {
            return param;
        }

        public QueryParamRoutePredicateFactory.Config setParam(String param) {
            this.param = param;
            return this;
        }
    }
}

