package com.huynguyen.wego.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarParkInfo {

    @JsonProperty("total_lots")
    private Integer totalLots;
    @JsonProperty("lot_type")
    private String lotType;
    @JsonProperty("lots_available")
    private Integer lotsAvailable;
}
