package com.gerardas.weatherapp.repositories;

import com.gerardas.weatherapp.entity.DataPointEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DataPointRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DataPointRepository dataPointRepository;


    @Test
    public void whenFindDataPointEntityByObservationTimeValueEquals_thenReturnDataPointEntity() {
        // given
        DataPointEntity dataPointEntity = new DataPointEntity();
        LocalDateTime dateTime = LocalDateTime.now();
        dataPointEntity.setObservationTimeValue(dateTime);
        entityManager.persist(dataPointEntity);
        entityManager.flush();

        // when
        DataPointEntity found = dataPointRepository.findDataPointEntityByObservationTimeValueEquals(dateTime);

        // then
        assertThat(found.getObservationTimeValue())
                .isEqualTo(dataPointEntity.getObservationTimeValue());
    }

    @Test
    public void whenFindByObservationTimeValueBetween_thenReturnListOfDataPointEntities() {
        // given
        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.now();
        int i;
        for (i = 0; i < 5; i++) {
            DataPointEntity dataPointEntity = new DataPointEntity();
            LocalDateTime dateTime = LocalDateTime.now().plusHours(i);
            dataPointEntity.setObservationTimeValue(dateTime);
            dataPointEntities.add(dataPointEntity);
        }
        LocalDateTime endDate = startDate.plusHours(i + 1);

        dataPointEntities.forEach(dataPointEntity -> {
            entityManager.persist(dataPointEntity);
            entityManager.flush();
        });

        // when
        List<DataPointEntity> found = dataPointRepository.findByObservationTimeValueBetween(startDate, endDate);

        // then
        assertThat(found)
                .isEqualTo(dataPointEntities);
    }


    @Test
    public void whenFindLatestDate_thenReturnLocalDateTime() {
        // given
        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        LocalDateTime latestDate = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            DataPointEntity dataPointEntity = new DataPointEntity();
            LocalDateTime dateTime = LocalDateTime.now().plusHours(i);
            dataPointEntity.setObservationTimeValue(dateTime);
            dataPointEntities.add(dataPointEntity);
            latestDate = dateTime;
        }

        dataPointEntities.forEach(dataPointEntity -> {
            entityManager.persist(dataPointEntity);
            entityManager.flush();
        });

        // when
        LocalDateTime found = dataPointRepository.findLatestDate();

        // then
        assertThat(found)
                .isEqualTo(latestDate);
    }


    @Test
    public void whenFindObservationTimeMin_thenReturnLocalDateTime() {
        // given
        List<DataPointEntity> dataPointEntities = new ArrayList<>();
        LocalDateTime searchDate = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            DataPointEntity dataPointEntity = new DataPointEntity();
            LocalDateTime dateTime = LocalDateTime.now().plusHours(i);
            dataPointEntity.setObservationTimeValue(dateTime);
            dataPointEntities.add(dataPointEntity);
            if (i == 0) searchDate = dateTime;
        }

        dataPointEntities.forEach(dataPointEntity -> {
            entityManager.persist(dataPointEntity);
            entityManager.flush();
        });

        // when
        LocalDateTime found = dataPointRepository.findObservationTimeMin();

        // then
        assertThat(found)
                .isEqualTo(searchDate);
    }

    @Test
    public void whenGetCountFromNotEmptyDB_thenReturnInteger() {
        // given
        List<DataPointEntity> dataPointEntities = new ArrayList<>();

        int i;
        for (i = 0; i < 5; i++) {
            DataPointEntity dataPointEntity = new DataPointEntity();
            LocalDateTime dateTime = LocalDateTime.now().plusHours(i);
            dataPointEntity.setObservationTimeValue(dateTime);
            dataPointEntities.add(dataPointEntity);
        }

        dataPointEntities.forEach(dataPointEntity -> {
            entityManager.persist(dataPointEntity);
            entityManager.flush();
        });

        // when
        Integer found = dataPointRepository.getCount();

        // then
        assertThat(found)
                .isEqualTo(i);
    }


}
