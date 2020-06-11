package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.repositories.DataPointRepository;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataPointServiceImpl implements DataPointService {


    private DataPointRepository dataPointRepository;

    @Autowired
    public void setDataPointRepository(DataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }



    @Override
    public DataPointEntity removeDataPoint(DataPointEntity dataPointEntity) {
        dataPointRepository.delete(dataPointEntity);
        return dataPointEntity;
    }

    @Override
    public DataPointEntity addDataPoint(DataPointEntity dataPointEntity) {
        if ((dataPointRepository.findDataPointEntityByObservationTimeValueEquals(dataPointEntity.getObservationTimeValue())) == null) {
            dataPointRepository.save(dataPointEntity);
        }
        return dataPointEntity;
    }

    @Override
    public List<DataPointEntity> addDataPoints(List<DataPointEntity> dataPointEntities) {
        dataPointEntities.forEach(this::addDataPoint
        );
        return dataPointEntities;
    }

    @Override
    public LocalDateTime findLatestDate() {
        return dataPointRepository.findLatestDate();
    }

    @Override
    public List<DataPointEntity> getPointsByDate(@NotNull LocalDate date) {
        if(date.isAfter(LocalDate.now())) return new ArrayList<>();

        LocalDateTime startDate = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(date, LocalTime.MAX);

        return dataPointRepository.findByObservationTimeValueBetween(startDate, endDate);
    }

    @Override
    public LocalDateTime[] getOldestAndLatestDates() {
        LocalDateTime[] localDateTimes = new LocalDateTime[2];
        localDateTimes[0] = dataPointRepository.findObservationTimeMin();
        localDateTimes[1] = dataPointRepository.findLatestDate();
        return localDateTimes;
    }

    @Override
    public List<DataPointEntity> getDataPointsByMonth(String year, String month) {
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(yearInt, monthInt, 1), LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(yearInt, monthInt, Month.of(Integer.parseInt(month)).maxLength()), LocalTime.MAX);
        return dataPointRepository.findByObservationTimeValueBetween(startDate, endDate);
    }

    @Override
    public Integer getCount() {
        return dataPointRepository.getCount();
    }
}
