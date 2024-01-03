package com.taskmanager.task.model.out_standing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OutstandingResponseModel {
    @JsonProperty("data_list")
    private List<OutStandingDataModel> dataList;
    @JsonProperty("total_due_amount")
    private  String totalDueAmount;
}
