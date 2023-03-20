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
            value = "select * from attendance WHERE emp_id = :empId and date >= :startDate and date < :endDate")
    List<AttendanceEntity> findByEmpIdAndDateRange(@Param("empId") Integer id,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    @Query(nativeQuery = true,
            value = "select * from attendance WHERE date >= :startDate and date < :endDate")
    List<AttendanceEntity> findByDateRange(@Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    @Query(nativeQuery = true,
            value = "select count(id) from attendance where status = 2 and emp_id = :id")
    Integer getPendingRequestedCount(@Param("id") int id);

    @Query(nativeQuery = true,
            value = "select * from attendance WHERE date >= '2023-03-01'  and date < '2023-04-01';")
    List<AttendanceEntity> getPastMonthAttendance();

    @Query(nativeQuery = true,
            value = "select * from attendance WHERE date = :date_val  and emp_id = :emp")
    List<AttendanceEntity> getAttendanceByDateAndEmpID(@Param("date_val") String date_val,
                                                       @Param("emp") int emp);

    @Query(nativeQuery = true,
            value = "SELECT distinct DATE_FORMAT(date, '%Y-%m') as dates FROM attendance;")
    List<String> getAttendanceDateRange();


    @Query(nativeQuery = true,
            value = "select (CASE WHEN ot > 0 and late > 0 THEN 3 \n" +
                    "        WHEN ot > 0 and late < 1 THEN 2\n" +
                    "        WHEN ot < 1 and late > 0 THEN 1 ELSE 0 END) as status_sum from (SELECT id, sum(apply_ot) as ot, sum(apply_late) as late  FROM attendance WHERE emp_id = :emp_id) obj")
    Integer getAttendanceOTAndLateById(@Param("emp_id") Integer emp_id);



}
