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

    ResponseList getTaskCountReportData(String start, String end);

    ResponseList getMorningLateReportData(String start, String end);

    ResponseList getPayrollReportInfo();

    ResponseList getBasicSalaryInfoById(Integer id);

    ResponseList getAllowanceSalaryInfoById(Integer id);

    ResponseList getDeductionsSalaryInfoById(Integer id);

    ResponseList updateSalaryById(Integer id, String category,
                                  String type, Float amount, String reason,
                                  Integer updatedUser, String additionType);

    ResponseList getSalaryForEachType(Integer id);

    ResponseList updateSalaryForEachType(Integer id, Integer status, Integer updatedUser);

    ResponseList updateMasterSalaryCategory(String category, String type, Integer updatedUser);

    ResponseList getMasterSalaryTypeByCategory(String category);

    ResponseList getMasterPayrollSettings(Integer id);

    ResponseList updateMasterPayrollSettings(Integer id, Integer status);


}
