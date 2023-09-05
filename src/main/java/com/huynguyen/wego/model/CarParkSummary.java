package com.huynguyen.wego.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarParkSummary {
    @JsonIgnore
    private String carParkNumber;
    @JsonProperty("total_lots")
    private Long totalLots;
    @JsonProperty("available_lots")
    private Long availableLots;

}