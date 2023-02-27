package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Integer > {
    List<AttendanceEntity> findAll();

    List<AttendanceEntity> findByEmpId(@Param("empId") Integer id);

    @Query(nativeQuery = true,
            value = "select count(id) from attendance where status = 2 and emp_id = :id")
    Integer getPendingRequestedCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "select * from attendance WHERE date >= '2023-02-01'  and date < '2023-03-01';;")
    List<AttendanceEntity> getPastMonthAttendance();

    @Query(nativeQuery = true,
            value = "select * from attendance WHERE date = :date_val  and emp_id = :emp")
    List<AttendanceEntity> getAttendanceByDateAndEmpID(@Param("date_val") String date_val,
                                                       @Param("emp") int emp);

}
