package com.taskmanager.task.model.Payroll;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MorningLateReportObject {

    private String id;

    private String name;

    private String departmentName;

    private String adminDepartmentName;

    private String personType;

    private String hrEmployeeStatus;

    private String morningLateTime;

    public MorningLateReportObject(String id, String name, String departmentName, String adminDepartmentName, String personType, String hrEmployeeStatus, String morningLateTime) {
        this.id = id;
        this.name = name;
        this.departmentName = departmentName;
        this.adminDepartmentName = adminDepartmentName;
        this.personType = personType;
        this.hrEmployeeStatus = hrEmployeeStatus;
        this.morningLateTime = morningLateTime;
    }
}
