package com.taskmanager.task.repository;

import com.taskmanager.task.entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<PerformanceEntity, Integer > {

    List<PerformanceEntity> findAll();

    List<PerformanceEntity> findByUserId(@Param("userId") Integer id);



}
