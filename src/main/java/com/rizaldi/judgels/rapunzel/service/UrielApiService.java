package com.rizaldi.judgels.rapunzel.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.rizaldi.judgels.rapunzel.config.UrielConfig;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "uriel")
class UrielApiService {
    private static final Logger LOG = LoggerFactory.getLogger(UrielApiService.class);
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private final String host;
    private final String scoreboardSecret;

    UrielApiService(UrielConfig config) {
        host = config.getHost();
        scoreboardSecret = config.getScoreboardSecret();
    }

    @Cacheable(key = "#containerJid", sync = true)
    public Contest getContest(String containerJid, String type) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("containerJid", containerJid);
        data.put("type", type);
        data.put("secret", scoreboardSecret);
        HttpContent content = new JsonHttpContent(JSON_FACTORY, data);

        GenericUrl api = new GenericUrl(host + "apis/scoreboards");
        LOG.info("Request: POST {}", api.toString());
        return HTTP_TRANSPORT.createRequestFactory()
                .buildPostRequest(api, content)
                .setParser(new JsonObjectParser(JSON_FACTORY))
                .execute()
                .parseAs(Contest.class);
    }
}
