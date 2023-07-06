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
            value = "select * from attendance WHERE date >= '2023-05-01'  and date < '2023-06-01';")
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


    @Query(nativeQuery = true,
            value = "select (IFNULL(obj1.total_count,0) + IFNULL(obj2.total_available_leaves,0)) as count    from people pe \n" +
                    "left join (SELECT lm.emp_id, sum(lm.total_leave) as total_count FROM leave_manager lm WHERE lm.status = 5 AND lm.from_date >= '2023-05-01' AND lm.to_date < '2023-06-01' and lm.leave_type not in ('Special Company Holiday - May','Day Off - May')  GROUP BY lm.emp_id) obj1\n" +
                    "on obj1.emp_id = pe.id\n" +
                    "left join (SELECT al.emp_id, SUM(al.original_leaves) as total_available_leaves FROM available_leaves al WHERE al.type like '%may%' GROUP BY al.emp_id) obj2 on obj2.emp_id = pe.id\n" +
                    "where pe.id = :emp_id")
    float getMonthLeaveDatesForPayRoll(@Param("emp_id") Integer emp_id);

    @Query(nativeQuery = true,
            value = "SELECT IFNULL(SUM(IFNULL(tl.estimate,0)),0) as count FROM task_list tl WHERE tl.status = 5 AND tl.start_date >= '2023-05-01' AND tl.start_date < '2023-06-01' AND tl.user_id = :emp_id GROUP BY tl.user_id;")
    Integer getMonthEstimation(@Param("emp_id") Integer emp_id);

    @Query(nativeQuery = true,
            value = "select id,name_in_full,attendance_date,IFNULL(in_time,''), IFNULL(out_time,''),\n" +
                    "IFNULL(working_hour_in_min,''), IFNULL(working_hour_in_hour,''),\n" +
                    "IFNULL(minor_staff_type,'')\n" +
                    "from(select at.id, pe.name_in_full, IFNULL(at.date,'') as attendance_date, " +
                    "CASE WHEN (at.in_time = '' or at.in_time is null) and (at.out_time = '' or " +
                    "at.out_time is null) THEN 'Leave'ELSE at.in_time END AS in_time, " +
                    "CASE WHEN (at.in_time = '' or at.in_time is null) and (at.out_time = '' " +
                    "or at.out_time is null) THEN 'Leave'ELSE at.out_time END AS out_time, " +
                    "at.total_working_hours/60 as working_hour_in_min , at.total_working_hours/(60*60) " +
                    "as working_hour_in_hour, pe.minor_staff_type from attendance at left join " +
                    "people pe on at.emp_id = pe.id where pe.person_type = 'minor' and " +
                    "at.date >= :start_date and at.date <= :end_date order by pe.name_in_full, at.date) obj;")
    List<Object> getMinoStaffAttendanceByDate(@Param("start_date") String start_date, @Param("end_date") String end_date);

    @Query(nativeQuery = true,
            value = "select id,name_in_full, count(id) as leave_count\n" +
                    "from(select pe.id, pe.name_in_full, IFNULL(at.date,'') as attendance_date, CASE WHEN " +
                    "(at.in_time = '' or at.in_time is null) and (at.out_time = '' or at.out_time is null) " +
                    "THEN 'Leave'ELSE at.in_time END AS in_time, CASE WHEN (at.in_time = '' or at.in_time " +
                    "is null) and (at.out_time = '' or at.out_time is null) THEN 'Leave'ELSE at.out_time " +
                    "END AS out_time, at.total_working_hours/60 as working_hour_in_min , " +
                    "at.total_working_hours/(60*60) as working_hour_in_hour, pe.minor_staff_type " +
                    "from attendance at left join people pe on at.emp_id = pe.id where pe.person_type = 'minor' " +
                    "and at.date >= :start_date and at.date < :end_date order by pe.name_in_full, at.date) obj\n" +
                    "where in_time = 'Leave' and out_time = 'Leave'\n" +
                    "group by id,name_in_full;")
    List<Object> getMinoStaffLeaveByDate(@Param("start_date") String start_date, @Param("end_date") String end_date);




}
