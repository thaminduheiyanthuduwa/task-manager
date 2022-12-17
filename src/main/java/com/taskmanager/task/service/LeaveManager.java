package com.taskmanager.task.service;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;


public interface LeaveManager {

    ResponseList createLeave(CreateLeave createLeave);

    ResponseList getLeaveById(Integer createLeave);


}
