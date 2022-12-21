package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "people")
public class EmpDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "admin_id")
    private String admin_id;

    @Column(name = "employee_id")
    private String employee_id;

    @Column(name = "designation")
    private String designation;

    @Column(name = "emp_level")
    private String emp_level;

    @Column(name = "admin_department_id")
    private String admin_department_id;

    @Column(name = "dept_id")
    private String dept_id;

    @Column(name = "faculty_id")
    private String faculty_id;

    @Column(name = "title_id")
    private String title_id;

    @Column(name = "qualification_id")
    private String qualification_id;

    @Column(name = "university_id")
    private String university_id;

    @Column(name = "qualification_name")
    private String qualification_name;

    @Column(name = "qualification_level_id")
    private String qualification_level_id;

    @Column(name = "academic_carder_position_id")
    private String academic_carder_position_id;

    @Column(name = "serial_number")
    private String serial_number;

    @Column(name = "old_employee_number")
    private String old_employee_number;

    @Column(name = "epf_number")
    private String epf_number;

    @Column(name = "person_type")
    private String person_type;

    @Column(name = "staff_type")
    private String staff_type;

    @Column(name = "name_in_full")
    private String name_in_full;

    @Column(name = "name_with_init")
    private String name_with_init;

    @Column(name = "given_name")
    private String given_name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "date_of_birth")
    private String date_of_birth;

    @Column(name = "nic_no")
    private String nicNo;

    @Column(name = "passport_no")
    private String passport_no;

    @Column(name = "perm_address")
    private String perm_address;

    @Column(name = "perm_work_address")
    private String perm_work_address;


    @Column(name = "contact_no")
    private String contact_no;

    @Column(name = "emergency_contact_name")
    private String emergency_contact_name;

    @Column(name = "emergency_contact_number")
    private String emergency_contact_number;

    @Column(name = "emergency_contact_address")
    private String emergency_contact_address;

    @Column(name = "emergency_contact_nic")
    private String emergency_contact_nic;

    @Column(name = "emergency_contact_ralation")
    private String emergency_contact_ralation;

    @Column(name = "email")
    private String email;

    @Column(name = "qualified_year")
    private String qualified_year;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private String status;

    @Column(name = "hr_employee_status")
    private String hr_employee_status;

    @Column(name = "approval_status")
    private String approval_status;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "updated_by")
    private String updated_by;

    @Column(name = "deleted_by")
    private String deleted_by;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "deleted_at")
    private String deleted_at;

    @Column(name = "supervisor")
    private Integer supervisor;

    public EmpDetailEntity() {}

}
