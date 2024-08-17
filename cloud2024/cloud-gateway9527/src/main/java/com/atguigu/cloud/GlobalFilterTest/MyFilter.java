package com.atguigu.cloud.GlobalFilterTest;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
@Component
public class MyFilter implements GlobalFilter, Ordered {
    public static final String BEGIN = "BEGIN-TIME";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(BEGIN, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long beginTime = (Long) exchange.getAttributes().get(BEGIN);
            if (beginTime != null) {
                System.out.println("=================================");
                System.out.println(exchange.getRequest().getURI());
                System.out.println(System.currentTimeMillis() - beginTime + "ms");
                System.out.println("=================================");
            }
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
