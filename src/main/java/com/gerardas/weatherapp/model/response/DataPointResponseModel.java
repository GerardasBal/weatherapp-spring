package com.gerardas.weatherapp.model.response;

import java.time.LocalDateTime;

public class DataPointResponseModel {

    private Long id;

    private Double lon;

    private Double lat;

    private Double tempValue;

    private String tempUnits;

    private LocalDateTime observationTimeValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getTempValue() {
        return tempValue;
    }

    public void setTempValue(Double tempValue) {
        this.tempValue = tempValue;
    }

    public String getTempUnits() {
        return tempUnits;
    }

    public void setTempUnits(String tempUnits) {
        this.tempUnits = tempUnits;
    }

    public LocalDateTime getObservationTimeValue() {
        return observationTimeValue;
    }

    public void setObservationTimeValue(LocalDateTime observationTimeValue) {
        this.observationTimeValue = observationTimeValue;
    }

}
