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
@Table(name = "leave_manager")
public class LeaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "total_leave")
    private Integer totalLeave;

    @Column(name = "status")
    private Integer status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_date")
    private Date approvedDate;

    public LeaveEntity() {}
}
