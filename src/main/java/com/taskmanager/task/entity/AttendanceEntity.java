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
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "empId")
    private int empId;

    @Column(name="date")
    private Date date;

    @Column(name="inTime")
    private String inTime;

    @Column(name="outTime")
    private String outTime;

    @Column(name="workDuration")
    private long workDuration;

    @Column(name="type")
    private String type;

    @Column(name="comment")
    private String comment;

    @Column(name="approvedBy")
    private String approvedBy;

    @Column(name="approvedDate")
    private Date approvedDate;

    @Column(name="status")
    private Integer status;

    @Column(name="approved_by_id")
    private Integer approved_by_id;

    @Column(name="supervisor_comment")
    private String supervisorComment;


}
