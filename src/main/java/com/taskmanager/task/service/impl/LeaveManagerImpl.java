package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.ObjectValues;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.model.leave.GetAvailableLeaves;
import com.taskmanager.task.repository.AvailableLeaveRepository;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.LeaveRepository;
import com.taskmanager.task.response.*;
import com.taskmanager.task.response.leave.LeaveBalance;
import com.taskmanager.task.response.leave.SupervisorLeaveList;
import com.taskmanager.task.service.LeaveManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LeaveManagerImpl implements LeaveManager {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmpDetailRepository empDetailRepository;

    @Autowired
    private AvailableLeaveRepository availableLeaveRepository;


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

        List<AvailableLeaveEntity> availableLeaves = availableLeaveRepository.findByEmpId(id);

        List<GetAvailableLeaves> leaveBalanceList = new ArrayList<>();

        for (AvailableLeaveEntity obj : availableLeaves){

            GetAvailableLeaves leaveBalance = new GetAvailableLeaves();
            ObjectValues objectValues1 = new ObjectValues();
            objectValues1.setObject2(obj.getType());
            objectValues1.setObject3(getPercentageColour(obj.getType()));
            leaveBalance.setLeaveType(objectValues1);
            leaveBalance.setLeaves(obj.getAvailableLeaves());
            leaveBalance.setOriginalLeave(obj.getOriginalLeaves());
            Float remainingBalance = 0F;
            try {
                remainingBalance = (float) ((obj.getAvailableLeaves() * 100) / obj.getOriginalLeaves() );
            }
            catch (Exception e){}

            ObjectValues objectValues2 = new ObjectValues();
            objectValues2.setObject1(remainingBalance);
            if (remainingBalance > 75)
                objectValues2.setObject2("success");
            else if (remainingBalance > 50)
                objectValues2.setObject2("primary");
            else if (remainingBalance > 25)
                objectValues2.setObject2("warning");
            else
                objectValues2.setObject2("danger");

            leaveBalance.setRemainingPercentage(objectValues2);
            leaveBalanceList.add(leaveBalance);


        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(leaveBalanceList);

        return responseList;

    }

    @Override
    @Transactional
    public ResponseList createLeave(Integer id, CreateLeave createLeave) {

        List<AvailableLeaveEntity> availableLeaves = availableLeaveRepository.findByEmpId(id);

        AvailableLeaveEntity updateObj = null;

        for (AvailableLeaveEntity obj : availableLeaves){

            if (createLeave.getLeaveType().equalsIgnoreCase(obj.getType())){
                updateObj = obj;
            }


        }

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

        if (updateObj == null){
            responseList.setCode(201);
            responseList.setMsg("You are not allowed to use "+createLeave.getLeaveType()+" leaves");
        }
        else if (updateObj.getAvailableLeaves() < createLeave.getTotal()){
            responseList.setCode(201);
            responseList.setMsg("There are only "+updateObj.getAvailableLeaves()+" available "+createLeave.getLeaveType()+" leaves");
        }
        else if ((dueDate.getTime() - createdDate.getTime()) < 0){
            responseList.setCode(201);
            responseList.setMsg("To date must be greater then From Date");
        }
        else if ((dueDate.getTime() - createdDate.getTime())/(60 * 60 * 24 * 1000) != (createLeave.getTotal()-1)){
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

            Integer newLeaves = updateObj.getAvailableLeaves() - createLeave.getTotal();
            updateObj.setAvailableLeaves(newLeaves);
            availableLeaveRepository.save(updateObj);
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

    private String getPercentageColour(String type){
        if (type.equalsIgnoreCase("Annual") || type.equalsIgnoreCase("Probation Half Day")){
            return "success";
        }
        else if (type.equalsIgnoreCase("Casual") || type.equalsIgnoreCase("Short Leave")){
            return "primary";
        }
        else if (type.equalsIgnoreCase("Lieu Leave") || type.equalsIgnoreCase("Minor Staff Monthly")){
            return "warning";
        }
        else if (type.equalsIgnoreCase("Special") || type.equalsIgnoreCase("Cleaning Staff")){
            return "danger";
        }
        else {
            return "success";
        }
    }
}
