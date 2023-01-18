package com.taskmanager.task.model.roster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateRoster {

    @JsonProperty("allDay")
    private Boolean allDay;

    @JsonProperty("end")
    private String end;

    @JsonProperty("extendedProps")
    private ExtendedRoster extendedProps;

    @JsonProperty("start")
    private String start;

    @JsonProperty("title")
    private String title;

    @JsonProperty("url")
    private String url;

}
