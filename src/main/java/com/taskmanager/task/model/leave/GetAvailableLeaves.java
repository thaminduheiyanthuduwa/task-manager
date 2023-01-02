package com.taskmanager.task.model.leave;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taskmanager.task.model.ObjectValues;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetAvailableLeaves {

    private ObjectValues leaveType;

    private Integer leaves;

    private Integer originalLeave;

    private ObjectValues remainingPercentage;

    private String colourForType;

    private String colourForAvailableLeaves;
}
