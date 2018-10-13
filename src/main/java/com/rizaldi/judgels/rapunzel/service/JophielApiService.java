package com.rizaldi.judgels.rapunzel.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@CacheConfig(cacheNames = "jophiel")
class JophielApiService {
    private static final Logger LOG = LoggerFactory.getLogger(JophielApiService.class);
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Executor requestExecutor = Executors.newFixedThreadPool(32);
    private WebClient client;
    @Value("${jophiel.host}")
    private String host;

    @PostConstruct
    private void construct() {
        client = WebClient.create(host);
    }

    @Cacheable(key = "#jids.hashCode()", sync = true)
    public List<User> getUsers(List<String> jids) throws IOException, ExecutionException, InterruptedException {
        LOG.info("fetch user data from " + host);
        List<Future<HttpResponse>> fHttpResponses = new ArrayList<>(jids.size());
        for (String jid : jids) {
            GenericUrl api = new GenericUrl(host + "/api/v1/users/" + jid);
            Future<HttpResponse> fHttpResponse = HTTP_TRANSPORT.createRequestFactory()
                    .buildGetRequest(api)
                    .setParser(new JsonObjectParser(JSON_FACTORY))
                    .executeAsync(requestExecutor);
            fHttpResponses.add(fHttpResponse);
        }

        List<User> users = new ArrayList<>(jids.size());
        for (Future<HttpResponse> fHttpResponse : fHttpResponses) {
            User user = fHttpResponse.get().parseAs(User.class);
            users.add(user);
        }
        return users;
    }

    public Flux<User> getUserFlux(List<String> jids) {
        Flux<String> jidFlux = Flux.fromIterable(jids);
        return jidFlux.flatMap(jid ->
                client.get()
                        .uri("/api/v1/users/" + jid)
                        .retrieve()
                        .bodyToMono(User.class)
        );
    }

    public Mono<User> getUserMono(String jid) {
        return client.get()
                .uri("/api/v1/users/" + jid)
                .retrieve()
                .bodyToMono(User.class);
    }
}
