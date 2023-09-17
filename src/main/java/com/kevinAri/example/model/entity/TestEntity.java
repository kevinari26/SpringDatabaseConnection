package com.kevinAri.example.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonIgnoreProperties
@Table(name = "person")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private Long id;
    @Column(name = "nama", nullable = false)
    private String nama;
    @Column(name = "nomor", nullable = false)
    private Integer nomor;
    @Column(name = "createdDate", nullable = false)
    private Timestamp createdDate;
}

