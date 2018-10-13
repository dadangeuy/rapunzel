package com.rizaldi.judgels.rapunzel.service;

import com.google.gson.Gson;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
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
@CacheConfig(cacheNames = "uriel")
class UrielApiService {
    private static final Logger LOG = LoggerFactory.getLogger(UrielApiService.class);
    private WebClient client;
    @Value("${uriel.host}")
    private String host;

    @PostConstruct
    private void construct() {
        client = WebClient.builder()
                .baseUrl(host)
                .filter(WebClientUtil.logFilter(LOG))
                .build();
    }

    @Cacheable(key = "#containerJid", sync = true)
    public Mono<Contest> getContestMono(String containerJid, String secret, String type) {
        Contest.RequestBody body = Contest.RequestBody.builder()
                .containerJid(containerJid)
                .secret(secret)
                .type(type)
                .build();

        return client.post()
                .uri("/apis/scoreboards")
                .syncBody(body)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> new Gson().fromJson(json, Contest.class))
                .cache();
    }
}
