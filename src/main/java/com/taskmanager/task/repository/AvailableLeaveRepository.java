package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AvailableLeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableLeaveRepository extends JpaRepository<AvailableLeaveEntity, Integer > {

    List<AvailableLeaveEntity> findAll();

    List<AvailableLeaveEntity> findByEmpId(@Param("empId") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT available_leaves FROM available_leaves WHERE type = 'Day Off - Mar' and emp_id = :id")
    Integer getAvailableOffDayLeave(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "SELECT sum(original_leaves) FROM available_leaves group by emp_id having emp_id = :id")
    Integer getAllLeaves(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "SELECT sum(available_leaves) FROM available_leaves group by emp_id having emp_id = :id")
    Integer getAvailable(@Param("id") int id);

}
