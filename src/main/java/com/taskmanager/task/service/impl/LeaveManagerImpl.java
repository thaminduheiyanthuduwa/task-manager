package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.LeaveRepository;
import com.taskmanager.task.repository.TaskBreakdownRepository;
import com.taskmanager.task.repository.TaskListRepository;
import com.taskmanager.task.response.*;
import com.taskmanager.task.service.LeaveManager;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveManagerImpl implements LeaveManager {

    @Autowired
    private LeaveRepository leaveRepository;


    @Override
    public ResponseList createLeave(CreateLeave createLeave) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date createdDate = new Date();
        Date dueDate = new Date();

        try {
            createdDate = formatter.parse(createLeave.getStartDate());
            dueDate = formatter.parse(createLeave.getEndDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<LeaveEntity> leaveEntity = new ArrayList<>();

        LeaveEntity leaveEntity1 = new LeaveEntity();

        leaveEntity1.setEmpId(createLeave.getUserId());
        leaveEntity1.setLeaveType(createLeave.getLeaveType());
        leaveEntity1.setFromDate(createdDate);
        leaveEntity1.setToDate(dueDate);
        leaveEntity1.setTotalLeave(createLeave.getTotal());
        leaveEntity1.setStatus(1);
        leaveEntity1.setComment(createLeave.getDescription());
        leaveEntity1.setCreatedDate(new Date());

        leaveEntity.add(leaveEntity1);
        leaveRepository.saveAll(leaveEntity);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList getLeaveById(Integer id) {

        List<LeaveEntity> leaveById = leaveRepository.findByEmpIdOrderByIdDesc(id);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(leaveById);

        return responseList;
    }
}
