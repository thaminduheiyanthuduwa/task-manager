package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LeaveEntityGetByIdObj {

    private Integer id;

    private Integer empId;

    private String leaveType;

    private String fromDate;

    private String toDate;

    private Float totalLeave;

    private Integer status;

    private String comment;

    private String createdDate;

    private Integer approvedBy;

    private String approvedDate;

    public LeaveEntityGetByIdObj() {}
}
