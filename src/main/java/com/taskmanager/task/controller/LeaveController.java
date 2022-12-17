package com.taskmanager.task.controller;

import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.LeaveManager;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

import java.text.ParseException;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "*")
public class LeaveController {

    @Autowired
    LeaveManager leaveManager;


    @RequestMapping(value = "/create_leave", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList createTask(@RequestBody CreateLeave createLeave) {

        return leaveManager.createLeave(createLeave);

    }

    @RequestMapping(value = "/get_leave_by_id/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList createTask(@PathVariable Integer id) {

        return leaveManager.getLeaveById(id);

    }

}
