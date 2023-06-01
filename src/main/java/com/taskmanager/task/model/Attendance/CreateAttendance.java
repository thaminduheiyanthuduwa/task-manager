package com.taskmanager.task.model.Attendance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class CreateAttendance {


    @JsonProperty("date")
    private String date;

    @JsonProperty("inTime")
    private String inTime;

    @JsonProperty("inTimeNew")
    private String inTimeNew;

    @JsonProperty("outTime")
    private String outTime;

    @JsonProperty("outTimeNew")
    private String outTimeNew;

    @JsonProperty("type")
    private String type;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("approvedBy")
    private String approvedBy;

    @JsonProperty("approvedDate")
    private String approvedDate;

    @JsonProperty("requestIssue")
    private Integer requestIssue;
}
