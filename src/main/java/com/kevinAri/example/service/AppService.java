package com.kevinAri.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinAri.example.model.repository.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class AppService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TestRepo testRepo;
    @Autowired
    JdbcTemplate jdbcTemplate; // set datasource di file properties

    public void execute() {
        try {
            // using hibernate
            System.out.println(objectMapper.writeValueAsString(testRepo.findAll()));
            System.out.println(objectMapper.writeValueAsString(testRepo.findByNama("w")));
            // using jdbcTemplate
            System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM TABEL_TEST")));
            System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM tabel_test WHERE nama='w'")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
