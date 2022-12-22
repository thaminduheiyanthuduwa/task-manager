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
    private String adminId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "designation")
    private String designation;

    @Column(name = "emp_level")
    private String empLevel;

    @Column(name = "admin_department_id")
    private String adminDepartmentId;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "faculty_id")
    private String facultyId;

    @Column(name = "title_id")
    private String titleId;

    @Column(name = "qualification_id")
    private String qualificationId;

    @Column(name = "university_id")
    private String universityId;

    @Column(name = "qualification_name")
    private String qualificationName;

    @Column(name = "qualification_level_id")
    private String qualificationLevelId;

    @Column(name = "academic_carder_position_id")
    private String academicCarderPositionId;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "old_employee_number")
    private String oldEmployeeNumber;

    @Column(name = "epf_number")
    private String epfNumber;

    @Column(name = "person_type")
    private String personType;

    @Column(name = "staff_type")
    private String staffType;

    @Column(name = "name_in_full")
    private String nameInFull;

    @Column(name = "name_with_init")
    private String nameWithInit;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "nic_no")
    private String nicNo;

    @Column(name = "passport_no")
    private String passportNo;

    @Column(name = "perm_address")
    private String permAddress;

    @Column(name = "perm_work_address")
    private String permWorkAddress;


    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_number")
    private String emergencyContactNumber;

    @Column(name = "emergency_contact_address")
    private String emergencyContactAddress;

    @Column(name = "emergency_contact_nic")
    private String emergencyContactNic;

    @Column(name = "emergency_contact_ralation")
    private String emergencyContactRalation;

    @Column(name = "email")
    private String email;

    @Column(name = "qualified_year")
    private String qualifiedYear;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private String status;

    @Column(name = "hr_employee_status")
    private String hrEmployeeStatus;

    @Column(name = "approval_status")
    private String approvalStatus;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "deleted_at")
    private String deletedAt;

    @Column(name = "supervisor")
    private Integer supervisor;

    public EmpDetailEntity() {}

}
