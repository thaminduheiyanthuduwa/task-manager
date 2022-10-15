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
@Table(name = "task_list")
public class TaskListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "reporter_id")
    private Integer reporterId;

    @Column(name = "label")
    private String label;

    @Column(name = "estimate")
    private Double estimate;

    @Column(name = "original_estimate")
    private Double originalEstimate;

    @Column(name = "blocked_task")
    private Integer blockedTask;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "rating_comment")
    private String ratingComment;

    @Column(name = "supervisor_rating")
    private Integer supervisorRating;

    @Column(name = "supervisor_comment")
    private String supervisorComment;

    @Column(name = "last_updated_user")
    private Integer lastUpdatedUser;

    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;

    public TaskListEntity() {}

}
