package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AllSalaryInfoEntity;
import com.taskmanager.task.entity.SalaryInfoMasterCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryInfoMasterCategoryRepository extends JpaRepository<SalaryInfoMasterCategory, Integer > {

    @Query(nativeQuery = true,
            value = "SELECT * FROM `salary_info_master_category` WHERE category = :category")
    List<SalaryInfoMasterCategory> getSalaryMasterTypeByCategory(@Param("category") String category);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `salary_info_master_category` WHERE type = :type")
    List<SalaryInfoMasterCategory> getSalaryMasterTypeByType(@Param("type") String type);

}
