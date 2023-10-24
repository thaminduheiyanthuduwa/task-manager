package com.taskmanager.task.repository;

import com.taskmanager.task.entity.MasterPayrollSettings;
import com.taskmanager.task.entity.SalaryInfoMasterCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollSettingRepository extends JpaRepository<MasterPayrollSettings, Integer > {

    @Query(nativeQuery = true,
            value = "SELECT ps.id, ps.setting_value, mps.setting_name, ps.created_date, ps.created_by " +
                    "FROM payroll_setting ps LEFT JOIN master_payroll_setting mps " +
                    "ON ps.setting_id = mps.setting_id WHERE ps.emp_id = :id ;")
    List<Object> getPayrollSettingByEmpId(@Param("id") Integer id);


}
