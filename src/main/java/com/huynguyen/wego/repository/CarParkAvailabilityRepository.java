package com.huynguyen.wego.repository;

import com.huynguyen.wego.entity.CarParkAvailabilityEntity;
import com.huynguyen.wego.model.CarParkSummary;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarParkAvailabilityRepository extends CrudRepository<CarParkAvailabilityEntity, String> {

    @Query("SELECT  new com.huynguyen.wego.model.CarParkSummary(e.carParkNumber, SUM(e.total), SUM(e.available)) " +
        "FROM com.huynguyen.wego.entity.CarParkAvailabilityEntity e " +
        "WHERE e.carParkNumber IN :ids " +
        "GROUP BY e.carParkNumber " +
        "HAVING SUM(e.available) > 0")
    List<CarParkSummary> getTotalAndAvailableSumsByCarParkNumbers(@Param("ids") List<String> carParkNumbers);
}
