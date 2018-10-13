package com.rizaldi.judgels.rapunzel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "file:/var/rapunzel/application.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:/application.properties", ignoreResourceNotFound = true)
})
public class RapunzelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RapunzelApplication.class, args);
    }
}
