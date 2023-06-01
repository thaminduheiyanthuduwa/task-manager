package com.taskmanager.task.service;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;

public interface PayrollManager {

    ResponseList updateWithAllSalaryInfoForMonth();

    ResponseList getPayrollSummery() throws ParseException;

    ResponseList getAllPeopleConfigInfo() throws ParseException;

    ResponseList startMonthPeopleConfig(Integer id);

    ResponseList changeStatus(Integer id, Integer status) throws ParseException;

    ResponseList changeAdvance(Integer id, Float advance) throws ParseException;

    ResponseList processOt() throws ParseException;

    ResponseList getPayrollPdfInfo(Integer id);

    ResponseList createPayRoll();

    ResponseList changePayRollSummaryStatus(int status);

    ResponseList getPayrollLeaveReportData(String start, String end);
    ResponseList getMorningLateReportData(String start, String end);


}
