package com.taskmanager.task.service;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;

public interface PenaltyManager {

    ResponseList getPenaltyByID(int id, int status) throws ParseException;

}
