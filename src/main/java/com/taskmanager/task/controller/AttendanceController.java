package com.taskmanager.task.controller;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    AttendanceManager attendanceManager;

    @RequestMapping(value = "/get-attendance-by-id", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendance(@RequestParam(value = "user_id") int id) {
        return attendanceManager.getAttendanceByID(id);

    }

    @RequestMapping(value = "/get-all-attendance", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAllAttendance() {
        return attendanceManager.getAllAttendanceDetails();

    }
    @RequestMapping(
            value = "/create_attendance",
            method = RequestMethod.POST,
            headers = "Accept=application/json")
    public ResponseList createTask(@RequestBody CreateAttendance createAttendance) {
        return attendanceManager.createAttendance(createAttendance);

    }
}
