package com.taskmanager.task.model.Attendance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
public class MyAttendance {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("empName")
    private String empName;

    @JsonProperty("date")
    private String date;

    @JsonProperty("inTime")
    private String inTime;

    @JsonProperty("outTime")
    private String outTime;

    @JsonProperty("type")
    private String type;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("approvedBy")
    private String approvedBy;

    @JsonProperty("approvedDate")
    private String approvedDate;

    @JsonProperty("totalWorkingTime")
    private String totalWorkingTime;

    @JsonProperty("lateTime")
    private String lateTime;

    @JsonProperty("otTime")
    private String otTime;

    @JsonProperty("applyOt")
    private Integer applyOt;

    @JsonProperty("attendanceStatus")
    private Integer attendanceStatus;

    public MyAttendance() {
    }
}
