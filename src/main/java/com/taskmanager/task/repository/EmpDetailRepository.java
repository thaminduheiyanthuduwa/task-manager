package com.taskmanager.task.repository;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpDetailRepository extends JpaRepository<EmpDetailEntity, Integer > {

    List<EmpDetailEntity> findAll();



    List<EmpDetailEntity> findByEmailAndNicNo(@Param("email") String email,
                                                  @Param("nicNo") String nicNo);

}
