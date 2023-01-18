package com.taskmanager.task.service;

import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.roster.CreateRoster;
import com.taskmanager.task.model.roster.UserRoster;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;
import java.util.List;

public interface RosterManager {

    ResponseList addMyRoster(CreateRoster createRoster, Integer user) throws ParseException;

    ResponseList getMyRoster(Integer user) throws ParseException;

    ResponseList getMyRosterForTable(Integer user,Integer status) throws ParseException;

    ResponseList getMyRosterForId(Integer user,Integer id) throws ParseException;

    ResponseList getUserList(Integer type, Integer user) throws ParseException;

    ResponseList requestChangeUser(Integer task, Integer user, List<UserRoster> users, String reason) throws ParseException;

    ResponseList changeStatus(Integer task, Integer user, Integer status) throws ParseException;

    ResponseList getChildListForSupervisor(int id);


}
