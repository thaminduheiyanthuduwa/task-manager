package com.taskmanager.task.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LastFourDayDueObj {

    private String due1;
    private String due2;
    private String due3;
    private String due4;
    private String task1;
    private String task2;
    private String task3;
    private String task4;
    private String priority1;
    private String priority2;
    private String priority3;
    private String priority4;


    public LastFourDayDueObj() {}

}
