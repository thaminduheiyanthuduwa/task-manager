package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.PenaltyEntity;
import com.taskmanager.task.model.penalty.PenaltyInfo;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.PenaltyRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.PenaltyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.*;


@Service
public class PenaltyManagerImpl implements PenaltyManager {

    @Autowired
    PenaltyRepository penaltyRepository;

    @Autowired
    private EmpDetailRepository empDetailRepository;

    @Override
    public ResponseList getPenaltyByID(int id, int status) throws ParseException {

        List<PenaltyEntity> objList = penaltyRepository.findByEmpId(id);

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(id);

        String name = emp.get().getNameInFull();

        List<PenaltyInfo> list = new ArrayList<>();

        for (PenaltyEntity obj : objList){

            if (status == 0){
                PenaltyInfo penaltyInfo = new PenaltyInfo();
                penaltyInfo.setId(obj.getId());
                penaltyInfo.setEmpId(obj.getEmpId());
                penaltyInfo.setEmpName(name);
                penaltyInfo.setPenaltyAmount(obj.getPenaltyAmount());
                penaltyInfo.setReason(obj.getReason());
                penaltyInfo.setApprovedDate(obj.getApprovedDate());
                penaltyInfo.setApprovedBy(obj.getApprovedBy());
                penaltyInfo.setStatus(obj.getStatus());
                list.add(penaltyInfo);
            }
            else if (obj.getStatus() == status){
                PenaltyInfo penaltyInfo = new PenaltyInfo();
                penaltyInfo.setId(obj.getId());
                penaltyInfo.setEmpId(obj.getEmpId());
                penaltyInfo.setEmpName(name);
                penaltyInfo.setPenaltyAmount(obj.getPenaltyAmount());
                penaltyInfo.setReason(obj.getReason());
                penaltyInfo.setApprovedDate(obj.getApprovedDate());
                penaltyInfo.setApprovedBy(obj.getApprovedBy());
                penaltyInfo.setStatus(obj.getStatus());
                list.add(penaltyInfo);
            }

        }


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(list);
        responseList.setMsg("Success");

        return responseList;
    }
}
