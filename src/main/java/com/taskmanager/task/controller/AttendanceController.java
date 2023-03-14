package com.taskmanager.task.controller;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    AttendanceManager attendanceManager;

    @RequestMapping(value = "/get-attendance-by-id", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendance(@RequestParam(value = "user_id") int id) throws ParseException {
        return attendanceManager.getAttendanceByID(id);

    }

    @RequestMapping(value = "/get-attendance-by-id-for-approval", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendanceForApproval(@RequestParam(value = "user_id") int id) throws ParseException {
        return attendanceManager.getAttendanceByIDForApproval(id);

    }

    @RequestMapping(value = "/get-attendance-by-attendance-id", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendanceByAttendanceId(@RequestParam(value = "id") int id) throws ParseException {
        return attendanceManager.getAttendanceByAttendanceID(id);

    }


    @RequestMapping(value = "/get-all-attendance", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAllAttendance() {
        return attendanceManager.getAllAttendanceDetails();

    }

    @RequestMapping(
            value = "/create_attendance/{id}",
            method = RequestMethod.POST,
            headers = "Accept=application/json")
    public ResponseList createTask(@PathVariable(value = "id") Integer id, @RequestBody CreateAttendance createAttendance) {
        return attendanceManager.createAttendance(id, createAttendance);

    }

    @RequestMapping(value = "/update-with-yesterday-attendance", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList updateWithYesterdayAttendanceData() {
        return attendanceManager.updateWithYesterdayAttendance();

    }

    @RequestMapping(value = "/get-child-for-supervisor", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendanceSummery(@RequestParam(value = "supervisor") int id) {

        return attendanceManager.getChildListForSupervisor(id);

    }

    @RequestMapping(value = "/change-status-attendance/{attendance}/{user}/{status}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList approveAttendance(@PathVariable(value = "attendance") Integer attendance,
                                          @PathVariable(value = "user") Integer user,
                                          @PathVariable(value = "status") Integer status,
                                          @RequestParam(value = "comment") String comment) {

        return attendanceManager.changeStatusForAttendance(attendance, user, status, comment);

    }

    @RequestMapping(value = "/get-today-in-time/{user}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getTodayInTime(@PathVariable(value = "user") int id) throws ParseException {
        return attendanceManager.getTodayInTime(id);

    }

    @RequestMapping(value = "/get-attendance-stat/{user}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendanceStat(@PathVariable(value = "user") int id) throws ParseException {
        return attendanceManager.getAttendanceStat(id);

    }

    @RequestMapping(value = "/get-total-taken-leaves/{user}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getTotalLeavesAndTakenLeaves(@PathVariable(value = "user") int id) throws ParseException {
        return attendanceManager.getTotalLeavesAndTakenLeaves(id);

    }

    @RequestMapping(value = "/get-penalty/{user}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getPenalty(@PathVariable(value = "user") int id) throws ParseException {
        return attendanceManager.getTotalPenalty(id);

    }

    @RequestMapping(value = "/update-last-month-attendance-with-ot-and-late", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList updateLastMonthData() throws ParseException {
        return attendanceManager.updatePastMonthAttendance();

    }

    @RequestMapping(value = "/update-status/{id}/{status}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList changeStatus(@PathVariable(value = "id") int id,
                                     @PathVariable(value = "status") int status) throws ParseException {
        return attendanceManager.changeStatus(id, status);

    }

    @RequestMapping(value = "/update-leave-with-attendance", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList updateLeaveWithAttendance() throws ParseException {
        return attendanceManager.updateLeaveWithAttendance();

    }

    @RequestMapping(value = "/update-roster-dates-with-attendance", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList updatesRosterDatesWithAttendance() throws ParseException {
        return attendanceManager.updateRosterDatesWithAttendance();

    }

    @RequestMapping(value = "/process-ot/{id}/{status}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList processOT(@PathVariable(value = "id") int id,
                                  @PathVariable(value = "status") int status) throws ParseException {
        return attendanceManager.processOt(id, status);

    }

    @RequestMapping(value = "/set-pay-roll-status", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList setPayRollStatus() throws ParseException {
        return attendanceManager.setPayRollStatus();

    }

    @RequestMapping(value = "/get-minor-staff-list", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getMinorStaffList(@RequestParam(value = "supervisor") int id) {

        return attendanceManager.getMinorStaffList(id);

    }
}
