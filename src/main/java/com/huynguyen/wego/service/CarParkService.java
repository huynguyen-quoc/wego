package com.huynguyen.wego.service;

import com.huynguyen.wego.entity.CarParkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarParkService {

    void sync();

    Page<CarParkEntity> search(Double longitude, Double latitude, Pageable pageable);


}
