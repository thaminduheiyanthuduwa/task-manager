package com.taskmanager.task.model.roster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRoster {

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("name")
    private String name;

}
