package com.taskmanager.task.entity;

import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonFormat;
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

    @Column(name="total_working_hours")
    private String totalWorkingHours;

    @Column(name="late_time")
    private String lateTime;

    @Column(name="ot_time")
    private String otTime;

    @Column(name="apply_ot")
    private Integer applyOt;

    @Column(name="apply_late")
    private Integer applyLate;

    @Column(name="leave_id")
    private Integer leaveId;

    @Column(name="leave_time")
    private Integer leaveTime;

    @Column(name="is_working_day")
    private Float isWorkingDay;

    @Column(name="is_extra_working")
    private Integer isExtraWorking;

    @Column(name="ot_amount")
    private Float otAmount;

    @Column(name="no_pay_amount")
    private Float noPayAmount;

    @Column(name="pay_roll_status")
    private Integer payRollStatus;

    @Column(name="late_amount")
    private Float lateAmount;

    @Column(name="morning_late")
    private String morningLate;

    @Column(name=" morning_late_amount")
    private Float morningLateAmount;

}
