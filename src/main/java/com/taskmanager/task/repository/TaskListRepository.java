package com.taskmanager.task.repository;

import com.taskmanager.task.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepository extends JpaRepository<TaskListEntity, Integer > {

    List<TaskListEntity> findAll();

    List<TaskListEntity> findByUserIdOrderByIdDesc(@Param("userId") Integer userId);

    @Query(nativeQuery = true,
            value = "select count(id) from task_list where status = 2 and user_id = :id")
    Integer getDeleteRequestedCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "select count(id) from task_list where status = 1 and user_id = :id")
    Integer getPendingCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "select count(id) from task_list where status = 3 and user_id = :id")
    Integer getReviewNeededCount(@Param("id") int id);




}
