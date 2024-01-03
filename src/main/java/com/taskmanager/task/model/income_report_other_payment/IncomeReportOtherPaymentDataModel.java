package com.taskmanager.task.model.income_report_other_payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeReportOtherPaymentDataModel {
    @JsonProperty("payment_plan_card_id")
    private Object paymentPlanCardId;
    @JsonProperty("student_id")
    private Integer studentId;
    @JsonProperty("name_initials")
    private String nameInitials;
    @JsonProperty("payment_plan_cards_status")
    private String paymentPlanCardStatus;
    private Double amount;
    @JsonProperty("total_paid")
    private Double totalPaid;
    @JsonProperty("payment_status")
    private String paymentStatus;
    @JsonProperty("due_date")
    private Object dueDate;
    private String name;
    @JsonProperty("student_category")
    private String studentCategory;

    @JsonProperty("batch_id")
    private Long batchId;
    @JsonProperty("batch_name")
    private String batchName;
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("course_name")
    private String courseName;
    @JsonProperty("dept_id")
    private Integer deptId;
    @JsonProperty("dept_name")
    private String deptName;
    @JsonProperty("faculty_id")
    private Integer facultyId;
    @JsonProperty("faculty_name")
    private String facultyName;
}
