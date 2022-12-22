package com.taskmanager.task.repository;

import com.taskmanager.task.entity.NewFeatureAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureAlertRepository extends JpaRepository<NewFeatureAlertEntity, Integer > {

    List<NewFeatureAlertEntity> findByEmpId(@Param("empId") int id);

}
