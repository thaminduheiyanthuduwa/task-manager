package com.taskmanager.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EstimateDailyTaskBreakDown {

    private Integer id;

    private Integer userId;

    private Integer taskCount;

    private Double totalEstimation;

    private String date;

    private Date dateSort;

    private Integer status;

    public EstimateDailyTaskBreakDown() {
    }
}
