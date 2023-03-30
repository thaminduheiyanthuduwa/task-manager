package com.taskmanager.task.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EstimateDailyTaskBreakDownSummary {

    private List<EstimateDailyTaskBreakDown> id;

    private Integer totalScore;

    public EstimateDailyTaskBreakDownSummary() {
    }
}
