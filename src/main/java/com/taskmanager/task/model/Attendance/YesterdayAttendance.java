package com.taskmanager.task.model.Attendance;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class YesterdayAttendance {

    private Integer id;

    private Integer empId;

    private String date;

    public YesterdayAttendance() {
    }
}
