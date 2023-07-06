package com.taskmanager.task.repository;

import com.taskmanager.task.entity.LeaveEntity;
import com.taskmanager.task.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveEntity, Integer> {

    List<LeaveEntity> findByEmpIdOrderByIdDesc(@Param("empId") Integer empId);

    @Query(nativeQuery = true,
            value = "select * from leave_manager where from_date >= :startDate")
    List<LeaveEntity> getLeaveByDate(@Param("startDate") String startDate);

    @Query(nativeQuery = true,
            value = "select count(id) from leave_manager where status = 1 and emp_id = :id")
    Integer getPendingRequestedCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "select count(id) from leave_manager where status = 2 and emp_id = :id")
    Integer getDeleteRequestedCount(@Param("id") int id);

}
