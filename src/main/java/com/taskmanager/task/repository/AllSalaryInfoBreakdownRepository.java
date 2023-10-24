package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AllSalaryInfoBreakdownEntity;
import com.taskmanager.task.entity.AllSalaryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllSalaryInfoBreakdownRepository extends JpaRepository<AllSalaryInfoBreakdownEntity, Integer > {

    List<AllSalaryInfoBreakdownEntity> findAll();

    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info_breakdown` WHERE emp_id = :id")
    List<AllSalaryInfoBreakdownEntity> getAllSalaryInfoByEmpId(@Param("id") Integer id);


    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info_breakdown` WHERE main_data_id = :id ORDER BY id DESC")
    List<AllSalaryInfoBreakdownEntity> getSalaryByMainTypeId(@Param("id") Integer id);


}
