package com.gerardas.weatherapp.service;

import com.gerardas.weatherapp.entity.DataPointEntity;
import com.gerardas.weatherapp.repositories.DataPointRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DataPointServiceImplIntegrationTest {

    @TestConfiguration
    static class DataPointServiceImplTestContextConfiguration {

        @Bean
        public DataPointService dataPointService() {
            return new DataPointServiceImpl();
        }
    }

    @Autowired
    private DataPointService dataPointService;

    @MockBean
    private DataPointRepository dataPointRepository;


    @Before
    public void setUp() {

        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        LocalDate date = LocalDate.now();
        LocalDateTime initialDate = LocalDateTime.of(date,LocalTime.of(15,10));

        for (int i = 0; i<5;i++){
                DataPointEntity dataPointEntity = new DataPointEntity();
                dataPointEntity.setObservationTimeValue(initialDate.plusHours(i));
                dataPointEntities.add(dataPointEntity);
        }

        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.MAX);

        Mockito.when(dataPointRepository.findByObservationTimeValueBetween(startDateTime, endDateTime))
                .thenReturn(dataPointEntities);


        LocalDateTime startDate =
                LocalDateTime.of(
                        LocalDate.of(date.getYear(),
                                date.getMonth(),
                                1),
                        LocalTime.MIN);

        LocalDateTime endDate =
                LocalDateTime.of(
                        LocalDate.of(
                                date.getYear(),
                                date.getMonth(),
                                date.getMonth().maxLength()),
                        LocalTime.MAX);

        Mockito.when(dataPointRepository.findByObservationTimeValueBetween(startDate, endDate))
                .thenReturn(dataPointEntities);
    }


    @Test
    public void whenGetPointsByDateIsValid_thenLengthOfListOfDataPointsShouldBeFive() {
        LocalDate findDate = LocalDate.now();
        List<DataPointEntity> found = dataPointService.getPointsByDate(findDate);

        assertThat(found.size())
                .isEqualTo(5);
    }

    @Test
    public void whenGetPointsByDateDateIsNotValid_thenLengthOfListOfDataPointsShouldBeZero() {
        LocalDate findDate = LocalDate.now().plusDays(1);
        List<DataPointEntity> found = dataPointService.getPointsByDate(findDate);

        assertThat(found.size())
                .isEqualTo(0);
    }


    @Test
    public void whenGetDataPointsByMonth_thenLengthOfListOfDataPointsShouldBeFive() {
        List<DataPointEntity> found =
                dataPointService
                        .getDataPointsByMonth(
                                String.valueOf(LocalDate.now().getYear()),
                                String.valueOf(LocalDate.now().getMonthValue()));
        assertThat(found.size())
                .isEqualTo(5);
    }


}
