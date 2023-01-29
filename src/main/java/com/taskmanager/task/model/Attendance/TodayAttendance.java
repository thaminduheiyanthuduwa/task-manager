package com.taskmanager.task.model.Attendance;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodayAttendance {

    String inTime;

    String outTime;

    String msg;

    Boolean late;

}
