package com.taskmanager.task.model.Attendance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinorStaffAttendanceObject {

    private String id;

    private String name;

    private String date;

    private String inTime;

    private String outTime;

    private String totalWorkingHourInMin;

    private String totalWorkingHourInHour;

    private String minorStaffTpe;

    public MinorStaffAttendanceObject(String id, String name, String date, String inTime,
                                  String outTime, String totalWorkingHourInMin,
                                  String totalWorkingHourInHour, String minorStaffTpe) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
        this.totalWorkingHourInMin = totalWorkingHourInMin;
        this.totalWorkingHourInHour = totalWorkingHourInHour;
        this.minorStaffTpe = minorStaffTpe;
    }
}
