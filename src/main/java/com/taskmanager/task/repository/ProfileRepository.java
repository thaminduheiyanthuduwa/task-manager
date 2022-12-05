package com.taskmanager.task.repository;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {

//    Optional<EmpDetailEntity> findById(@Param("id") Integer id);

}
