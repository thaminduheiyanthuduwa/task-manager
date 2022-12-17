package com.taskmanager.task.service.impl;

import com.taskmanager.task.repository.AttendanceRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceManagerImpl implements AttendanceManager {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public ResponseList getAttendanceByID(int id) {

        ResponseList responseList = new ResponseList();

        return responseList;
    }

    @Override
    public ResponseList getAllAttendanceDetails() {

        ResponseList responseList = new ResponseList();

        return responseList;
    }



}
