package com.taskmanager.task.model.out_standing_other_payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OutStandingOtherPaymentResponseModel {
    @JsonProperty("data_list")
    private List<OutStandingOtherPaymentDataModel> dataList;
    @JsonProperty("total_due_amount")
    private String totalDueAmount;
}
