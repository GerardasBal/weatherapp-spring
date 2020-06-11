package com.gerardas.weatherapp.util;

import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.model.request.DataPointRequestModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

public class DataPointParser {


    public static DataPointEntity parseDataPoint(DataPointRequestModel dataPointRequestModel) {
        DataPointEntity dataPointEntity = new DataPointEntity();
        dataPointEntity.setLat(dataPointRequestModel.getLat());
        dataPointEntity.setLon(dataPointRequestModel.getLon());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
        String stringDate = String.valueOf(dataPointRequestModel.getObservationTime().get("value"));
        TemporalAccessor temporalAccessor = dateTimeFormatter.parse(stringDate);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.from(temporalAccessor), ZoneId.systemDefault());

        dataPointEntity.setObservationTimeValue(dateTime);
        dataPointEntity.setTempUnits(String.valueOf(dataPointRequestModel.getTemp().get("units")));
        dataPointEntity.setTempValue(Double.parseDouble(String.valueOf(dataPointRequestModel.getTemp().get("value"))));
        return dataPointEntity;
    }

    public static List<DataPointEntity> parseDataPoints(List<DataPointRequestModel> dataPointRequestModels) {
        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        dataPointRequestModels.forEach(dataPointRequestModel -> dataPointEntities.add(parseDataPoint(dataPointRequestModel)));
        return dataPointEntities;
    }

}
