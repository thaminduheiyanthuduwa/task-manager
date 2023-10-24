package com.taskmanager.task.controller;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import com.taskmanager.task.service.PayrollManager;
import com.taskmanager.task.service.impl.PayrollManagerImpl;
import lombok.NonNull;
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
    public ResponseList getAttendance() {
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
    public ResponseList startMonthPeopleConfig() {
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

    @RequestMapping(value = "/get-payroll-pdf-info/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getPayrollPdfInfo(@PathVariable(value = "id") Integer id) {
        return payrollManager.getPayrollPdfInfo(id);

    }

    @RequestMapping(value = "/create-payroll-summary", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList createPayrollSummary() {
        return payrollManager.createPayRoll();

    }

    @RequestMapping(value = "/change-payroll-summary-status/{status}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList changePayrollSummeryStatus(@PathVariable(value = "status") int status) {
        return payrollManager.changePayRollSummaryStatus(status);

    }


    @RequestMapping(value = "/get-payroll-leave-report-data/{start}/{end}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getPayrollLeaveReportData(@PathVariable String start, @PathVariable String end) {
        return payrollManager.getPayrollLeaveReportData(start, end);

    }

    @RequestMapping(value = "/get-payroll-task-report-data/{start}/{end}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getPayrollTaskReportData(@PathVariable String start, @PathVariable String end) {
        return payrollManager.getTaskCountReportData(start, end);

    }

    @RequestMapping(value = "/get-morning-late-amount/{start}/{end}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getMorningLateReportData(@PathVariable String start, @PathVariable String end) {
        return payrollManager.getMorningLateReportData(start, end);

    }

    @RequestMapping(value = "/get-payroll-report-info", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getPayrollReportInfo() {
        return payrollManager.getPayrollReportInfo();

    }

    @RequestMapping(value = "/get-basic-salary-info-by-id/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getBasicSalaryInfoById(@PathVariable Integer id) {
        return payrollManager.getBasicSalaryInfoById(id);

    }

    @RequestMapping(value = "/get-allowance-salary-info-by-id/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAllowanceSalaryInfoById(@PathVariable Integer id) {
        return payrollManager.getAllowanceSalaryInfoById(id);

    }

    @RequestMapping(value = "/get-deduction-salary-info-by-id/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getDeductionSalaryInfoById(@PathVariable Integer id) {
        return payrollManager.getDeductionsSalaryInfoById(id);
    }

    @RequestMapping(value = "/update-salary-by-id/{id}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList updateSalaryById(@PathVariable @NonNull Integer id,
                                         @RequestParam @NonNull String category,
                                         @RequestParam @NonNull String type,
                                         @RequestParam @NonNull Float amount,
                                         @RequestParam @NonNull String reason,
                                         @RequestParam(value = "updated_user") @NonNull Integer updatedUser,
                                         @RequestParam(value = "addition_type") @NonNull String additionType) {
        return payrollManager.updateSalaryById(id, category, type, amount, reason, updatedUser, additionType);

    }

    @RequestMapping(value = "/get-salary-for-each-type/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getSalaryForEachType(@PathVariable Integer id) {
        return payrollManager.getSalaryForEachType(id);

    }

    @RequestMapping(value = "/update-salary-for-each-type/{id}/{status}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList updateSalaryForEachType(@PathVariable Integer id, @PathVariable Integer status, @RequestParam(value = "updated_user") @NonNull Integer updatedUser) {
        return payrollManager.updateSalaryForEachType(id, status, updatedUser);

    }

    @RequestMapping(value = "/update-master-salary-category", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList updateMasterSalaryCategory(@RequestParam @NonNull String category,
                                                   @RequestParam @NonNull String type,
                                                   @RequestParam(value = "updated_user") @NonNull Integer updatedUser) {
        return payrollManager.updateMasterSalaryCategory(category, type, updatedUser);

    }

    @RequestMapping(value = "/get-master-salary-type/{category}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getMasterSalaryTypeByCategory(@PathVariable String category) {
        return payrollManager.getMasterSalaryTypeByCategory(category);

    }

    @RequestMapping(value = "/get-master-payroll-setting/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getMasterPayrollSettings(@PathVariable Integer id) {
        return payrollManager.getMasterPayrollSettings(id);

    }

    @RequestMapping(value = "/update-master-payroll-setting/{id}/{status}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList updateMasterPayrollSettings(@PathVariable Integer id, @PathVariable Integer status) {
        return payrollManager.updateMasterPayrollSettings(id, status);

    }
}
