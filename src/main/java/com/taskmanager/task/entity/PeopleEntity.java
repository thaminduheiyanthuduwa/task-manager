package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "people")
public class PeopleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "designation")
    private Long designation;

    @Column(name = "emp_level")
    private Long empLevel;

    @Column(name = "admin_department_id")
    private Integer adminDepartmentId;

    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "faculty_id")
    private Integer facultyId;

    @Column(name = "title_id")
    private Integer titleId;

    @Column(name = "qualification_id")
    private Short qualificationId;

    @Column(name = "university_id")
    private Integer universityId;

    @Column(name = "qualification_name")
    private String qualificationName;

    @Column(name = "qualification_level_id")
    private Short qualificationLevelId;

    @Column(name = "academic_carder_position_id")
    private Long academicCarderPositionId;

    @Column(name = "serial_number")
    private Integer serialNumber;

    @Column(name = "old_employee_number")
    private Integer oldEmployeeNumber;

    @Column(name = "epf_number")
    private Integer epfNumber;

    @Column(name = "person_type", nullable = false)
    private String personType = "employee";

    @Column(name = "minor_staff_type")
    private String minorStaffType;

    @Column(name = "staff_type", nullable = false)
    private Integer staffType;

    @Column(name = "name_in_full")
    private String nameInFull;

    @Column(name = "name_with_init")
    private String nameWithInit;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "nic_no")
    private String nicNo;

    @Column(name = "passport_no")
    private String passportNo;

    @Column(name = "perm_address", columnDefinition = "text")
    private String permAddress;

    @Column(name = "perm_work_address", columnDefinition = "text")
    private String permWorkAddress;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_number")
    private String emergencyContactNumber;

    @Column(name = "emergency_contact_address", columnDefinition = "tinytext")
    private String emergencyContactAddress;

    @Column(name = "emergency_contact_nic")
    private String emergencyContactNic;

    @Column(name = "emergency_contact_ralation")
    private String emergencyContactRelation;

    @Column(name = "email")
    private String email;

    @Column(name = "qualified_year")
    private Short qualifiedYear;

    @Column(name = "remarks", columnDefinition = "text")
    private String remarks;

    @Column(name = "status")
    private Integer status;

    @Column(name = "hr_employee_status", nullable = false)
    private String hrEmployeeStatus = "ACTIVE";

    @Column(name = "approval_status")
    private Integer approvalStatus;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

}
