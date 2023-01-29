package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AttendanceEntity;
import com.taskmanager.task.entity.PenaltyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaltyRepository extends JpaRepository<PenaltyEntity, Integer > {


    List<PenaltyEntity> findByEmpId(@Param("empId") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT sum(penalty_amount) FROM `penalty` group by emp_id having emp_id = :id")
    Integer getAllPenaltyValue(@Param("id") int id);


}
