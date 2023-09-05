package com.huynguyen.wego.config;

import java.time.Clock;
import java.util.TimeZone;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }


    @Bean
    public Clock singaporeClockTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Singapore");
        return Clock.system(timeZone.toZoneId());
    }

}
