package com.taskmanager.task.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoryPoints {

    private Integer tickets;
    private Integer counts;
    private Double percentage;
    private Double totalStory;
    private String status;

    public StoryPoints() {}

}
