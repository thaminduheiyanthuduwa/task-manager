package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "all_salary_info")
public class AllSalaryInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "type")
    private String type;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "month")
    private String month;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "approved_date")
    private Date approvedDate;

    @Column(name = "approved_by")
    private Integer approvedBy;

    public AllSalaryInfoEntity() {}

}
