package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.model.request.DataPointRequestModel;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherApiService {

    void updateDatabase();

    List<DataPointRequestModel> fetchVilniusHistoryDataFromWeatherAPI(LocalDateTime latestDateInDB);
}
