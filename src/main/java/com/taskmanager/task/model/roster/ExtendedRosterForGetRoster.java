package com.taskmanager.task.model.roster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExtendedRosterForGetRoster {

    @JsonProperty("calendar")
    private String calendar;

    @JsonProperty("description")
    private String description;


}
