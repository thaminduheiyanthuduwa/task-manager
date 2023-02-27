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
    private Integer payrollId;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "serial_id")
    private String serialId;

    @Column(name = "name")
    private String name;

    @Column(name = "is_no_pay")
    private Integer isNoPay;

    @Column(name = "is_ot")
    private Integer isOt;

    @Column(name = "advance")
    private Float advance;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "applicable_dates")
    private Integer applicableDates;

    @Column(name = "is_ot_basic")
    private Integer isOtBasic;

    @Column(name = "is_no_pay_basic")
    private Integer isNoPayBasic;

    @Column(name = "total_ot")
    private Float totalOt;

    @Column(name = "total_no_pay")
    private Float totalNoPay;

    @Column(name = "basic_salary")
    private Float basicSalary;

    @Column(name = "not_applicable")
    private Integer notApplicable;

    @Column(name = "gross_salary")
    private Float grossSalary;

    public PayrollEntityDetails() {}

}
