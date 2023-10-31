package com.taskmanager.task.repository;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.PayrollEntityDetails;
import com.taskmanager.task.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpDetailRepository extends JpaRepository<EmpDetailEntity, Integer > {

    List<EmpDetailEntity> findAll();

    List<EmpDetailEntity> findByEmailAndNicNo(@Param("email") String email,
                                                  @Param("nicNo") String nicNo);

    List<EmpDetailEntity> findBySupervisor(@Param("supervisor") int supervisor);

    List<EmpDetailEntity> findByNameInFull(@Param("nameInFull") String nameInFull);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `people` WHERE id in (select emp_id from all_salary_info)")
    List<EmpDetailEntity> getEmpDetailListById();


}







