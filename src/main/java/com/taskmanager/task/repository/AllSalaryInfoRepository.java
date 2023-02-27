package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AllSalaryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllSalaryInfoRepository extends JpaRepository<AllSalaryInfoEntity, Integer > {

    List<AllSalaryInfoEntity> findAll();

    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info` WHERE name = :id and type in ('Budgetary Allowance', 'basic_salary')")
    List<AllSalaryInfoEntity> getBasicSalaryInfoByName(@Param("id") String id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info` WHERE name = :id and category in ('Basic Salary','Allowances')")
    List<AllSalaryInfoEntity> getGrossSalaryInfoByName(@Param("id") String id);

}
