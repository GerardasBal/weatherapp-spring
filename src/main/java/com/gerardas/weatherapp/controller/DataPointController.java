package com.gerardas.weatherapp.controller;

import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.service.DataPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.*;
import java.util.*;

@RestController
public class DataPointController {

    private DataPointService dataPointService;

    @Autowired
    public void setDataPointService(DataPointService dataPointService) {
        this.dataPointService = dataPointService;
    }


    @GetMapping("/points/month")
    public ResponseEntity<List<DataPointEntity>> getAllPoints(@RequestParam String selectedYear, @RequestParam String selectedMonth) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(dataPointService.getDataPointsByMonth(selectedYear, selectedMonth));
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
