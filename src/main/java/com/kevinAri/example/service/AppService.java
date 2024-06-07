package com.kevinAri.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinAri.example.model.entity.TableTestEntity;
import com.kevinAri.example.model.repository.TableTestRepo;
import com.kevinAri.example.util.CommonUtil;
import com.kevinAri.example.util.CriteriaParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
public class AppService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TableTestRepo tableTestRepo;
    @Autowired
    JdbcTemplate jdbcTemplate; // set datasource in properties file

    @Setter
    @Getter
    static class TableTestDto {
        private String tableTestUuid;
        private String name;
        private Integer number;
        private Date createdDate;
    }
    public void execute() {
        try {
//            populateTableFromCsv();

//            usingHibernate();
//            usingJdbcTemplate();
//            usingHibernateSpecs();

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void populateTableFromCsv() throws Exception {
        List<TableTestEntity> listTableTestEntity = new LinkedList<>();

        Path path = Paths.get("src/main/resources/tableFiles/", "table_test.csv");
        BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(";");
            TableTestEntity tableTestEntity = new TableTestEntity();
            tableTestEntity.setTableTestId(values[0]);
            tableTestEntity.setName(values[1]);
            tableTestEntity.setNumber(Integer.valueOf(values[2]));
            tableTestEntity.setCreatedDate(CommonUtil.parseStringDateToDate(values[3], "dd/MM/yyyy HH:mm"));
            listTableTestEntity.add(tableTestEntity);
        }

        // save
        tableTestRepo.saveAll(listTableTestEntity);
    }


    private void usingHibernate() throws Exception {
        Set<String> setName = new HashSet<>();
        setName.add("kevin");
        setName.add("w");
        setName.add("w");
        // execute query using hibernate
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findAll()));
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findByName("w")));
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findByNameIn(setName)));
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findByTableTestIdJpql("1")));
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findByTableTestIdInJpql(Arrays.asList("1", "2"))));
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findByTableTestIdNative("1")));
        // call stored procedure using hibernate
        System.out.println(tableTestRepo.concatNumberAndString(1, " randomString"));
        // test sort and pageable
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findByNumberGreaterThan(100,
                Sort.by(Sort.Direction.ASC, "createdDate", "name"))));
        System.out.println(objectMapper.writeValueAsString(tableTestRepo.findByNumberGreaterThan(100,
                PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "createdDate", "name")))));
    }
    private void usingJdbcTemplate() throws Exception {
        // execute query using jdbcTemplate
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM table_test")));
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM table_test WHERE name='w'")));
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM table_test WHERE table_test_id<=10 or name=\"henry v\" and number>900")));
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM table_test WHERE (table_test_id<=10 or name=\"henry v\") and number>900")));
        // call view using jdbcTemplate
        System.out.println(objectMapper.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM view_table_test")));

        // convert jdbc return to dto
        List<TableTestDto> tableTestDtoList = new ArrayList<>();
        List<Map<String, Object>> listFromJdbc = jdbcTemplate.queryForList("SELECT * FROM table_test");
        for (Map<String, Object> data : listFromJdbc) {
            TableTestDto temp = objectMapper.convertValue(data, TableTestDto.class);
            tableTestDtoList.add(temp);
        }
        System.out.println(objectMapper.writeValueAsString(tableTestDtoList));
    }
    private void usingHibernateSpecs() {
        // standard JPA specification
        Specification<TableTestEntity> specs = (r, q, b) -> b.lessThanOrEqualTo(r.get("tableTestId"), "10");
        List<TableTestEntity> listEnt = tableTestRepo.findAll(specs);

        // advanced custom JPA specification
        CriteriaParser<TableTestEntity> criteriaParser = new CriteriaParser<>();
        criteriaParser.setDateTimeArr(Arrays.asList("createdDate"));
        Specification<TableTestEntity> specs1 = criteriaParser.parse("number>900 AND tableTestId<=10 OR name=henry v");
        List<TableTestEntity> listEnt1 = tableTestRepo.findAll(specs1);
        Specification<TableTestEntity> specs2 = criteriaParser.parseRecursively("number>900 AND (tableTestId<=10 OR name=henry v)");
        List<TableTestEntity> listEnt2 = tableTestRepo.findAll(specs2);
        Specification<TableTestEntity> specs3 = criteriaParser.parseRecursively("createdDate>=2022-12-14 15:33:42");
        List<TableTestEntity> listEnt3 = tableTestRepo.findAll(specs3);
        Specification<TableTestEntity> specs4 = criteriaParser.parseRecursively("tableTestId<=10 OR name='henry v' AND number>900");
        List<TableTestEntity> listEnt4 = tableTestRepo.findAll(specs4);
    }

}
