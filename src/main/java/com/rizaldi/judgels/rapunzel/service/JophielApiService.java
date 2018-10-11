package com.rizaldi.judgels.rapunzel.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.rizaldi.judgels.rapunzel.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Executor requestExecutor = Executors.newFixedThreadPool(32);
    @Value("${rapunzel.jophiel.host}")
    private String host;

    @Cacheable(key = "#jids.hashCode()")
    public List<User> getUsers(List<String> jids) throws IOException, ExecutionException, InterruptedException {
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

    @Cacheable(key = "#jid")
    public User getUser(String jid) throws IOException {
        GenericUrl api = new GenericUrl("http://cpsso-gemastik.its.ac.id/api/v1/users/" + jid);
        HttpResponse httpResponse = HTTP_TRANSPORT.createRequestFactory()
                .buildGetRequest(api)
                .setParser(new JsonObjectParser(JSON_FACTORY))
                .execute();
        return httpResponse.parseAs(User.class);
    }
}
