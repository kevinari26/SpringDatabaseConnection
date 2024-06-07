package com.kevinAri.example.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonIgnoreProperties
@Table(name = "table_test")
public class TableTestEntity {
    @Id
    private String tableTestId;
    private String name;
    private Integer number;
    private Date createdDate;
}

