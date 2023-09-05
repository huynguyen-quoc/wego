package com.huynguyen.wego.client.impl;

import com.huynguyen.wego.client.CoordinateConverter;
import com.huynguyen.wego.client.dto.CoordinateResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class OpenMapCoordinateConverterImpl implements CoordinateConverter {

    private final RestTemplate restTemplate;
    @Value("${com.huynguyen.wego.open-map.endpoint:https://developers.onemap.sg}")
    private String url;
    @Override
    public CoordinateResponse convert(String longitude, String latitude) {
        URI targetUrl = UriComponentsBuilder.fromUriString(url)  // Build the base link
            .path("/commonapi/convert/3414to4326")                            // Add path
            .queryParam("Y", latitude)                                // Add one or more query params
            .queryParam("X", longitude)                                // Add one or more query params
            .build()                                                 // Build the URL
            .encode()                                                // Encode any URI items that need to be encoded
            .toUri();
        return restTemplate.getForObject(
            targetUrl,
            CoordinateResponse.class);
    }
}
