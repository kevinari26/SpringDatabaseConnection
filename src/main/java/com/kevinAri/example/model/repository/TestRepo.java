package com.kevinAri.example.model.repository;

import com.kevinAri.example.model.entity.TestEntity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Configurable
public interface TestRepo extends JpaRepository<TestEntity, Long> {
    List<TestEntity> findByNama(String nama);
}
