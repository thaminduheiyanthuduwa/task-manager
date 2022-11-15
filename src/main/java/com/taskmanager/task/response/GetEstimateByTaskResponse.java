package com.taskmanager.task.response;

import com.taskmanager.task.entity.TaskBreakdownEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetEstimateByTaskResponse {

    private Double total;

    private List<TaskBreakdownEntity> taskBreakdownEntities;

    private String lastUpdatedTime  ;

    public GetEstimateByTaskResponse() {}

}
