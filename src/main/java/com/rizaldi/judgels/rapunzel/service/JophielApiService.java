package com.rizaldi.judgels.rapunzel.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.rizaldi.judgels.rapunzel.config.JophielConfig;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@CacheConfig(cacheNames = "jophiel")
class JophielApiService {
    private static final Logger LOG = LoggerFactory.getLogger(JophielApiService.class);
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private final String host;
    private final ContestantImageProviderService imageProvider;

    public JophielApiService(JophielConfig config, ContestantImageProviderService imageProvider) {
        host = config.getHost();
        this.imageProvider = imageProvider;
    }

    @Cacheable(key = "#jid", sync = true)
    public User getUser(String jid) throws IOException {
        GenericUrl api = new GenericUrl(host + "api/v1/users/" + jid);
        LOG.info("Request: GET {}", api.toString());
        User user = HTTP_TRANSPORT.createRequestFactory()
                .buildGetRequest(api)
                .setParser(new JsonObjectParser(JSON_FACTORY))
                .execute()
                .parseAs(User.class);
        imageProvider.updateProfilePicture(user);
        return user;
    }
}
