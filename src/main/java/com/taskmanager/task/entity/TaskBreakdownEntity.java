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
@Table(name = "estimate_breakdown")
public class TaskBreakdownEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "estimate")
    private Double estimate;

    @Column(name = "date")
    private Date date;

    public TaskBreakdownEntity() {}

}
