package com.kevinAri.example.model.repository;

import com.kevinAri.example.model.entity.TableTestEntity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
@Configurable
public interface TableTestRepo extends JpaRepository<TableTestEntity, String>, JpaSpecificationExecutor<TableTestEntity> {
    List<TableTestEntity> findByName(String name);
    List<TableTestEntity> findByNameIn(Set<String> setName);
    List<TableTestEntity> findByNumberGreaterThan(Integer numberGreaterThan, Sort sort);
    List<TableTestEntity> findByNumberGreaterThan(Integer numberGreaterThan, Pageable pageable);

    // JPQL
    @Query("SELECT e FROM TableTestEntity e WHERE e.tableTestId = ?1")
    TableTestEntity findByTableTestIdJpql(String tableTestId);
//    @Query("SELECT e FROM TableTestEntity e WHERE e.tableTestId in (?1)")
    @Query("SELECT e FROM TableTestEntity e WHERE e.tableTestId in (:listTableTestId)")
    List<TableTestEntity> findByTableTestIdInJpql(List<String> listTableTestId);
    // Native
    @Query(value = "SELECT * FROM table_test t WHERE t.table_test_id = ?1", nativeQuery = true)
    TableTestEntity findByTableTestIdNative(String tableTestId);

    // stored procedure
    @Procedure(name = "concatNumberAndString")
    String concatNumberAndString(Integer number, String string);
}
