package com.kevinAri.example.controller;

import com.kevinAri.example.model.entity.TestEntity;
import com.kevinAri.example.model.repository.TestRepo;
import com.kevinAri.example.service.AppService;
import com.kevinAri.example.util.CriteriaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@CrossOrigin
public class GraphQLController {
    // API GraphQL
    @Autowired
    private TestRepo repo;

    // payload example
    /*
    {
      persons {
        id,
        nama
      }
    }
    {
      personById(id:1) {
        id,
        nama
      }
    }
    {
      personsFilter(filterQuery:"nomor>900 AND id<=10 OR nama=henry v") {
        id,
        nama
      }
    }
    */
    @QueryMapping
    public List<TestEntity> persons () {
        return repo.findAll();
    }

    @QueryMapping
    public Object personById (@Argument Long id) {
        return repo.findById(id);
    }

    @QueryMapping
    public List<TestEntity> personsFilter (@Argument String filterQuery) {
        CriteriaParser<TestEntity> critParser = new CriteriaParser<>();
        critParser.setDateTimeArr(Collections.singletonList("createdDate"));
        Specification<TestEntity> specs = critParser.parseRecursively(filterQuery);
        return repo.findAll(specs);
    }


    @Autowired
    AppService appService;

    @GetMapping(value = "/test-transactional", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object hello() {
        try {
            appService.execute2();
            return ResponseEntity.status(HttpStatus.OK)
                    .body("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error");
        }
    }
}
