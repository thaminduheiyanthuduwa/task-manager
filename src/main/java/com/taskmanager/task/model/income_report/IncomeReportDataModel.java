package com.taskmanager.task.model.income_report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeReportDataModel {
    @JsonProperty("payment_plan_card_id")
    private Object paymentPlanCardId;
    @JsonProperty("student_id")
    private Integer studentId;
    @JsonProperty("name_initials")
    private String nameInitials;
    @JsonProperty("registered_date")
    private Object registeredDate ;
    @JsonProperty("installment_type")
    private String installmentType;
    @JsonProperty("student_category")
    private String studentCategory;
    @JsonProperty("installment_counter")
    private Integer installmentCounter;
    @JsonProperty("due_amount")
    private Double dueAmount;

    private Double amount;
    @JsonProperty("tax_paid")
    private Double taxPaid;
    @JsonProperty("total_late_payment_paid")
    private Double totalLatePaymentPaid;
    @JsonProperty("payment_status")
    private String paymentStatus;
    @JsonProperty("batch_name")
    private String batchName;
    @JsonProperty("due_date")
    private  Object dueDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("batch_id")
    private Long batchId;
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
    @JsonProperty("total_paid")
    private Double totalPaid;
}
