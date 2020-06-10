package com.gerardas.weatherapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.model.request.DataPointRequestModel;
import com.gerardas.weatherapp.service.DataPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.util.*;

@RestController
public class DataPointController {


    private DataPointService dataPointService;

    private Boolean firstRun = true;


    @Autowired
    public void setDataPointService(DataPointService dataPointService) {
        this.dataPointService = dataPointService;
    }




    @GetMapping("/load")
    @Profile("test")
    public List<DataPointEntity> loadInitialDataFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new ClassPathResource("historydata.json").getFile();
            DataPointRequestModel[] dataPoint = objectMapper.readValue(file, DataPointRequestModel[].class);

            List<DataPointEntity> dataPointEntities = new ArrayList<>();
            for (DataPointRequestModel dataModel : dataPoint
            ) {

                DataPointEntity dataPointEntity = dataPointService.parseDataPoint(dataModel);
                dataPointEntities.add(dataPointEntity);
                dataPointService.addDataPoint(dataPointEntity);

            }
            return dataPointService.getAllDataPoints();
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }


    @GetMapping("/points/all")
    public ResponseEntity<List<DataPointEntity>> getAllPoints() {
        if (firstRun && dataPointService.getAllDataPoints().isEmpty()) {
            this.loadInitialDataFromFile();
            firstRun = false;
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(dataPointService.getAllDataPoints());
    }


    @GetMapping("/points")
    public ResponseEntity<List<DataPointEntity>> getPointsByDate(@RequestParam String selectedYear, @RequestParam String selectedMonth, @RequestParam String selectedDay) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        LocalDate selectedDate = LocalDate.of(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), Integer.parseInt(selectedDay));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(dataPointService.getPointsByDate(selectedDate));
    }

    @GetMapping("/points/dates")
    public ResponseEntity<LocalDateTime[]> getOldestAndLatestDates(){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(dataPointService.getOldestAndLatestDates());
    }


}
