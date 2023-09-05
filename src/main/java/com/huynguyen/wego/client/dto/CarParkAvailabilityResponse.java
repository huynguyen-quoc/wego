package com.huynguyen.wego.client.dto;

import java.util.List;
import lombok.Data;

@Data
public class CarParkAvailabilityResponse {
    private List<CarParkItem> items;

}
