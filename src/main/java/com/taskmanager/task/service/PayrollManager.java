package com.taskmanager.task.service;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;

public interface PayrollManager {

    ResponseList updateWithAllSalaryInfoForMonth() throws ParseException;

    ResponseList getPayrollSummery() throws ParseException;

    ResponseList getAllPeopleConfigInfo() throws ParseException;

    ResponseList startMonthPeopleConfig(Integer id) throws ParseException;

    ResponseList changeStatus(Integer id, Integer status) throws ParseException;

    ResponseList changeAdvance(Integer id, Float advance) throws ParseException;

    ResponseList processOt() throws ParseException;

}
