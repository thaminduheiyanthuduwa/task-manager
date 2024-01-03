package com.taskmanager.task.model.income_report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class IncomeReportResponseModel {
    @JsonProperty("course_fee_income")
    private String courseFeeIncome;
    @JsonProperty("registration_income")
    private String registrationIncome;
    @JsonProperty("initial_income")
    private String initialIncome;
    @JsonProperty("total_income")
    private String totalIncome;
    @JsonProperty("data")
    List<IncomeReportDataModel> data;

}
