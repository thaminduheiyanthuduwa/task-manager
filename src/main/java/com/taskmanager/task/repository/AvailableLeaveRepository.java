package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AvailableLeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableLeaveRepository extends JpaRepository<AvailableLeaveEntity, Integer > {

    List<AvailableLeaveEntity> findAll();

    List<AvailableLeaveEntity> findByEmpId(@Param("empId") Integer id);

}
