package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "penalty")
public class PenaltyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "penalty_amount")
    private Float penaltyAmount;

    @Column(name="reason")
    private String reason;

    @Column(name="created_date")
    private Date createdDate;

    @Column(name="approved_date")
    private Date approvedDate;

    @Column(name="approved_by")
    private Integer approvedBy;

    @Column(name="status")
    private Integer status;




}
