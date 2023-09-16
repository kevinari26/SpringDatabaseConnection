package com.kevinAri.example.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinAri.example.model.repository.TestRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
            // execute query using hibernate
            System.out.println(objectMapper.writeValueAsString(testRepo.findAll()));
            System.out.println(objectMapper.writeValueAsString(testRepo.findByNama("w")));
            System.out.println(objectMapper.writeValueAsString(testRepo.findByTableIdJpql(1L)));
            System.out.println(objectMapper.writeValueAsString(testRepo.findByTableIdNative(1L)));
            // execute query using jdbcTemplate
            System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM tabel_test")));
            System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM tabel_test WHERE nama='w'")));
            // call view using jdbcTemplate
            System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM view_test")));

            // convert jdbc return to dto
            List<TempDto> tempDtoList = new ArrayList<>();
            List<Map<String, Object>> listFromJdbc = jdbcTemplate.queryForList("SELECT * FROM tabel_test");
            for (Map<String, Object> data : listFromJdbc) {
                TempDto temp = objectMapper.convertValue(data, TempDto.class);
                tempDtoList.add(temp);
            }

            // call stored procedure using hibernate
            System.out.println(testRepo.concatNumberAndString(1, " randomString"));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
