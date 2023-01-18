package com.taskmanager.task.controller;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.roster.CreateRoster;
import com.taskmanager.task.model.roster.UserRoster;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import com.taskmanager.task.service.RosterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roster")
@CrossOrigin(origins = "*")
public class RosterController {

    @Autowired
    RosterManager rosterManager;

    @RequestMapping(value = "/create_roster/{user}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList createMyRoster(@RequestBody CreateRoster createRoster, @PathVariable Integer user) throws ParseException {
        return rosterManager.addMyRoster(createRoster, user);

    }

    @RequestMapping(value = "/get-my-roster/{user}/{config}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getMyRoster(@PathVariable Integer user, @PathVariable String config ) throws ParseException {
        return rosterManager.getMyRoster(user);

    }

    @RequestMapping(value = "/get-my-roster-for-table/{user}/{status}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getMyRosterForTable(@PathVariable Integer user, @PathVariable Integer status ) throws ParseException {
        return rosterManager.getMyRosterForTable(user, status);

    }

    @RequestMapping(value = "/get-my-roster-for-id/{user}/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getMyRosterForId(@PathVariable Integer user, @PathVariable Integer id) throws ParseException {
        return rosterManager.getMyRosterForId(user, id);

    }

    @RequestMapping(value = "/get-user-list/{type}/{user}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getUserList(@PathVariable Integer type, @PathVariable Integer user ) throws ParseException {
        return rosterManager.getUserList(type, user);

    }

    @RequestMapping(value = "/request-change-user/{task}/{user}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList changeUser(@PathVariable Integer task, @PathVariable Integer user, @RequestBody List<UserRoster> userList, @RequestParam String reason ) throws ParseException {

        return rosterManager.requestChangeUser(task, user, userList, reason);

    }

    @RequestMapping(value = "/change-status/{task}/{user}/{status}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList changeUser(@PathVariable Integer task, @PathVariable Integer user, @PathVariable Integer status) throws ParseException {

        return rosterManager.changeStatus(task, user, status);

    }

    @RequestMapping(value = "/get-child-for-supervisor", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList login(@RequestParam(value = "supervisor") int id) {

        return rosterManager.getChildListForSupervisor(id);

    }


}
