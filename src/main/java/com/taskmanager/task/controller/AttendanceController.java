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
}
