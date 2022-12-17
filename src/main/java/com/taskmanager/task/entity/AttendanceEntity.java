package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "emp_id")
    private int empId;

    @Column(name="date")
    private Date date;

    @Column(name="in_time")
    private String inTime;

    @Column(name="out_time")
    private String outTime;

    @Column(name="work_duration")
    private int workDuration;

    @Column(name="type")
    private String Type;

    @Column(name="comment")
    private String comment;

    @Column(name="approved_by")
    private String approvedBy;

    @Column(name="approved_date")
    private Date approvedDate;


}
