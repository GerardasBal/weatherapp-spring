package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.model.request.DataPointRequestModel;
import com.gerardas.weatherapp.service.DataPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherAPIService {


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


    @Scheduled(fixedRate = 1000 * 60 * 60)
    void updateDatabase() {
        LocalDateTime latestDateInDB = dataPointService.findLatestDate();

        LocalDateTime date = LocalDateTime.now().minusMinutes(90);
        if (callLimitReachedTimeStamp.isBefore(LocalDateTime.now().minusMinutes(30))) {
            callLimitReached = false;
        }
        System.out.println("Latest in DB before update: " + latestDateInDB);
        LocalDateTime latestDateInDBOld = latestDateInDB.minusHours(1);
        while (latestDateInDB.isBefore(date) && !callLimitReached && !latestDateInDB.isEqual(latestDateInDBOld)) {
            List<DataPointRequestModel> dataPointRequestModels = fetchDataFromWeatherAPI(latestDateInDB);
            if (dataPointRequestModels != null) {
                dataPointService.addDataPoints(
                        dataPointService.parseDataPoints(dataPointRequestModels));
                System.out.println("Updated!");
            }

            latestDateInDBOld = latestDateInDB;
            latestDateInDB = dataPointService.findLatestDate();
        }
        System.out.println("Latest dataPoint time in DB: " + latestDateInDB + " Limit reached:" + callLimitReached);
        System.out.println("Limit Reach time: " + callLimitReachedTimeStamp);
        System.out.println("Now: " + LocalDateTime.now());
        System.out.println("------------------------------------");
    }

    List<DataPointRequestModel> fetchDataFromWeatherAPI(LocalDateTime latestDateInDB) {
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
            System.out.println(e);
        }
        return null;
    }
}
