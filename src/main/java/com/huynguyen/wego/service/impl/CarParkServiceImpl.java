package com.huynguyen.wego.service.impl;

import com.huynguyen.wego.client.CoordinateConverter;
import com.huynguyen.wego.client.dto.CoordinateResponse;
import com.huynguyen.wego.entity.CarParkEntity;
import com.huynguyen.wego.repository.CarParkRepository;
import com.huynguyen.wego.service.CarParkService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarParkServiceImpl implements CarParkService {

    private final CarParkRepository carParkRepository;
    private final CoordinateConverter coordinateConverter;
    private static final Pattern PATTERN = Pattern.compile("\"([^\"]*)\"");
    @Value("${com.huynguyen.wego.car-park.csv:/csv/carpark.csv}")
    private String filePath;
    @Override
    public void sync() {
        log.info("start import data for file=[{}]", filePath);
        Resource resource = new ClassPathResource(filePath);

        try (InputStream inputStream = resource.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            int lineCount = 0;
            List<CarParkEntity> carParkEntities = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                // Skip the first line
                if (lineCount == 0) {
                    lineCount++;
                    continue;
                }
                // Process each line of the CSV file here
                List<String> columns = new ArrayList<>();
                Matcher matcher = PATTERN.matcher(line);
                while (matcher.find()) {
                    columns.add(matcher.group(1).trim());
                }
                String carParkNumber = columns.get(0);
                String address = columns.get(1);
                String latitude = columns.get(2);
                String longitude = columns.get(3);

                CoordinateResponse coordinateResponse = coordinateConverter.convert(longitude, latitude);
                // Set parameters for the INSERT statement

                CarParkEntity carParkEntity = new CarParkEntity();
                carParkEntity.setCarParkNumber(carParkNumber);
                carParkEntity.setAddress(address);
                carParkEntity.setLongitude(coordinateResponse.getLongitude());
                carParkEntity.setLatitude(coordinateResponse.getLatitude());
                GeometryFactory geometryFactory = new GeometryFactory();
                Point point = geometryFactory.createPoint(
                    new Coordinate(coordinateResponse.getLongitude(), coordinateResponse.getLatitude()));
                carParkEntity.setCoordinates(point);

                // Add the statement to the batch
                carParkEntities.add(carParkEntity);
                lineCount++;

                // If the batch size is reached, execute the batch
                if (lineCount % 300 == 0) {
                    carParkRepository.saveAll(carParkEntities);
                    carParkEntities.clear();
                }
            }
            carParkRepository.saveAll(carParkEntities);
        } catch (Exception e) {
            log.error("got exception to import data", e);
        }
        log.info("end import data for file=[{}]", filePath);
    }

    @Override
    public Page<CarParkEntity> search(Double longitude, Double latitude, Pageable pageable) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point centralPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        Distance distance = new Distance(5, Metrics.KILOMETERS);

        return carParkRepository.findLocationsNearby(centralPoint,
            distance.getValue() * 1000, pageable);
    }


}
