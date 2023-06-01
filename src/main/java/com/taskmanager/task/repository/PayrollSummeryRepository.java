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
            value = "SELECT p.id, p.name_in_full, d.dept_name, adi.department_name as Admin_Department_Id,p.person_type,p.hr_employee_status, cas.tot as Casual, anu.tot as Annual, day_off.tot as Day_Off_Apr,  spe.tot as Special_Leave, probation_half_day.tot as Probation_Half_Day, short_leave.tot as Short_Leave, \n" +
                    "no_pay_weekday.count as No_Pay_Weekday,\n" +
                    "no_pay_weekend.count as No_Pay_Weekend\n" +
                    "FROM people p \n" +
                    "left join departments d on p.dept_id = d.dept_id \n" +
                    "left join admin_departments adi on adi.admin_department_id = p.admin_department_id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Casual','Casual - Sudden Illness','Casual - Funeral', 'Casual - Accident or vehicle breakdown') and lm.status = 5 and lm.from_date >= '2023-05-01' and lm.to_date < '2023-06-01' GROUP BY lm.emp_id) cas on cas.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Annual') and lm.status = 5 and lm.from_date >= '2023-05-01' and lm.to_date < '2023-06-01' GROUP BY lm.emp_id) anu on anu.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Day Off - May') and lm.status = 5 and lm.from_date >= '2023-05-01' and lm.to_date < '2023-06-01' GROUP BY lm.emp_id) day_off on day_off.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Special') and lm.status = 5 and lm.from_date >= '2023-05-01' and lm.to_date < '2023-06-01' GROUP BY lm.emp_id) spe on spe.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Probation Half Day') and lm.status = 5 and lm.from_date >= '2023-05-01' and lm.to_date < '2023-06-01' GROUP BY lm.emp_id) probation_half_day on probation_half_day.emp_id = p.id \n" +
                    "left join (select lm.emp_id, sum(lm.total_leave) as tot from leave_manager lm WHERE lm.leave_type in ('Short Leave') and lm.status = 5 and lm.from_date >= '2023-05-01' and lm.to_date < '2023-06-01' GROUP BY lm.emp_id) short_leave on short_leave.emp_id = p.id\n" +
                    "left join (select at.emp_id, count(at.id) as count from attendance at where at.date >= '2023-05-01' and at.date < '2023-06-01' and at.pay_roll_status = 8 group by at.emp_id) no_pay_weekday on no_pay_weekday.emp_id = p.id\n" +
                    "left join (select at.emp_id, count(at.id) as count from attendance at where at.date >= '2023-05-01' and at.date < '2023-06-01' and at.pay_roll_status = -1 group by at.emp_id) no_pay_weekend on no_pay_weekend.emp_id = p.id\n" +
                    "\n" +
                    "where p.hr_employee_status in ('ACTIVE','PERMANENT','PROBATION','CONTRACT') and p.person_type in ('lecturer','individual','employee',' intern');")
    List<Object> getPayrollLeaveReportData();


}
