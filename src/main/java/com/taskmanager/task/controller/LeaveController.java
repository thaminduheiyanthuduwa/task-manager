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

    @RequestMapping(value = "/get_available_leaves/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAvailableLeaves(@PathVariable Integer id) {

        return leaveManager.getAvailableLeaves(id);

    }

    @RequestMapping(value = "/create_leave/{id}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList createLeave(@PathVariable Integer id,@RequestBody CreateLeave createLeave) {

        return leaveManager.createLeave(id, createLeave);

    }

    @RequestMapping(value = "/get-child-for-supervisor", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList login(@RequestParam(value = "supervisor") int id) {

        return leaveManager.getChildListForSupervisor(id);

    }

    @RequestMapping(value = "/update-status/{id}/{status}/{user}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList updateStatus(@PathVariable Integer id,
                                     @PathVariable Integer status,
                                     @PathVariable Integer user) {

        return leaveManager.updateStatus(id, status, user);

    }

}
