package com.huynguyen.wego.service;

import com.huynguyen.wego.model.CarParkSummary;
import java.util.List;
import java.util.Map;

public interface ParkAvailabilityService {
    void sync();

    Map<String, CarParkSummary> getSummary(List<String> carParkNumbers);
}
