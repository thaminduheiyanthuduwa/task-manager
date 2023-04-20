package com.taskmanager.task.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PerformanceOutObj {

    private String name;

    private List<PerformanceDetailObj> performanceDetailObj;

    private String status;

    private String rank;

    private Float totalTaskManagerScore;

    private Float totalAttendanceScore;

    private Float totalNoPayScore;

    private Float totalScore;


    public PerformanceOutObj() {}

}
