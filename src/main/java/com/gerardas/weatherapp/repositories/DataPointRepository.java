package com.gerardas.weatherapp.repositories;

import com.gerardas.weatherapp.entity.DataPointEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DataPointRepository extends CrudRepository<DataPointEntity, Long> {

    List<DataPointEntity> findByObservationTimeValueBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    DataPointEntity findDataPointEntityByObservationTimeValueEquals(LocalDateTime entityDate);

    @Query("select max(a.observationTimeValue) from DataPointEntity a")
    LocalDateTime findLatestDate();

    @Query("select min(a.observationTimeValue) from DataPointEntity a")
    LocalDateTime findObservationTimeMin();

    @Query("select count(a) from DataPointEntity a")
    Integer getCount();


}
