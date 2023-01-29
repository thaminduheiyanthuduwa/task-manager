package com.taskmanager.task.model.penalty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PenaltyInfo {

    private int id;

    private Integer empId;

    private String empName;

    private Float penaltyAmount;

    private String reason;

    private Date createdDate;

    private Date approvedDate;

    private Integer approvedBy;

    private Integer status;
}
