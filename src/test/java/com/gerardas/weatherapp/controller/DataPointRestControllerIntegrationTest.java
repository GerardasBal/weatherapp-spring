package com.gerardas.weatherapp.controller;


import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.service.DataPointService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@WebMvcTest(DataPointController.class)
public class DataPointRestControllerIntegrationTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private DataPointService service;


    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray()
            throws Exception {
        LocalDateTime setTime = LocalDateTime.now();
        List<DataPointEntity> allDataPoints = new ArrayList<>();
        for (int i = 0; i<3;i++) {
            DataPointEntity dataPointEntity = new DataPointEntity();
            setTime = LocalDateTime.now();
            dataPointEntity.setObservationTimeValue(setTime);
            allDataPoints.add(dataPointEntity);
        }

        String year = String.valueOf(setTime.getYear());
        String month = String.valueOf(setTime.getMonthValue());
        String day = String.valueOf(setTime.getDayOfMonth());
        given(service.getPointsByDate(LocalDate.now())).willReturn(allDataPoints);

        mvc.perform(get("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .param("selectedYear", year)
                .param("selectedMonth", month)
                .param("selectedDay", day))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].observationTimeValue").exists());
    }


}
