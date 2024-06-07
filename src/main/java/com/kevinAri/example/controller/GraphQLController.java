package com.kevinAri.example.controller;

import com.kevinAri.example.model.entity.TableTestEntity;
import com.kevinAri.example.model.repository.TableTestRepo;
import com.kevinAri.example.service.AppService;
import com.kevinAri.example.service.GraphQLService;
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
    private TableTestRepo repo;

    // payload example
    /*
    {
      listTableTest {
        tableTestId,
        name
      }
    }
    {
      tableTestById(id:2){
        tableTestId,
        name
      }
    }
    {
      tableTestFilter(filterQuery:"number>900 AND tableTestId<=10 OR name=henry v") {
        tableTestId,
        name,
        number
      }
    }
    */
    @QueryMapping(name = "listTableTest")
    public List<TableTestEntity> listTableTest () {
        return repo.findAll();
    }

    @QueryMapping(name = "tableTestById")
    public Object tableTestById (@Argument String id) {
        return repo.findById(id);
    }

    @QueryMapping(name = "tableTestFilter")
    public List<TableTestEntity> tableTestFilter (@Argument String filterQuery) {
        CriteriaParser<TableTestEntity> critParser = new CriteriaParser<>();
        critParser.setDateTimeArr(Collections.singletonList("createdDate"));
        Specification<TableTestEntity> specs = critParser.parseRecursively(filterQuery);
        return repo.findAll(specs);
    }


    @Autowired
    GraphQLService graphQLService;

    @GetMapping(value = "/test-transactional", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object hello() {
        try {
            graphQLService.testTransactional();
            return ResponseEntity.status(HttpStatus.OK)
                    .body("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error");
        }
    }
}
