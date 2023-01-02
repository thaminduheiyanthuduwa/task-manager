package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Integer > {
    List<AttendanceEntity> findAll();

    List<AttendanceEntity> findByEmpId(@Param("empId") Integer id);

}
