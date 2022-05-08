package io.medveckis.factory;

import io.micronaut.context.annotation.Factory;
import io.micronaut.http.client.LoadBalancer;
import io.micronaut.http.client.loadbalance.DiscoveryClientLoadBalancerFactory;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

@Factory
public class APIGatewayFactory {

    @Singleton
    public Map<LoadBalancer, List<String>> routingMap(DiscoveryClientLoadBalancerFactory factory) {
        return Map.of(
                factory.create("book-service"), List.of("books", "categories"),
                factory.create("user-service"), List.of("users"),
                factory.create("borrow-management-service"), List.of("borrow-management")
        );
    }
}
