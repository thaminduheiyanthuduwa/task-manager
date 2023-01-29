package com.taskmanager.task.controller;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import com.taskmanager.task.service.PenaltyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/penalty")
@CrossOrigin(origins = "*")
public class PenaltyController {

    @Autowired
    PenaltyManager penaltyManager;

    @RequestMapping(value = "/get-penalty-by-id/{user}/{status}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendance(@PathVariable(value = "user") int id,
                                      @PathVariable(value = "status") int status) throws ParseException {
        return penaltyManager.getPenaltyByID(id, status);

    }


}
