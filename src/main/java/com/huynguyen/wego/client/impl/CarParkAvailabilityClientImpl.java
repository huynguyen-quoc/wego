package com.huynguyen.wego.client.impl;

import com.huynguyen.wego.client.CarParkAvailabilityClient;
import com.huynguyen.wego.client.dto.CarParkAvailabilityResponse;
import java.net.URI;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class CarParkAvailabilityClientImpl implements CarParkAvailabilityClient {

    private final RestTemplate restTemplate;
    @Value("${com.huynguyen.wego.availability.endpoint:https://api.data.gov.sg}")
    private String url;

    @Override
    public CarParkAvailabilityResponse search(ZonedDateTime dateTime) {
        URI targetUrl = UriComponentsBuilder.fromUriString(url)  // Build the base link
            .path("/v1/transport/carpark-availability")                            // Add path
            .build()                                                 // Build the URL
            .encode()                                                // Encode any URI items that need to be encoded
            .toUri();
        return restTemplate.getForObject(targetUrl, CarParkAvailabilityResponse.class);
    }
}
