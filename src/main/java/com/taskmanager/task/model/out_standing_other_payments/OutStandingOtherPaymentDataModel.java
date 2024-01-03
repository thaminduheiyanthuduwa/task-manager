package com.taskmanager.task.model.out_standing_other_payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutStandingOtherPaymentDataModel {
    private Long id;
    private String status;
    @JsonProperty("student_id")
    private Integer studentId;
    @JsonProperty("name_initials")
    private String nameInitials;

    private Double amount;
    @JsonProperty("due_date")
    private Object dueDate;
    private String name;
    @JsonProperty("payment_type")
    private String paymentType;
    @JsonProperty("batch_name")
    private String batchName;
    @JsonProperty("course_name")
    private String courseName;
    @JsonProperty("payment_plan_card_status")
    private String paymentPlanCardStatus;
    @JsonProperty("total_paid")
    private Double totalPaid;
    @JsonProperty("due_amount")
    private Double dueAmount;
    @JsonProperty("payment_status")
    private String paymentStatus;
    @JsonProperty("registered_date")
    private String registeredDate;
    @JsonProperty("student_category")
    private String studentCategory;
}
