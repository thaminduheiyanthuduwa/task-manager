package com.taskmanager.task.service;

import com.taskmanager.task.response.ResponseList;

public interface AttendanceManager {
    ResponseList getAttendanceByID(int id);
}
