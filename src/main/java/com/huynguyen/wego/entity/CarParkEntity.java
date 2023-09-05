package com.huynguyen.wego.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity(name = "car_park_information")
@Setter
@Getter
@NoArgsConstructor
public class CarParkEntity {

    @Column(name = "car_park_number")
    @Id
    private String carParkNumber;
    private String address;

    private Double longitude;

    private Double latitude;

    @Column(columnDefinition = "Point")
    private Point coordinates;

}
