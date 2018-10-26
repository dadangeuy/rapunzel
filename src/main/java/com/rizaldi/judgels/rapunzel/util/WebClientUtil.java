package com.rizaldi.judgels.rapunzel.util;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public final class WebClientUtil {

    public static ExchangeFilterFunction logRequest(Logger log) {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("Request: {} {}", request.method(), request.url());
            return Mono.just(request);
        });
    }

    public static ExchangeFilterFunction logResponse(Logger log) {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("Response: {}", response.statusCode(), response);
            return Mono.just(response);
        });
    }
}
