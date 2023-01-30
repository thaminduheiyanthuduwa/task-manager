package com.taskmanager.task.model.leave;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateLeave {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("leave_type")
    private String leaveType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("total_leaves")
    private Float total;

}
