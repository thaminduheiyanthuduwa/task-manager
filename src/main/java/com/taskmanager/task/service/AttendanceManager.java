package com.taskmanager.task.service;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;

public interface AttendanceManager {
    ResponseList getAttendanceByID(int id) throws ParseException;

    ResponseList getAllAttendanceDetails();

    ResponseList getAttendanceByAttendanceID(Integer id);

    ResponseList createAttendance(Integer id,CreateAttendance createAttendance);

    ResponseList updateWithYesterdayAttendance();

    ResponseList getChildListForSupervisor(int id);

    ResponseList changeStatusForAttendance(int attendance,int user,int status, String comment);

    ResponseList getTodayInTime(int id) throws ParseException;

    ResponseList getAttendanceStat(int id) throws ParseException;

    ResponseList getTotalLeavesAndTakenLeaves(int id) throws ParseException;

    ResponseList getTotalPenalty(int id) throws ParseException;
}
