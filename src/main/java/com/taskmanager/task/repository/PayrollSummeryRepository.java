package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AttendanceEntity;
import com.taskmanager.task.entity.PayrollSummery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollSummeryRepository extends JpaRepository<PayrollSummery, Integer > {



}
