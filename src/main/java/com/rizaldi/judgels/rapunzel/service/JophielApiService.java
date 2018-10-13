package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.model.judgels.User;
import com.rizaldi.judgels.rapunzel.util.WebClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
@CacheConfig(cacheNames = "jophiel")
class JophielApiService {
    private static final Logger LOG = LoggerFactory.getLogger(JophielApiService.class);
    private WebClient client;
    @Value("${jophiel.host}")
    private String host;

    @PostConstruct
    private void construct() {
        client = WebClient.builder()
                .baseUrl(host)
                .filter(WebClientUtil.logFilter(LOG))
                .build();
    }

    @Cacheable(key = "#jid", sync = true)
    public Mono<User> getUserMono(String jid) {
        return client.get()
                .uri("/api/v1/users/" + jid)
                .retrieve()
                .bodyToMono(User.class)
                .cache();
    }
}
