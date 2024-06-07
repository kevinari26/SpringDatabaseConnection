package com.kevinAri.example.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonIgnoreProperties
@Table(name = "table_test_2")
// turn off select before insert or update
public class TableTest2Entity implements Persistable<String> {
    @Transient
    private boolean isNew = true;
    @Override
    public boolean isNew() {
        return isNew;
    }
    @Override
    public String getId() {
        return tableTest2Id;
    }

    @Id
    @Column(name="table_test_2_id")
    private String tableTest2Id;
    private String name;
    private Integer number;
    private Date createdDate;
}

