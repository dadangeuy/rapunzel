package com.rizaldi.judgels.rapunzel.service;

import com.google.gson.Gson;
import com.rizaldi.judgels.rapunzel.config.UrielConfig;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import com.rizaldi.judgels.rapunzel.util.WebClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
@CacheConfig(cacheNames = "uriel")
class UrielApiService {
    private static final Logger LOG = LoggerFactory.getLogger(UrielApiService.class);
    private static final Gson GSON = new Gson();
    private final UrielConfig config;
    private final WebClient client;

    UrielApiService(UrielConfig config) {
        this.config = config;
        client = WebClient.builder()
                .baseUrl(config.getHost())
                .filter(WebClientUtil.logRequest(LOG))
                .build();
    }

    @PostConstruct
    private void construct() {
    }

    @Cacheable(key = "#containerJid", sync = true)
    public Mono<Contest> getContestMono(String containerJid, String type) {
        Contest.RequestBody body = Contest.RequestBody.builder()
                .containerJid(containerJid)
                .secret(config.getScoreboardSecret())
                .type(type)
                .build();

        return client.post()
                .uri("/apis/scoreboards")
                .syncBody(body)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> GSON.fromJson(json, Contest.class))
                .cache();
    }
}
