package com.taskmanager.task.repository;

import com.taskmanager.task.entity.ProfilePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilePostRepository extends JpaRepository<ProfilePost, Integer > {

    List<ProfilePost> findAll();

}
