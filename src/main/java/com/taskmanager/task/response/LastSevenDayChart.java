package com.taskmanager.task.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LastSevenDayChart {

    private List<LastSevenDaySeriesObj> series;


    public LastSevenDayChart() {}

}
