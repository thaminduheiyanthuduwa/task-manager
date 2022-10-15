package com.taskmanager.task.repository;

import com.taskmanager.task.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends JpaRepository<TaskListEntity, Integer > {

    List<TaskListEntity> findAll();

    List<TaskListEntity> findByUserIdOrderByIdDesc(@Param("userId") Integer userId);


}
