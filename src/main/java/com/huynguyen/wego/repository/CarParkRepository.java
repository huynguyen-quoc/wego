package com.huynguyen.wego.repository;

import com.huynguyen.wego.entity.CarParkEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarParkRepository extends CrudRepository<CarParkEntity, String> {

    @Query(value = "SELECT * FROM car_park_information l WHERE ST_Distance(l.coordinates, :centralPoint) < :distance", nativeQuery = true)
    Page<CarParkEntity> findLocationsNearby(@Param("centralPoint") Point centralPoint,
        @Param("distance") double distance,
        Pageable pageable);

}
