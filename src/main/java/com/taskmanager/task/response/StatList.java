package com.taskmanager.task.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;

@Getter
@Setter
public class StatList {

    private Integer completedTaskForToday;

    private Integer allTask;

    private Integer deletedTask;

    private Integer pendingTask;

    private Integer highPriorityTask;

    private Integer mediumPriorityTask;

    private Integer lowPriorityTask;

    public StatList() {}

}
