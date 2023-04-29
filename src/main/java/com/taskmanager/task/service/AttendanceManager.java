package com.taskmanager.task.service;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;

public interface AttendanceManager {
    ResponseList getAttendanceByID(int id) throws ParseException;

    ResponseList getAttendanceByIDForMonth(int id);

    ResponseList getAttendanceByIDForApproval(int id, String date) throws ParseException;

    ResponseList getAllAttendanceDetails();

    ResponseList getAttendanceByAttendanceID(Integer id);

    ResponseList createAttendance(Integer id,CreateAttendance createAttendance);

    ResponseList updateWithYesterdayAttendance(int id);

    ResponseList updateWithYesterdayAttendanceForUserId(int id, String start, String end);

    ResponseList getChildListForSupervisor(int id);

    ResponseList getMinorStaffList(int id);

    ResponseList changeStatusForAttendance(int attendance,int user,int status, String comment);

    ResponseList getTodayInTime(int id) throws ParseException;

    ResponseList getAttendanceStat(int id) throws ParseException;

    ResponseList getTotalLeavesAndTakenLeaves(int id) throws ParseException;

    ResponseList getTotalPenalty(int id) throws ParseException;

    ResponseList updatePastMonthAttendance() throws ParseException;

    ResponseList changeStatus(Integer id, Integer status, String date) throws ParseException;

    ResponseList updateLeaveWithAttendance() throws ParseException;

    ResponseList updateRosterDatesWithAttendance() throws ParseException;

    ResponseList processOt(Integer id, Integer status) throws ParseException;

    ResponseList setPayRollStatus() throws ParseException;

    ResponseList getOTProcessDates();

}
