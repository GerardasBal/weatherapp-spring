package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.model.request.DataPointRequestModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface DataPointService {

    DataPointEntity parseDataPoint(DataPointRequestModel dataPointRequestModel);


    List<DataPointEntity> parseDataPoints(List<DataPointRequestModel> dataPointRequestModels);

    DataPointEntity removeDataPoint(DataPointEntity dataPointEntity);


    DataPointEntity addDataPoint(DataPointEntity dataPointEntity);

    List<DataPointEntity> addDataPoints(List<DataPointEntity> dataPointEntities);

    List<DataPointEntity> getAllDataPoints();

    Boolean isInDatabase(DataPointEntity dataPointEntity);

    LocalDateTime findLatestDate();

    List<DataPointEntity> getPointsByDate(LocalDate date);

    LocalDateTime[] getOldestAndLatestDates();

//    List<DataPointEntity> getBetweenDates(Date startDate, Date endDate);

}
