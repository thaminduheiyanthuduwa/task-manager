package com.taskmanager.task.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class TaskListEntityResponse {


    private int id;

    private Integer userId;

    private Integer categoryId;

    private String taskTitle;

    private String taskDescription;

    private String startDate;

    private String endDate;

    private Integer reporterId;

    private String label;

    private String recurring;

    private Integer subId;

    private Double estimate;

    private Double originalEstimate;

    private Integer blockedTask;

    private Integer status;

    private Integer isActive;

    private String priority;

    private Integer rating;

    private String ratingComment;

    private Integer supervisorRating;

    private String supervisorComment;

    private Integer lastUpdatedUser;

    private String lastUpdatedDate;

    private String completedDate;

    private String deletedDate;

    public TaskListEntityResponse() {}

}
