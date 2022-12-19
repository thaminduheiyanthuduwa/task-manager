package com.taskmanager.task.service;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.response.ResponseList;

public interface AttendanceManager {
    ResponseList getAttendanceByID(int id);

    ResponseList getAllAttendanceDetails();

    ResponseList createAttendance(CreateAttendance createAttendance);
}
