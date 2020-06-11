package com.gerardas.weatherapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DataPointRequestModel {

    private Long id;

    private Double lon;

    private Double lat;

    private Map<String, Object> temp;

    @JsonProperty("observation_time")
    private Map<String, Object> observationTime;

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

    public Map<String, Object> getTemp() {
        return temp;
    }

    public void setTemp(Map<String, Object> temp) {
        this.temp = temp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(Map<String, Object> observationTime) {
        this.observationTime = observationTime;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                ", temp=" + temp +
                ", observationTime=" + observationTime +
                '}';
    }
}
