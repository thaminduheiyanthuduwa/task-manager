package com.taskmanager.task.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LastSevenDaySeriesObj {

    private String name;

    private List<Integer> date;

    public LastSevenDaySeriesObj() {}

}
