package com.taskmanager.task.model.roster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRosterEmpList {

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("name")
    private String name;

}
