package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.entity.DataPointEntity;
import com.sun.istack.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DataPointService {

    DataPointEntity removeDataPoint(DataPointEntity dataPointEntity);

    DataPointEntity addDataPoint(DataPointEntity dataPointEntity);

    List<DataPointEntity> addDataPoints(List<DataPointEntity> dataPointEntities);

    LocalDateTime findLatestDate();

    List<DataPointEntity> getPointsByDate(@NotNull LocalDate date);

    LocalDateTime[] getOldestAndLatestDates();

    List<DataPointEntity> getDataPointsByMonth(String year, String month);

    Integer getCount();


}
