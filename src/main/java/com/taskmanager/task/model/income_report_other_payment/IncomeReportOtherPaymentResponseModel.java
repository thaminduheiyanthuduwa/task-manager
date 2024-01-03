package com.taskmanager.task.model.income_report_other_payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class IncomeReportOtherPaymentResponseModel {
    @JsonProperty("total_income")
    private String totalIncome;
    @JsonProperty("data_list")
    List<IncomeReportOtherPaymentDataModel> dataList;
}
