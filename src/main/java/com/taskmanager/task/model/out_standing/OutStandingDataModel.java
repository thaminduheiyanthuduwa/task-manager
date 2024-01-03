package com.taskmanager.task.model.out_standing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutStandingDataModel {
    @JsonProperty("payment_plan_card_id")
    private Object paymentPlanCardId;
    @JsonProperty("due_amount")
    private Double dueAmount;
    @JsonProperty("batch_name")
    private String batchName;
    @JsonProperty("status")
    private String status;
    @JsonProperty("payment_status")
    private String paymentStatus;
    @JsonProperty("due_date")
    private Object dueDate;
    @JsonProperty("name_initials")
    private String nameInitials;
    @JsonProperty("registered_date")
    private Object registeredDate;
    @JsonProperty("installment_type")
    private String installmentType;
    @JsonProperty("student_id")
    private Integer studentId;
    @JsonProperty("installment_counter")
    private Integer installmentCounter;
    @JsonProperty("payment_type")
    private String paymentType;
    @JsonProperty("tax_paid")
    private Double taxPaid;
    @JsonProperty("course_name")
    private String courseName;
    @JsonProperty("student_category")
    private String studentCategory;
}
