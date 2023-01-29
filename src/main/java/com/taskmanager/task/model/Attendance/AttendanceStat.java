package com.taskmanager.task.model.Attendance;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttendanceStat {

    String icon;

    String color;

    String title;

    String subtitle;

    String customClass;

    public AttendanceStat(String icon, String color, String title, String subtitle, String customClass) {
        this.icon = icon;
        this.color = color;
        this.title = title;
        this.subtitle = subtitle;
        this.customClass = customClass;
    }
}
