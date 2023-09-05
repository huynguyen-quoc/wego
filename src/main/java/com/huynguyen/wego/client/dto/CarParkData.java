package com.huynguyen.wego.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class CarParkData {
    @JsonProperty("carpark_number")
    private String carParkNumber;
    @JsonProperty("carpark_info")
    private List<CarParkInfo> carParkInfo;

}
