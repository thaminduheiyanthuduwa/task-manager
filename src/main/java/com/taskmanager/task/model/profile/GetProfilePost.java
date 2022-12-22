package com.taskmanager.task.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class GetProfilePost {

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("username")
    private String username;

    @JsonProperty("postTime")
    private String postTime;

    @JsonProperty("postText")
    private String postText;

    @JsonProperty("postImg")
    private String postImg;

    public GetProfilePost() {
    }
}
