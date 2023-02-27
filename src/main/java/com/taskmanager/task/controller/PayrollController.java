package com.taskmanager.task.controller;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import com.taskmanager.task.service.PayrollManager;
import com.taskmanager.task.service.impl.PayrollManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/payroll")
@CrossOrigin(origins = "*")
public class PayrollController {

    @Autowired
    PayrollManager payrollManager;

    @RequestMapping(value = "/get-attendance-by-id", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendance() throws ParseException {
        return payrollManager.updateWithAllSalaryInfoForMonth();

    }

    @RequestMapping(value = "/get-payroll-summery", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getPayrollSummery() throws ParseException {
        return payrollManager.getPayrollSummery();

    }

    @RequestMapping(value = "/payroll-people-config-get-all", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAllPeopleConfigInfo() throws ParseException {
        return payrollManager.getAllPeopleConfigInfo();

    }

    @RequestMapping(value = "/start-month-people-config", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList startMonthPeopleConfig() throws ParseException {
        return payrollManager.startMonthPeopleConfig(1);

    }

    @RequestMapping(value = "/update-status/{id}/{status}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList changeStatus(@PathVariable(value = "id") int id,
                                     @PathVariable(value = "status") int status) throws ParseException {
        return payrollManager.changeStatus(id, status);

    }

    @RequestMapping(value = "/add-advance/{id}/{advance}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList addAdvance(@PathVariable(value = "id") int id,
                                     @PathVariable(value = "advance") float advance) throws ParseException {
        return payrollManager.changeAdvance(id, advance);

    }

    @RequestMapping(value = "/process_ot", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList processOt() throws ParseException {
        return payrollManager.processOt();

    }

}
