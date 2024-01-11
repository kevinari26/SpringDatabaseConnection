package com.kevinAri.example.model.repository;

import com.kevinAri.example.model.entity.TestEntity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
@Configurable
public interface TestRepo extends JpaRepository<TestEntity, Long>, JpaSpecificationExecutor<TestEntity> {
    List<TestEntity> findByNama(String nama);
    List<TestEntity> findByNamaIn(Set<String> setNama);

    // JPQL
    @Query("SELECT e FROM TestEntity e WHERE e.id = ?1")
    TestEntity findByTableIdJpql(Long id);
//    @Query("SELECT e FROM TestEntity e WHERE e.id in (?1)")
    @Query("SELECT e FROM TestEntity e WHERE e.id in (:id)")
    List<TestEntity> findByTableIdInJpql(List<Long> id);
    // Native
    @Query(value = "SELECT * FROM tabel_test t WHERE t.id = ?1", nativeQuery = true)
    TestEntity findByTableIdNative(Long id);

    // stored procedure
    @Procedure(name = "concatNumberAndString")
    String concatNumberAndString(Integer number, String string);
}
