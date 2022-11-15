package com.taskmanager.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateTask {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("task_title")
    private String taskTitle;

    @JsonProperty("task_description")
    private String taskDescription;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("reporter_id")
    private Integer reporterId;

    @JsonProperty("label")
    private String label;

    @JsonProperty("recurring")
    private String recurring;

    @JsonProperty("estimate")
    private Double estimate;

    @JsonProperty("original_estimate")
    private Double originalEstimate;

    @JsonProperty("blocked_task")
    private Integer blockedTask;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("is_active")
    private Integer isActive;

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("rating_comment")
    private String ratingComment;

    @JsonProperty("supervisor_rating")
    private Integer supervisorRating;

    @JsonProperty("supervisor_comment")
    private String supervisorComment;

    @JsonProperty("last_updated_user")
    private Integer lastUpdatedUser;

}
