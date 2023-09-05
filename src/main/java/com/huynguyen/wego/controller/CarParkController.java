package com.huynguyen.wego.controller;

import com.huynguyen.wego.entity.CarParkEntity;
import com.huynguyen.wego.model.CarParkInfoWithTotalAvailableLots;
import com.huynguyen.wego.model.CarParkSummary;
import com.huynguyen.wego.service.CarParkService;
import com.huynguyen.wego.service.ParkAvailabilityService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/carparks")
@RequiredArgsConstructor
public class CarParkController {

    private final CarParkService carParkService;
    private final ParkAvailabilityService parkAvailabilityService;

    private final Pageable DEFAULT_PAGE = PageRequest.of(0, 50);

    @GetMapping("/nearest")
    public List<CarParkInfoWithTotalAvailableLots> findLocationsNearby(
        @RequestParam("latitude") double centralLatitude,
        @RequestParam("longitude") double centralLongitude,
        @RequestParam(value = "page", defaultValue = "1", required = false) int page,
        @RequestParam(value = "per_page", defaultValue = "50", required = false) int perPage) {
        Pageable pageable = DEFAULT_PAGE;
        if (page > 1 && perPage <= 50) {
            pageable = PageRequest.of(page - 1, perPage);
        }
        Page<CarParkEntity> carParkEntities = carParkService.search(centralLatitude, centralLongitude, pageable);
        Map<String, CarParkInfoWithTotalAvailableLots> carParkInfoWithTotalAvailableLotsMap = new HashMap<>();
        List<String> carParkNumbers = new ArrayList<>();
        for (CarParkEntity carParkEntity : carParkEntities.getContent()) {
            carParkNumbers.add(carParkEntity.getCarParkNumber());
            CarParkInfoWithTotalAvailableLots carParkInfoWithTotalAvailableLots = new CarParkInfoWithTotalAvailableLots();
            carParkInfoWithTotalAvailableLots.setAddress(carParkEntity.getAddress());
            carParkInfoWithTotalAvailableLots.setLongitude(carParkEntity.getLongitude());
            carParkInfoWithTotalAvailableLots.setLatitude(carParkEntity.getLatitude());
            carParkInfoWithTotalAvailableLotsMap.put(carParkEntity.getCarParkNumber(),
                carParkInfoWithTotalAvailableLots);
        }
        Map<String, CarParkSummary> carParkSummaryMap = parkAvailabilityService.getSummary(carParkNumbers);
        for (String key : carParkInfoWithTotalAvailableLotsMap.keySet()) {
            CarParkInfoWithTotalAvailableLots carParkInfoWithTotalAvailableLots = carParkInfoWithTotalAvailableLotsMap.get(
                key);
            carParkInfoWithTotalAvailableLots.setCarParkSummary(carParkSummaryMap.get(key));
        }

        return carParkInfoWithTotalAvailableLotsMap.values().stream()
            .filter(s -> Objects.nonNull(s.getCarParkSummary())).toList();

    }

}
