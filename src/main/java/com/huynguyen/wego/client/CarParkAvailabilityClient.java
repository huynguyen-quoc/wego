package com.huynguyen.wego.client;

import com.huynguyen.wego.client.dto.CarParkAvailabilityResponse;
import java.time.ZonedDateTime;

public interface CarParkAvailabilityClient {

    CarParkAvailabilityResponse search(ZonedDateTime dateTime);

}
