package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.config.JophielConfig;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import com.rizaldi.judgels.rapunzel.util.WebClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@CacheConfig(cacheNames = "jophiel")
class JophielApiService {
    private static final Logger LOG = LoggerFactory.getLogger(JophielApiService.class);
    private final WebClient client;

    public JophielApiService(JophielConfig config) {
        client = WebClient.builder()
                .baseUrl(config.getHost())
                .filter(WebClientUtil.logRequest(LOG))
                .filter(WebClientUtil.logResponse(LOG))
                .build();
    }

    @Deprecated
    @Cacheable(key = "'mono-' + #jid", sync = true)
    public Mono<User> getUserMono(String jid) {
        return client.get()
                .uri("/api/v1/users/" + jid)
                .retrieve()
                .bodyToMono(User.class)
                .cache();
    }

    @Cacheable(key = "#jid", sync = true)
    public User getUser(String jid) {
        return client.get()
                .uri("/api/v1/users/" + jid)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }
}
