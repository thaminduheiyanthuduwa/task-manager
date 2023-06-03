package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "payroll_people_config")
public class PayrollEntityDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "payroll_id")
    private Integer payrollId = 0;

    @Column(name = "emp_id")
    private Integer empId = 0;

    @Column(name = "serial_id")
    private String serialId;

    @Column(name = "name")
    private String name;

    @Column(name = "is_no_pay")
    private Integer isNoPay ;

    @Column(name = "is_ot")
    private Integer isOt;

    @Column(name = "is_late")
    private Integer isLate;

    @Column(name = "advance")
    private Float advance = 0F;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "applicable_dates")
    private Integer applicableDates;

    @Column(name = "is_ot_basic")
    private Integer isOtBasic;

    @Column(name = "is_no_pay_basic")
    private Integer isNoPayBasic;

    @Column(name = "total_ot")
    private Float totalOt = 0F;

    @Column(name = "total_no_pay")
    private Float totalNoPay = 0F;

    @Column(name = "basic_salary")
    private Float basicSalary = 0F;

    @Column(name = "not_applicable")
    private Integer notApplicable;

    @Column(name = "gross_salary")
    private Float grossSalary = 0F;

    @Column(name = "total_late_amount")
    private Float totalLateAmount = 0F;

    @Column(name = "total_morning_late")
    private Float totalMorningLate = 0F;

    @Column(name = "epf_deduction")
    private Float epfDeduction = 0F;

    @Column(name = "epf_addition")
    private Float epfAddition = 0F;

    @Column(name = "etf")
    private Float etf = 0F;

    @Column(name = "total_deductions")
    private Float totalDeductions = 0F;

    @Column(name = "total_additions")
    private Float totalAdditions = 0F;

    @Column(name = "total_working_hours")
    private Float totalWorkingHours = 0F;

    @Column(name = "total_task_hours")
    private Float totalTaskHours = 0F;

    @Column(name = "total_deduction_for_tasks")
    private Float totalDeductionForTasks = 0F;

    @Column(name = "published")
    private Integer published;

    @Column(name = "payee")
    private Float payee = 0F;

    public PayrollEntityDetails() {}

}
