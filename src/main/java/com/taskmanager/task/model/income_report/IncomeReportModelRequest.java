package com.taskmanager.task.model.income_report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeReportModelRequest {
    @JsonProperty("batch_id")
    private Long batchId;
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("dept_id")
    private Integer deptId;
    @JsonProperty("faculty_id")
    private Integer facultyId;
    @JsonProperty("start_date")
    private String startDate;
}
