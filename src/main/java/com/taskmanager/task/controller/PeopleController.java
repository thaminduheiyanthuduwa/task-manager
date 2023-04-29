package com.taskmanager.task.controller;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import com.taskmanager.task.service.PeopleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/people")
@CrossOrigin(origins = "*")
public class PeopleController {

    @Autowired
    PeopleManager peopleManager;

    @RequestMapping(value = "/update-people-table", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList updatePeopleTable() throws ParseException {
        return peopleManager.updatePeopleTable();

    }

}
