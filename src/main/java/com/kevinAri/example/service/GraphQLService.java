package com.kevinAri.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinAri.example.model.entity.TableTestEntity;
import com.kevinAri.example.model.repository.TableTestRepo;
import com.kevinAri.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GraphQLService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TableTestRepo tableTestRepo;


    // testing transactional
    @Transactional
    public void testTransactional() {
        try {
            TableTestEntity tableTestEntity = new TableTestEntity();
            tableTestEntity.setNumber(123);
            tableTestEntity.setName("123");
            tableTestEntity.setCreatedDate(CommonUtil.currentTime());

            TableTestEntity tableTestEntity2 = new TableTestEntity();
            tableTestEntity2.setName("123");
            tableTestEntity2.setCreatedDate(CommonUtil.currentTime());

            tableTestRepo.save(tableTestEntity);
            tableTestRepo.save(tableTestEntity2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
