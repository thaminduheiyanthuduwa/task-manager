package com.taskmanager.task.repository;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.TaskBreakdownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskBreakdownRepository extends JpaRepository<TaskBreakdownEntity, Integer > {

    List<TaskBreakdownEntity> findAll();

    List<TaskBreakdownEntity> findByTaskId(@Param("taskId") Integer task);

    long deleteByTaskId(@Param("taskId") Integer task);

    List<TaskBreakdownEntity> findByUserId(@Param("userId") Integer user);

}
