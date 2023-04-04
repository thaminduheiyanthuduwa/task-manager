package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Integer id;

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

    @Column(name = "recurring")
    private String recurring;

    @Column(name = "sub_id")
    private Integer subId;

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

    @Column(name = "priority")
    private String priority;

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

    @Column(name = "completed_date")
    private Date completedDate;

    @Column(name = "deleted_date")
    private Date deletedDate;

    @Column(name = "is_reverted")
    private int isReverted;

    @Column(name = "auto_status")
    private int autoStatus;

    @Column(name = "create_date")
    private Date createDate;

    public TaskListEntity() {}


    @Override
    public Object clone() {
        try {
            return (TaskListEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            return new TaskListEntity(this.id,
                    this.userId,this.categoryId,
                    this.taskTitle, this.taskDescription,this.startDate,
                    this.endDate,this.reporterId,
                    this.label, this.recurring,this.subId,
                    this.estimate, this.originalEstimate,
                    this.blockedTask, this.status, this.isActive,
                    this.priority, this.rating, this.ratingComment,
                    this.supervisorRating, this.supervisorComment,
                    this.lastUpdatedUser, this.lastUpdatedDate,
                    this.completedDate,this.deletedDate,this.isReverted, this.autoStatus,
                    this.createDate);
        }
    }

    public Date getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String temp = sdf.format(endDate);
        try {
            return sdf.parse(temp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
