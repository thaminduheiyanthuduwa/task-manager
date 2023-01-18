package com.taskmanager.task.model.roster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRoster {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("title")
    private String title;

    @JsonProperty("start")
    private String start;

    @JsonProperty("end")
    private String end;

    @JsonProperty("allDay")
    private Boolean allDay;

    @JsonProperty("extendedProps")
    private ExtendedRosterForGetRoster extendedProps;

}
