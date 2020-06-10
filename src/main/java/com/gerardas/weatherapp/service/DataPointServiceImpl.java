package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.model.request.DataPointRequestModel;
import com.gerardas.weatherapp.repositories.DataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.TemporalAccessorParser;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataPointServiceImpl implements DataPointService {


    private DataPointRepository dataPointRepository;

    @Autowired
    public void setDataPointRepository(DataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }

    @Override
    public DataPointEntity parseDataPoint(DataPointRequestModel dataPointRequestModel) {
        DataPointEntity dataPointEntity = new DataPointEntity();
        dataPointEntity.setLat(dataPointRequestModel.getLat());
        dataPointEntity.setLon(dataPointRequestModel.getLon());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
//        LocalDateTime dateTime = LocalDateTime.from(
//                dateTimeFormatter.parse(
//                        String.valueOf(
//                                dataPointRequestModel.getObservationTime().get("value"))));


        String stringDate = String.valueOf(dataPointRequestModel.getObservationTime().get("value"));
        TemporalAccessor temporalAccessor = dateTimeFormatter.parse(stringDate);

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.from(temporalAccessor), ZoneId.systemDefault());


        dataPointEntity.setObservationTimeValue(dateTime);
        dataPointEntity.setTempUnits(String.valueOf(dataPointRequestModel.getTemp().get("units")));
        dataPointEntity.setTempValue(Double.parseDouble(String.valueOf(dataPointRequestModel.getTemp().get("value"))));
        return dataPointEntity;
    }


    @Override
    public List<DataPointEntity> parseDataPoints(List<DataPointRequestModel> dataPointRequestModels) {
        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        dataPointRequestModels.forEach(dataPointRequestModel -> dataPointEntities.add(parseDataPoint(dataPointRequestModel)));
        return dataPointEntities;
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
    public List<DataPointEntity> getAllDataPoints() {
        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        dataPointRepository.findAll().iterator().forEachRemaining(dataPointEntities::add);
        return dataPointEntities;
    }

    @Override
    public LocalDateTime findLatestDate() {
        return dataPointRepository.findLatestDate();
    }

    //    @Override
//    public List<DataPointEntity> getBetweenDates(Date startDate, Date endDate) {
//
//        return dataPointRepository.findByDateBetween(startDate, endDate);
//    }

    @Override
    public Boolean isInDatabase(DataPointEntity dataPointEntity) {
        return (dataPointRepository
                .findDataPointEntityByObservationTimeValueEquals(dataPointEntity
                        .getObservationTimeValue())) != null;
    }

    @Override
    public List<DataPointEntity> getPointsByDate(LocalDate date) {
        LocalDateTime startDate = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(date, LocalTime.MAX);
        return dataPointRepository.findByObservationTimeValueBetween(startDate, endDate);
    }


    @Override
    public LocalDateTime[] getOldestAndLatestDates() {
        LocalDateTime[] localDateTimes = new LocalDateTime[2];
        localDateTimes[0] = dataPointRepository.getObservationTimeMin();
        localDateTimes[1] = dataPointRepository.findLatestDate();
        return localDateTimes;
    }
}
