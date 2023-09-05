package com.huynguyen.wego.service.impl;

import com.huynguyen.wego.client.CarParkAvailabilityClient;
import com.huynguyen.wego.client.dto.CarParkAvailabilityResponse;
import com.huynguyen.wego.client.dto.CarParkData;
import com.huynguyen.wego.client.dto.CarParkInfo;
import com.huynguyen.wego.entity.CarParkAvailabilityEntity;
import com.huynguyen.wego.model.CarParkSummary;
import com.huynguyen.wego.repository.CarParkAvailabilityRepository;
import com.huynguyen.wego.service.ParkAvailabilityService;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkAvailabilityServiceImpl implements ParkAvailabilityService {

    private final CarParkAvailabilityClient client;
    private final CarParkAvailabilityRepository carParkAvailabilityRepository;
    private final Clock singaporeClockTime;

    @Override
    public void sync() {
        ZonedDateTime time = ZonedDateTime.now(singaporeClockTime);
        log.info("start sync data for availability at [{}]", time);
        CarParkAvailabilityResponse carParkAvailabilityResponse = client.search(time);
        Map<String, List<CarParkInfo>> carParkInfoMap = carParkAvailabilityResponse.getItems().stream()
            .flatMap(carParkItem -> carParkItem.getCarParkData().stream())
            .collect(Collectors.toMap(
                CarParkData::getCarParkNumber,
                CarParkData::getCarParkInfo,
                (existingList, newList) -> {
                    List<CarParkInfo> mergedList = new ArrayList<>(existingList);
                    mergedList.addAll(newList);
                    return mergedList;
                }
            ));
        List<CarParkAvailabilityEntity> entities = carParkInfoMap.entrySet().stream()
            .flatMap(entry -> entry.getValue().stream()
                .map(carParkInfo -> {
                    CarParkAvailabilityEntity entity = new CarParkAvailabilityEntity();
                    entity.setCarParkNumber(entry.getKey()); // Set carParkNumber from map key
                    entity.setTotal(carParkInfo.getTotalLots());
                    entity.setAvailable(carParkInfo.getLotsAvailable());
                    entity.setType(carParkInfo.getLotType());
                    return entity;
                })
            )
            .toList();

        carParkAvailabilityRepository.saveAll(entities);
    }

    @Override
    public Map<String, CarParkSummary> getSummary(List<String> carParkNumbers) {
        return carParkAvailabilityRepository.getTotalAndAvailableSumsByCarParkNumbers(carParkNumbers).stream()
            .collect(Collectors.toMap(CarParkSummary::getCarParkNumber, s -> s));
    }
}
