package com.taskmanager.task.model.roster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ExtendedRoster {

    @JsonProperty("calendar")
    private String calendar;

    @JsonProperty("description")
    private String description;

    @JsonProperty("guests")
    private List<UserRoster> guests;

}
