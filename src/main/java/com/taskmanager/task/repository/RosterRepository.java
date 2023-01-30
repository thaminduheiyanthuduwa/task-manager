package com.taskmanager.task.repository;

import com.taskmanager.task.entity.RosterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RosterRepository extends JpaRepository<RosterEntity, Integer > {

    List<RosterEntity> findAll();

    List<RosterEntity> findByUserOrderByIdDesc(@Param("user") Integer id);

    @Query(nativeQuery = true,
            value = "select count(id) from my_roster where status = 1 and user = :id")
    Integer getPendingCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "select count(id) from my_roster where status = 3 and user = :id")
    Integer getChangeRequestedCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "select count(id) from my_roster where status in (3,2,4,8) and user = :id")
    Integer getReviewNeededCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "SELECT count(*) FROM `my_roster` WHERE status not in (2,4,5,7, 8) and user = :id")
    Integer getAvailableRosterCount(@Param("id") int id);
}
