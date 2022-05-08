package io.medveckis.web.filter;

import io.micronaut.discovery.ServiceInstance;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.client.LoadBalancer;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;

@Filter("/**")
public class APIGatewayFilter implements HttpServerFilter {
    private final Map<LoadBalancer, List<String>> routingMap;
    private final ProxyHttpClient proxyHttpClient;

    public APIGatewayFilter(Map<LoadBalancer, List<String>> routingMap, ProxyHttpClient proxyHttpClient) {
        this.routingMap = routingMap;
        this.proxyHttpClient = proxyHttpClient;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        LoadBalancer loadBalancer = routingMap
                .entrySet()
                .stream()
                .filter(entry -> hasMatch(entry.getValue(), request.getPath()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();

        return Flowable.fromPublisher(loadBalancer.select())
                .flatMap(serviceInstance -> {
                    MutableHttpRequest<?> finalRequest = prepareRequestForTarget(request, serviceInstance);
                    return proxyHttpClient.proxy(finalRequest);
                });
    }

    private MutableHttpRequest<?> prepareRequestForTarget(HttpRequest<?> request, ServiceInstance serviceInstance) {
        return request.mutate()
                .uri(uri -> uri
                        .scheme("http")
                        .host(serviceInstance.getHost())
                        .port(serviceInstance.getPort())
                );
    }

    private boolean hasMatch(List<String> matchers, String path) {
        return matchers.stream().anyMatch(path::contains);
    }
}
