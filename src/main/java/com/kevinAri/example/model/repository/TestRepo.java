package com.kevinAri.example.model.repository;

import com.kevinAri.example.model.entity.TestEntity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Configurable
public interface TestRepo extends JpaRepository<TestEntity, Long> {
    List<TestEntity> findByNama(String nama);

    // JPQL
    @Query("SELECT e FROM TestEntity e WHERE e.id = ?1")
    TestEntity findByTableIdJpql(Long id);
    // Native
    @Query(value = "SELECT * FROM tabel_test t WHERE t.id = ?1", nativeQuery = true)
    TestEntity findByTableIdNative(Long id);

    // stored procedure
    @Procedure(name = "concatNumberAndString")
    String concatNumberAndString(Integer number, String string);
}
