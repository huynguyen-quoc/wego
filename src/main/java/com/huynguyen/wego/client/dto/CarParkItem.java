package com.huynguyen.wego.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CarParkItem {

    private ZonedDateTime timestamp;

    @JsonProperty(value = "carpark_data")
    private List<CarParkData> carParkData;
}
