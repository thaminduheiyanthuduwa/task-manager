package com.taskmanager.task.model.Payroll;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllLeaveReportObject {

    private String id;

    private String name;

    private String departmentName;

    private String adminDepartmentName;

    private String personType;

    private String hrEmployeeStatus;

    private String casual;

    private String annual;

    private String dayOffApr;

    private String specialLeave;

    private String probationHalfDay;

    private String shortLeave;

    private String noPayWeekday;

    private String noPayWeekend;

    public AllLeaveReportObject(String id, String name, String departmentName, String adminDepartmentName, String personType, String hrEmployeeStatus, String casual, String annual, String dayOffApr, String specialLeave, String probationHalfDay, String shortLeave, String noPayWeekday, String noPayWeekend) {
        this.id = id;
        this.name = name;
        this.departmentName = departmentName;
        this.adminDepartmentName = adminDepartmentName;
        this.personType = personType;
        this.hrEmployeeStatus = hrEmployeeStatus;
        this.casual = casual;
        this.annual = annual;
        this.dayOffApr = dayOffApr;
        this.specialLeave = specialLeave;
        this.probationHalfDay = probationHalfDay;
        this.shortLeave = shortLeave;
        this.noPayWeekday = noPayWeekday;
        this.noPayWeekend = noPayWeekend;
    }
}
