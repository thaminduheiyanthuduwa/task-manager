package com.taskmanager.task.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformanceDetailObj {

    private Integer id;

    private String category;

    private String score;

    private String idealRating;

    private String rank;

    private String comment;


    public PerformanceDetailObj() {}

}
