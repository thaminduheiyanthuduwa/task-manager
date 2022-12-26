package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.AttendanceEntity;
import com.taskmanager.task.entity.LeaveEntity;
import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.repository.AttendanceRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class AttendanceManagerImpl implements AttendanceManager {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public ResponseList getAttendanceByID(int id) {

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Attendance data find by id");
        responseList.setData(attendanceRepository.findById(id));
        return responseList;
    }

    @Override
    public ResponseList getAllAttendanceDetails() {

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Attendance All data");
        responseList.setData(attendanceRepository.findAll());

        return responseList;
    }

    public ResponseList createAttendance(CreateAttendance createAttendance) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date createdDate = new Date();
        Date approvedDate = new Date();
        
//        Date createdDate;
//        Date approvedDate;

        try {
            createdDate = formatter.parse(createAttendance.getDate());
            approvedDate = formatter.parse(createAttendance.getApprovedDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String getInTime = createAttendance.getInTime();
        String getOutTime = createAttendance.getOutTime();
        Date time1;
        Date time2;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        try {
            time1 = format.parse(getInTime);
            time2 = format.parse(getOutTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        long difference = time2.getTime() - time1.getTime();

        List<AttendanceEntity> attendanceEntities = new ArrayList<>();

        AttendanceEntity attendanceEntity = new AttendanceEntity();

        attendanceEntity.setEmpId(createAttendance.getEmpId());
        attendanceEntity.setInTime(createAttendance.getInTime());
        attendanceEntity.setDate(createdDate);
//        attendanceEntity.setDate(new Date());
        attendanceEntity.setApprovedDate(approvedDate);
        attendanceEntity.setOutTime(createAttendance.getOutTime());
        attendanceEntity.setWorkDuration(difference);
        attendanceEntity.setComment(createAttendance.getComment());
        attendanceEntity.setApprovedBy(createAttendance.getApprovedBy());

        attendanceEntities.add(attendanceEntity);
        attendanceRepository.saveAll(attendanceEntities);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }


}
