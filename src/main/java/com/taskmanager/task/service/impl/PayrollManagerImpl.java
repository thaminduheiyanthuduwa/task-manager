package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.repository.*;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.PayrollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PayrollManagerImpl implements PayrollManager {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    AllSalaryInfoRepository allSalaryInfoRepository;


    @Autowired
    private EmpDetailRepository empDetailRepository;

    @Autowired
    private PayrollSummeryRepository payrollSummeryRepository;

    @Autowired
    private PayrollDetailsRepository payrollDetailsRepository;


    @Override
    public ResponseList updateWithAllSalaryInfoForMonth() throws ParseException {

        String url = "http://localhost:8080/main-erp/payroll/update-all-salary-info";

        RestTemplate restTemplate = new RestTemplate();

        List<EmpDetailEntity> currentEmp = empDetailRepository.findAll();

        ArrayList newList = ((ArrayList) restTemplate.getForObject(url, Map.class).get("data"));

        List<AllSalaryInfoEntity> allSalaryInfoEntities = new ArrayList<>();


        for (Object tempObj : newList) {

            AllSalaryInfoEntity allSalaryInfoEntity = new AllSalaryInfoEntity();

            LinkedHashMap<String, String> temp = (LinkedHashMap) tempObj;

            List<Integer> id = currentEmp.stream()
                    .filter(empDetailEntity -> (empDetailEntity
                            .getNameInFull() != null && empDetailEntity
                            .getNameInFull().equalsIgnoreCase(temp.get("name")))).map(EmpDetailEntity::getId).toList();

            allSalaryInfoEntity.setEmpId(id.isEmpty() ? -999 : id.get(0));
            allSalaryInfoEntity.setName(temp.get("name"));
            allSalaryInfoEntity.setCategory(temp.get("category"));
            allSalaryInfoEntity.setType(temp.get("type"));
            allSalaryInfoEntity.setAmount(Float.valueOf(temp.get("amount")));
            allSalaryInfoEntity.setMonth("Feb-23");
            allSalaryInfoEntities.add(allSalaryInfoEntity);

        }

        allSalaryInfoRepository.saveAll(allSalaryInfoEntities);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList getPayrollSummery() throws ParseException {

        List<PayrollSummery> summery = payrollSummeryRepository.findAll();

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(summery);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList getAllPeopleConfigInfo() throws ParseException {

        List<PayrollEntityDetails> summery = payrollDetailsRepository.findAll();

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(summery);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList startMonthPeopleConfig(Integer id) throws ParseException {

        List<EmpDetailEntity> peopleList = empDetailRepository.findAll();

        List<PayrollEntityDetails> list = new ArrayList<>();

        peopleList.forEach(empDetailEntity -> {

            if (true) {
                PayrollEntityDetails obj = new PayrollEntityDetails();

                List<AllSalaryInfoEntity> allSalaryInfo = allSalaryInfoRepository.getBasicSalaryInfoByName(empDetailEntity.getNameInFull());
                List<AllSalaryInfoEntity> grossSalaryInfo = allSalaryInfoRepository.getGrossSalaryInfoByName(empDetailEntity.getNameInFull());

                double sum = allSalaryInfo.stream().mapToDouble(AllSalaryInfoEntity::getAmount).sum();
                double grossSum = grossSalaryInfo.stream().mapToDouble(AllSalaryInfoEntity::getAmount).sum();

                obj.setPayrollId(id);
                obj.setEmpId(empDetailEntity.getId());
                obj.setSerialId(empDetailEntity.getSerialNumber());
                obj.setName(empDetailEntity.getNameInFull());
                obj.setIsNoPay(1);
                obj.setIsOt(0);
                obj.setAdvance(0F);
                obj.setIsActive(1);
                obj.setApplicableDates(30);
                obj.setIsOtBasic(1);
                obj.setIsNoPayBasic(1);
                obj.setBasicSalary((float) sum);
                obj.setGrossSalary((float) grossSum);
                list.add(obj);
            }
        });

        payrollDetailsRepository.saveAll(list);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList changeStatus(Integer id, Integer status) throws ParseException {

        Optional<PayrollEntityDetails> detail = payrollDetailsRepository.findById(id);

        PayrollEntityDetails obj = detail.get();

        if (status == 1){
            Integer val = obj.getIsOtBasic();
            obj.setIsOtBasic(val == 1 ? 0 : 1);
        }
        if (status == 2){
            Integer val = obj.getIsNoPayBasic();
            obj.setIsNoPayBasic(val == 1 ? 0 : 1);
        }
        if (status == 3){
            Integer val = obj.getNotApplicable();
            obj.setNotApplicable(val == 1 ? 0 : 1);
        }

        payrollDetailsRepository.save(obj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList changeAdvance(Integer id, Float advance) throws ParseException {

        Optional<PayrollEntityDetails> detail = payrollDetailsRepository.findById(id);

        PayrollEntityDetails obj = detail.get();

        obj.setAdvance(advance);

        payrollDetailsRepository.save(obj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList processOt() throws ParseException {



        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }


}
