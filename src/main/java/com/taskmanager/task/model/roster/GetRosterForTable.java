package com.taskmanager.task.model.roster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class GetRosterForTable {

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

    @JsonProperty("type")
    private String calendar;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("requested_by")
    private String requestedBy;

    @JsonProperty("requested_for")
    private String RequestedForName;

}
