package com.taskmanager.task.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Builder
@Getter
@Setter
public class CreateProfilePost {

    @JsonProperty("title")
    private String title;

    @JsonProperty("category")
    private List<String> category;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("status")
    private String status;

    @JsonProperty("content")
    private String content;

    @JsonProperty("featured_image")
    private File featuredImage;

}
