package com.kevinAri.example.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinAri.example.model.entity.TestEntity;
import com.kevinAri.example.model.repository.TestRepo;
import com.kevinAri.example.util.CriteriaParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;


@Service
public class AppService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TestRepo testRepo;
    @Autowired
    JdbcTemplate jdbcTemplate; // set datasource in properties file

    @Setter
    @Getter
    static class TempDto {
        private Long id;
        private String nama;
        private Integer nomor;
        @JsonProperty("created_date")
        private Timestamp createdDate;
    }
    public void execute() {
        try {
            usingHibernate();
//            usingJdbcTemplate();
//            usingHibernateSpecs();

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void usingHibernate() throws JsonProcessingException {
        Set<String> setName = new HashSet<>();
        setName.add("kevin");
        setName.add("w");
        setName.add("w");
        // execute query using hibernate
        System.out.println(objectMapper.writeValueAsString(testRepo.findAll()));
        System.out.println(objectMapper.writeValueAsString(testRepo.findByNama("w")));
        System.out.println(objectMapper.writeValueAsString(testRepo.findByNamaIn(setName)));
        System.out.println(objectMapper.writeValueAsString(testRepo.findByTableIdJpql(1L)));
        System.out.println(objectMapper.writeValueAsString(testRepo.findByTableIdInJpql(Arrays.asList(1L, 2L))));
        System.out.println(objectMapper.writeValueAsString(testRepo.findByTableIdNative(1L)));
        // call stored procedure using hibernate
        System.out.println(testRepo.concatNumberAndString(1, " randomString"));
    }
    private void usingJdbcTemplate() throws JsonProcessingException {
        // execute query using jdbcTemplate
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM person")));
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM person WHERE nama='w'")));
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM person WHERE id<=10 or nama=\"henry v\" and nomor>900")));
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM person WHERE (id<=10 or nama=\"henry v\") and nomor>900")));
        // call view using jdbcTemplate
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM view_person")));

        // convert jdbc return to dto
        List<TempDto> tempDtoList = new ArrayList<>();
        List<Map<String, Object>> listFromJdbc = jdbcTemplate.queryForList("SELECT * FROM tabel_test");
        for (Map<String, Object> data : listFromJdbc) {
            TempDto temp = objectMapper.convertValue(data, TempDto.class);
            tempDtoList.add(temp);
        }
        System.out.println(objectMapper.writeValueAsString(tempDtoList));
    }
    private void usingHibernateSpecs() {
        // standard JPA specification
        Specification<TestEntity> specs = (r, q, b) -> b.lessThanOrEqualTo(r.get("id"), "10");
        List<TestEntity> listEnt = testRepo.findAll(specs);

        // advanced custom JPA specification
        CriteriaParser<TestEntity> criteriaParser = new CriteriaParser<>();
        criteriaParser.setDateTimeArr(Arrays.asList("createdDate"));
        Specification<TestEntity> specs1 = criteriaParser.parse("nomor>900 AND id<=10 OR nama=henry v");
        List<TestEntity> listEnt1 = testRepo.findAll(specs1);
        Specification<TestEntity> specs2 = criteriaParser.parseRecursively("nomor>900 AND (id<=10 OR nama=henry v)");
        List<TestEntity> listEnt2 = testRepo.findAll(specs2);
        Specification<TestEntity> specs3 = criteriaParser.parseRecursively("createdDate>=2022-12-14 15:33:42");
        List<TestEntity> listEnt3 = testRepo.findAll(specs3);
        Specification<TestEntity> specs4 = criteriaParser.parseRecursively("id<=10 OR nama='henry v' AND nomor>900");
        List<TestEntity> listEnt4 = testRepo.findAll(specs4);
    }

    // testing transactional
    @Transactional
    public void execute2() {
        try {
            TestEntity testEntity = new TestEntity();
            testEntity.setNomor(123);
            testEntity.setNama("123");
            testEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            TestEntity testEntity2 = new TestEntity();
            testEntity2.setNama("123");
            testEntity2.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            testRepo.save(testEntity);
            testRepo.save(testEntity2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
