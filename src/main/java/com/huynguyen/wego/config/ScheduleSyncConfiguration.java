package com.huynguyen.wego.config;

import com.huynguyen.wego.service.ParkAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class ScheduleSyncConfiguration {

    private final ParkAvailabilityService parkAvailabilityService;


    @Scheduled(cron = "${com.huynguyen.wego.availability.sync.expression:0 * * * * *}")
    void sync() {
        parkAvailabilityService.sync();
    }
}
