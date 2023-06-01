package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntityGetIdObj {

    private int id;

    private String name;

    private int empId;

    private String date;

    private String inTime;

    private String outTime;

    private long workDuration;

    private String type;

    private String comment;

    private String approvedBy;

    private String approvedDate;

    private Integer status;

    private Integer approved_by_id;

    private String supervisorComment;

    private Integer requestIssue;


}
