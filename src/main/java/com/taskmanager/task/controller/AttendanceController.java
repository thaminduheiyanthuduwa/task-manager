package com.taskmanager.task.controller;

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
    public ResponseList getTask(@RequestParam(value = "user_id") int id) {
        return attendanceManager.getAttendanceByID(id);

    }
}
