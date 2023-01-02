package com.taskmanager.task.model.Attendance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErpAttendance {

    private Integer id;

    private Date date;

    private Date createdTime;

    private String issueInTime;

    private String issueOutTime;

    private Integer status;

    private String comment;

    private String statusComment;

    private String type;

    private String approvedBy;

    private Date approvedDate;


    public ErpAttendance() {
    }
}
