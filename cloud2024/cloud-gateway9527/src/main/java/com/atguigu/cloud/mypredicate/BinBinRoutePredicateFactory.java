package com.atguigu.cloud.mypredicate;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class BinBinRoutePredicateFactory extends AbstractRoutePredicateFactory<BinBinRoutePredicateFactory.Config> {

    public BinBinRoutePredicateFactory() {
        super(BinBinRoutePredicateFactory.Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                return serverWebExchange.getRequest().getQueryParams().getFirst("username").equals(config.getUsername());
            }
        };
    }
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("username");
    }

    public static class Config {

        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
