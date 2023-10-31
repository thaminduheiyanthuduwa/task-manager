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

    @Query(nativeQuery = true,
            value = "SELECT p.id, p.name_in_full, IFNULL(d.dept_name,'NOT FOUND'), IFNULL(adi.department_name,'NOT FOUND') as Admin_Department_Id,\n" +
                    "IFNULL(p.person_type,'NOT FOUND') ,IFNULL(p.hr_employee_status,'NOT FOUND'), \n" +
                    "IFNULL(cas.tot,0) as Casual, IFNULL(anu.tot,0) as Annual, IFNULL(day_off.tot,0) as Day_Off_Apr,  (IFNULL(spe.tot,0)+IFNULL(spe2.tot,0)) as Special_Leave,  \n" +
                    "IFNULL(probation_half_day.tot,0) as Probation_Half_Day, IFNULL(short_leave.tot,0) as Short_Leave, \n" +
                    "IFNULL(no_pay_weekday.count,0) as No_Pay_Weekday,\n" +
                    "IFNULL(no_pay_weekend.count,0) as No_Pay_Weekend\n" +
                    "FROM people p \n" +
                    "left join departments d on p.dept_id = d.dept_id \n" +
                    "left join admin_departments adi on adi.admin_department_id = p.admin_department_id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Casual','Casual - Sudden Illness','Casual - Funeral', 'Casual - Accident or vehicle breakdown', 'Special - Marketing') and lm.status = 5 and lm.from_date >= '2023-09-01' and lm.to_date < '2023-10-01' GROUP BY lm.emp_id) cas on cas.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Annual') and lm.status = 5 and lm.from_date >= '2023-09-01' and lm.to_date < '2023-10-01' GROUP BY lm.emp_id) anu on anu.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Day Off - Sep') and lm.status = 5 and lm.from_date >= '2023-09-01' and lm.to_date < '2023-10-01' GROUP BY lm.emp_id) day_off on day_off.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Special') and lm.status = 5 and lm.from_date >= '2023-09-01' and lm.to_date < '2023-10-01' GROUP BY lm.emp_id) spe on spe.emp_id = p.id \n" +
//                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Convocation 2023 Leave') and lm.status = 5 and lm.from_date >= '2023-08-01' and lm.to_date < '2023-09-04' GROUP BY lm.emp_id) spe2 on spe2.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Probation Half Day') and lm.status = 5 and lm.from_date >= '2023-09-01' and lm.to_date < '2023-10-01' GROUP BY lm.emp_id) probation_half_day on probation_half_day.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Short Leave') and lm.status = 5 and lm.from_date >= '2023-09-01' and lm.to_date < '2023-10-01' GROUP BY lm.emp_id) short_leave on short_leave.emp_id = p.id\n" +
                    "left join (select at.emp_id, count(at.id) as count from attendance at where at.date >= '2023-09-01' and at.date < '2023-10-01' and at.pay_roll_status = 8 group by at.emp_id) no_pay_weekday on no_pay_weekday.emp_id = p.id\n" +
                    "left join (select at.emp_id, count(at.id) as count from attendance at where at.date >= '2023-09-01' and at.date < '2023-10-01' and at.pay_roll_status = -1 group by at.emp_id) no_pay_weekend on no_pay_weekend.emp_id = p.id\n" +
                    "\n" +
                    "where p.hr_employee_status in ('ACTIVE','PERMANENT','PROBATION','CONTRACT') and p.person_type in ('lecturer','individual','employee',' intern') and p.id in (select emp_id from all_salary_info);")
    List<Object> getPayrollLeaveReportData();

    @Query(nativeQuery = true,
            value = "SELECT p.id, p.name_in_full, IFNULL(d.dept_name,'NOT FOUND'), IFNULL(adi.department_name,'NOT FOUND') as Admin_Department_Id,\n" +
                    "IFNULL(p.person_type,'NOT FOUND') ,IFNULL(p.hr_employee_status,'NOT FOUND'), ((obj.amount * (-1))/60) as morning_late\n" +
                    "FROM people p \n" +
                    "left join departments d on p.dept_id = d.dept_id \n" +
                    "left join admin_departments adi on adi.admin_department_id = p.admin_department_id \n" +
                    "left join (SELECT at.emp_id, sum(at.morning_late) as amount FROM attendance at \n" +
                    "where at.date >= '2023-09-01' and at.date < '2023-10-01' and \n" +
                    "(at.apply_late = 0 or at.apply_late = 5) and at.pay_roll_status in (2,3,4,5,7,11)\n" +
                    "group by at.emp_id \n" +
                    "having sum(at.morning_late) < 0) obj on obj.emp_id = p.id\n" +
                    "where p.hr_employee_status in ('ACTIVE','PERMANENT','PROBATION','CONTRACT') and p.person_type in ('lecturer','individual','employee',' intern') and p.id in (select emp_id from all_salary_info);")
    List<Object> getMorningLateReportData();


    @Query(nativeQuery = true,
            value = "select pe.id, pe.name_in_full, IFNULL(d.dept_name,'NOT FOUND'), IFNULL(adi.department_name,'NOT FOUND') as Admin_Department_Id,\n" +
                    "IFNULL(pe.person_type,'NOT FOUND') as person_type ,IFNULL(pe.hr_employee_status,'NOT FOUND') as hr_employee_status ,((30-((IFNULL(obj1.total_count,0) + IFNULL(obj2.total_available_leaves,0))))*8) as expected_working_hours, obj3.original_count as original_hours   from people pe \n" +
                    "left join departments d on pe.dept_id = d.dept_id \n" +
                    "left join admin_departments adi on adi.admin_department_id = pe.admin_department_id\n" +
                    "left join (SELECT lm.emp_id, sum(lm.total_leave) as total_count FROM leave_manager lm WHERE lm.status = 5 AND lm.from_date >= '2023-09-01' AND lm.to_date < '2023-10-01' and lm.leave_type not in ('Day Off - Sep','Convocation 2023 Leave')  GROUP BY lm.emp_id) obj1\n" +
                    "on obj1.emp_id = pe.id\n" +
                    "left join (SELECT al.emp_id, SUM(al.original_leaves) as total_available_leaves FROM available_leaves al WHERE al.type like '%sep%' GROUP BY al.emp_id) obj2 on obj2.emp_id = pe.id\n" +
                    "\n" +
                    "left join (SELECT tl.user_id, IFNULL(SUM(IFNULL(tl.estimate,0)),0) as original_count FROM task_list tl WHERE tl.status = 5 AND tl.start_date >= '2023-09-01' AND tl.start_date < '2023-10-01' GROUP BY tl.user_id) obj3 on obj3.user_id = pe.id where pe.id in (select emp_id from all_salary_info);")
    List<Object> getTaskReportData();

}
