package com.kevinAri.example.controller;

import com.kevinAri.example.model.entity.TestEntity;
import com.kevinAri.example.model.repository.TestRepo;
import com.kevinAri.example.util.CriteriaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@CrossOrigin
public class GraphQLController {
    // API GraphQL
    @Autowired
    private TestRepo repo;

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
}
