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

}
