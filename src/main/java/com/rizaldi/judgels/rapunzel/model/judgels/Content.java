package com.rizaldi.judgels.rapunzel.model.judgels;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class Content {
    @Key
    private List<Entry> entries;
}
