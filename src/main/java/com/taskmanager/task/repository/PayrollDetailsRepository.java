package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AllSalaryInfoEntity;
import com.taskmanager.task.entity.PayrollEntityDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollDetailsRepository extends JpaRepository<PayrollEntityDetails, Integer > {

    List<PayrollEntityDetails> findAll();

    List<PayrollEntityDetails> findByEmpId(@Param("empId") Integer empId);

}
