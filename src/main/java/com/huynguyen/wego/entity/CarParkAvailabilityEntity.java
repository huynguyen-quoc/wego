package com.huynguyen.wego.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "car_park_availability")
@Setter
@Getter
@NoArgsConstructor
public class CarParkAvailabilityEntity {

    @Column(name = "car_park_number")
    @Id
    private String carParkNumber;
    private Integer total;
    private Integer available;
    private String type;

}
