package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.LeaveRepository;
import com.taskmanager.task.response.*;
import com.taskmanager.task.response.leave.LeaveBalance;
import com.taskmanager.task.response.leave.SupervisorLeaveList;
import com.taskmanager.task.service.LeaveManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LeaveManagerImpl implements LeaveManager {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmpDetailRepository empDetailRepository;


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

    @Override
    public ResponseList getAvailableLeaves(Integer id) {

        List<LeaveBalance> leaveBalanceList = new ArrayList<>();

        LeaveBalance leaveBalance1 = new LeaveBalance();
        leaveBalance1.setLeaveType("Annual");
        leaveBalance1.setLeaves(5);
        leaveBalanceList.add(leaveBalance1);

        LeaveBalance leaveBalance2 = new LeaveBalance();
        leaveBalance2.setLeaveType("Casual");
        leaveBalance2.setLeaves(3);
        leaveBalanceList.add(leaveBalance2);

        LeaveBalance leaveBalance3 = new LeaveBalance();
        leaveBalance3.setLeaveType("Special");
        leaveBalance3.setLeaves(2);
        leaveBalanceList.add(leaveBalance3);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(leaveBalanceList);

        return responseList;

    }

    @Override
    public ResponseList createLeave(Integer id, CreateLeave createLeave) {

        List<LeaveEntity> leaveById = leaveRepository.findByEmpIdOrderByIdDesc(id);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date createdDate = new Date();
        Date dueDate = new Date();

        try {
            createdDate = formatter.parse(createLeave.getStartDate());
            dueDate = formatter.parse(createLeave.getEndDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ResponseList responseList = new ResponseList();


        if ((dueDate.getTime() - createdDate.getTime()) < 0){
            responseList.setCode(201);
            responseList.setMsg("To date must be greater then From Date");
        }
        else if ((dueDate.getTime() - createdDate.getTime())/(60 * 60 * 24 * 1000) != createLeave.getTotal()){
            responseList.setCode(201);
            responseList.setMsg("Total Leave must be equal to date different");
        }
        else {
            LeaveEntity leaveEntity = new LeaveEntity();
            leaveEntity.setEmpId(id);
            leaveEntity.setLeaveType(createLeave.getLeaveType());
            leaveEntity.setFromDate(createdDate);
            leaveEntity.setToDate(dueDate);
            leaveEntity.setTotalLeave(createLeave.getTotal());
            leaveEntity.setStatus(1);
            leaveEntity.setComment(createLeave.getDescription());
            leaveEntity.setCreatedDate(new Date());
            leaveRepository.save(leaveEntity);
            responseList.setCode(200);
            responseList.setMsg("Success");
        }

        return responseList;

    }

    @Override
    public ResponseList getChildListForSupervisor(int id) {

        List<EmpDetailEntity> list = empDetailRepository.findBySupervisor(id);

        List<SupervisorLeaveList> supervisorLists = new ArrayList<>();

        list.forEach(empDetailEntity -> {

            SupervisorLeaveList supervisorList = new SupervisorLeaveList();
            supervisorList.setId(empDetailEntity.getId());
            supervisorList.setEmail(empDetailEntity.getEmail());
            supervisorList.setNicNo(empDetailEntity.getNicNo());
            supervisorList.setGivenName(empDetailEntity.getGivenName());
            supervisorList.setNameInFull(empDetailEntity.getNameInFull());
            supervisorList.setContactNo(empDetailEntity.getContactNo());
            supervisorList.setContactNo(empDetailEntity.getContactNo());
            supervisorList.setSupervisor(empDetailEntity.getSupervisor());

            supervisorList.setDeleteRequested(leaveRepository.getDeleteRequestedCount(empDetailEntity.getId()));
            supervisorList.setPendingLeave(leaveRepository.getPendingRequestedCount(empDetailEntity.getId()));

            supervisorLists.add(supervisorList);

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(supervisorLists);

        return responseList;

    }

    @Override
    public ResponseList updateStatus(int id, int status, int user) {

        Optional<LeaveEntity> leaveById = leaveRepository.findById(id);

        LeaveEntity obj = leaveById.get();

        obj.setStatus(status);
        if (status == 5){
            obj.setApprovedBy(user);
            obj.setApprovedDate(new Date());
        }

        leaveRepository.save(obj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }
}
