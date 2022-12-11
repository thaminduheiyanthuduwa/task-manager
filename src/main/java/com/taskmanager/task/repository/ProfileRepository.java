package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AboutEntity;
import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProfileRepository extends JpaRepository<EmpDetailEntity, Integer> {

//    Optional<EmpDetailEntity> findById(@Param("id") Integer id);

    List<EmpDetailEntity> findById(@Param("id") int id);


    @Query(nativeQuery = true,
            value = "SELECT name_in_full,date_of_birth,perm_address,email FROM people WHERE id = :id")
    List<EmpDetailEntity> getUserAboutDetails(@Param("id") int id);

}
