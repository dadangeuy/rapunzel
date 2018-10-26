package com.rizaldi.judgels.rapunzel.model.judgels;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class User {
    @Key
    private String jid;
    @Key
    private String username;
    @Key
    private String name;
    @Key
    private String profilePictureUrl;
}
