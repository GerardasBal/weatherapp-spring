package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.model.request.DataPointRequestModel;
import com.gerardas.weatherapp.util.DataPointParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherAPIServiceImpl implements WeatherApiService {

    Logger logger = LoggerFactory.getLogger(WeatherApiService.class);


    private Boolean callLimitReached = false;

    private LocalDateTime callLimitReachedTimeStamp = LocalDateTime.now();

    private RestTemplate restTemplate;


    private DataPointService dataPointService;

    @Autowired
    public void setDataPointService(DataPointService dataPointService) {
        this.dataPointService = dataPointService;
    }

    @Autowired
    public void setRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void updateDatabase() {

        LocalDateTime latestDateInDB;
        if (dataPointService.getCount() == 0) {
            latestDateInDB = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusMonths(2);
        } else {
            latestDateInDB = dataPointService.findLatestDate();
        }

        if (callLimitReachedTimeStamp.isBefore(LocalDateTime.now().minusMinutes(30))) {
            callLimitReached = false;
        }

        LocalDateTime latestDateInDBOld = latestDateInDB.minusHours(1);
        String updateMessage = "";
        while (!callLimitReached && !latestDateInDB.isEqual(latestDateInDBOld)) {
            List<DataPointRequestModel> dataPointRequestModels = fetchVilniusHistoryDataFromWeatherAPI(latestDateInDB);
            if (dataPointRequestModels != null) {
                dataPointService.addDataPoints(
                        DataPointParser.parseDataPoints(dataPointRequestModels));
                updateMessage = "Database updated.";
            } else {
                updateMessage = "Database already Up-to-date";
            }
            latestDateInDBOld = latestDateInDB;
            latestDateInDB = dataPointService.findLatestDate();
            logger.info("Updating database... Latest data point time: " + latestDateInDB);
        }
        if(callLimitReached){
            logger.warn("Call limit reached at: " + callLimitReachedTimeStamp + ". Latest data point time: " + latestDateInDB);
        } else {
            logger.info(updateMessage + " Latest data point time: " + latestDateInDB);
        }
    }

    @Override
    public List<DataPointRequestModel> fetchVilniusHistoryDataFromWeatherAPI(LocalDateTime latestDateInDB) {

        String apiUrl = "https://api.climacell.co/v3/weather/historical/station";

        String apiKey = "uY5089LCgIWqTzQQOnoQ4Bvg94AwF7Np";
        double lat = 54.687157;
        double lon = 25.279652;
        String unitSystem = "si";
        String fields = "temp";

        LocalDateTime startDate = latestDateInDB;
        LocalDateTime endDate = latestDateInDB.plusDays(1);

        startDate = startDate.minusHours(3);
        endDate = endDate.minusHours(3);

        String startDateNew = startDate.toString() + ":00Z";
        String endDateNew = endDate.toString() + ":00Z";

        String apiUrlFull = apiUrl +
                "?lat=" +
                lat +
                "&lon=" +
                lon +
                "&unit_system=" +
                unitSystem +
                "&start_time=" +
                startDateNew +
                "&end_time=" +
                endDateNew +
                "&fields=" +
                fields +
                "&apikey=" +
                apiKey;

        try {
            DataPointRequestModel[] dataPointRequestModels = restTemplate.getForObject(apiUrlFull, DataPointRequestModel[].class);
            callLimitReached = false;
            if (dataPointRequestModels != null) {
                return Arrays.asList(dataPointRequestModels);
            }

        } catch (HttpClientErrorException e) {
            callLimitReached = true;
            callLimitReachedTimeStamp = LocalDateTime.now();
        }
        return null;
    }
}
