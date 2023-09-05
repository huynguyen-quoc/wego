package com.huynguyen.wego.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class CarParkInfoWithTotalAvailableLots {
    private String address;

    private Double latitude;

    private Double longitude;
    @JsonUnwrapped
    private CarParkSummary carParkSummary;

}
