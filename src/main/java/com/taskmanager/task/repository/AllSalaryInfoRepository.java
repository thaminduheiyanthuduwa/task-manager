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
            value = "SELECT * FROM `all_salary_info` WHERE emp_id = :id")
    List<AllSalaryInfoEntity> getAllSalaryInfoByEmpId(@Param("id") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info` WHERE emp_id = :id and type in ('Budgetary Allowance', 'basic_salary')")
    List<AllSalaryInfoEntity> getBasicSalaryInfoByEmpId(@Param("id") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info` WHERE emp_id = :id and category in ('Basic Salary','Allowances')")
    List<AllSalaryInfoEntity> getGrossSalaryInfoByEmpId(@Param("id") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info` WHERE emp_id = :id and category in (:category)")
    List<AllSalaryInfoEntity> getAllSalaryInfoByEmpIdAndCategory(@Param("id") Integer id, @Param("category") String category);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `all_salary_info` WHERE emp_id = :id and category in (:category) and type in (:type)")
    List<AllSalaryInfoEntity> getAllSalaryInfoByEmpIdAndCategoryAndType(@Param("id") Integer id,
                                                                        @Param("category") String category,
                                                                        @Param("type") String type);

}
