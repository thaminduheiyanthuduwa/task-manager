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
@Table(name = "hr_attendance")
public class AttendanceReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "date_time")
    private Date dateTime;

    @Column(name = "created_at")
    private Date createdAt;
}
