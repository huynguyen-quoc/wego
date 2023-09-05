package com.huynguyen.wego.config;

import com.huynguyen.wego.service.CarParkService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "com.huynguyen.wego.import.enabled", havingValue = "true", matchIfMissing = true)
public class ImportDataConfiguration {

    private final CarParkService carParkService;

    @PostConstruct
    void postConstruct() {
        carParkService.sync();
    }
}
