package com.taskmanager.task.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@Table(name = "my_roster")
public class RosterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "title")
    private String 	title;

    @Column(name = "calendar")
    private String calendar;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "user")
    private Integer user;

    @Column(name = "created_user")
    private Integer createdUser;

    @Column(name = "all_day")
    private Integer allDay;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "change_requested_by")
    private Integer changeRequestedBy;

    @Column(name = "reason")
    private String reason;

    @Column(name = "sub_task")
    private Integer subTask;

    @Column(name = "requested_user_id")
    private Integer requestedUserId;

    @Column(name = "requested_user_name")
    private String requestedUserName;

    @Column(name = "request_for_id")
    private Integer requestForId;

    @Column(name = "request_for_name")
    private String RequestForName;

    public RosterEntity() {}

    @Override
    public Object clone() {
        try {
            return (RosterEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            return new RosterEntity(this.id,
                    this.title,this.calendar,
                    this.startDate, this.endDate,this.user,
                    this.createdUser, this.allDay,this.description,
                    this.status, this.uniqueId,
                    this.changeRequestedBy, this.reason, this.subTask,
                    this.requestedUserId,this.requestedUserName,
                    this.requestForId,this.RequestForName);
        }
    }

}
