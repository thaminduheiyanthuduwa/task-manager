package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.Payroll.PayrollPdfInfoBasicObject;
import com.taskmanager.task.model.Payroll.PayrollPdfInfoDeductionObject;
import com.taskmanager.task.model.Payroll.PayrollPdfInfoEarningObject;
import com.taskmanager.task.model.Payroll.PayrollPdfInfoObject;
import com.taskmanager.task.repository.*;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.PayrollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseList updateWithAllSalaryInfoForMonth() {

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
            allSalaryInfoEntity.setMonth("Mar");
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


        List<PayrollEntityDetails> objList = new ArrayList<>();

        summery.forEach(payrollEntityDetails -> {

//            Integer value = attendanceRepository.getAttendanceOTAndLateById(payrollEntityDetails.getEmpId());
//
//            if (value == 3) {
//                payrollEntityDetails.setIsOt(1);
//                payrollEntityDetails.setIsLate(1);
//            }
//            else if (value == 2){
//                payrollEntityDetails.setIsOt(1);
//                payrollEntityDetails.setIsLate(0);
//            }
//            else if (value == 1){
//                payrollEntityDetails.setIsOt(0);
//                payrollEntityDetails.setIsLate(1);
//            }
//            else {
//                payrollEntityDetails.setIsOt(0);
//                payrollEntityDetails.setIsLate(0);
//            }
            payrollEntityDetails.setIsOt(8);
            payrollEntityDetails.setIsLate(8);

            objList.add(payrollEntityDetails);

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(objList);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList startMonthPeopleConfig(Integer id) {

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

    @Override
    public ResponseList getPayrollPdfInfo(Integer id) {

        EmpDetailEntity emp = empDetailRepository.findById(id).get();

        PayrollEntityDetails payroll = payrollDetailsRepository.findByEmpId(id).get(0);

        List<AllSalaryInfoEntity> salaryInfo = allSalaryInfoRepository.getAllSalaryInfoByName(emp.getNameInFull());

        PayrollPdfInfoObject payrollPdfInfoObject = new PayrollPdfInfoObject();

        payrollPdfInfoObject.setId(String.valueOf(id));
        payrollPdfInfoObject.setName(emp.getNameInFull());
        payrollPdfInfoObject.setEpfNo(emp.getEpfNumber());
        payrollPdfInfoObject.setDesignation(emp.getDesignation());
        payrollPdfInfoObject.setDesignation(emp.getDesignation());
        payrollPdfInfoObject.setDate("2023-May");

        List<PayrollPdfInfoEarningObject> list1 = new ArrayList<>();
        List<PayrollPdfInfoBasicObject> list3 = new ArrayList<>();
        List<PayrollPdfInfoDeductionObject> list2 = new ArrayList<>();

        salaryInfo.forEach(allSalaryInfoEntity -> {

            if ((allSalaryInfoEntity.getCategory()
                    .equalsIgnoreCase("Basic Salary") || allSalaryInfoEntity.getCategory()
                    .equalsIgnoreCase("Allowances")) && allSalaryInfoEntity.getAmount() != 0) {

                if (!(allSalaryInfoEntity.getType().equalsIgnoreCase("basic_salary") ||
                        allSalaryInfoEntity.getType().equalsIgnoreCase("Budgetary Allowance"))) {
                    PayrollPdfInfoEarningObject payrollPdfInfoEarningObject = new PayrollPdfInfoEarningObject();
                    payrollPdfInfoEarningObject.setTitle("Payroll");
                    payrollPdfInfoEarningObject.setEarnings(allSalaryInfoEntity.getType());
                    payrollPdfInfoEarningObject.setRate("");
                    payrollPdfInfoEarningObject.setHours("");
                    payrollPdfInfoEarningObject.setTotal(String.valueOf(allSalaryInfoEntity.getAmount()));
                    list1.add(payrollPdfInfoEarningObject);
                }
            }
            else if ((allSalaryInfoEntity.getCategory()
                    .equalsIgnoreCase("Deductions")) && allSalaryInfoEntity.getAmount() != 0) {

                PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
                payrollPdfInfoDeductionObject.setTitle("Payroll");
                payrollPdfInfoDeductionObject.setDeduction(allSalaryInfoEntity.getType());
                payrollPdfInfoDeductionObject.setRate("");
                payrollPdfInfoDeductionObject.setHours("");
                payrollPdfInfoDeductionObject.setTotal(String.valueOf(allSalaryInfoEntity.getAmount()));
                list2.add(payrollPdfInfoDeductionObject);
            }

        });


        PayrollPdfInfoBasicObject payrollPdfInfoBasicObject = new PayrollPdfInfoBasicObject();
        payrollPdfInfoBasicObject.setTitle("Payroll");
        payrollPdfInfoBasicObject.setBasic("Basic Salary");
        payrollPdfInfoBasicObject.setRate("");
        payrollPdfInfoBasicObject.setHours("");
        payrollPdfInfoBasicObject.setTotal(String.valueOf(payroll.getBasicSalary()));
        list3.add(payrollPdfInfoBasicObject);


        Float tmpNoPay = 0F;

        if (payroll.getTotalNoPay() > 0){

            tmpNoPay = payroll.getTotalNoPay();
            PayrollPdfInfoBasicObject payrollPdfInfoBasicObject2 = new PayrollPdfInfoBasicObject();
            payrollPdfInfoBasicObject2.setTitle("Payroll");
            payrollPdfInfoBasicObject2.setBasic("No Pay");
            payrollPdfInfoBasicObject2.setRate("");
            payrollPdfInfoBasicObject2.setHours("");
            payrollPdfInfoBasicObject2.setTotal(String.valueOf(payroll.getTotalNoPay()));
            list3.add(payrollPdfInfoBasicObject2);
        }


        PayrollPdfInfoBasicObject payrollPdfInfoBasicObject3 = new PayrollPdfInfoBasicObject();
        payrollPdfInfoBasicObject3.setTitle("Payroll");
        payrollPdfInfoBasicObject3.setBasic("Finalized Basic Salary");
        payrollPdfInfoBasicObject3.setRate("");
        payrollPdfInfoBasicObject3.setHours("");
        payrollPdfInfoBasicObject3.setTotal(String.valueOf(payroll.getBasicSalary()-payroll.getTotalNoPay()));
        list3.add(payrollPdfInfoBasicObject3);

        if (payroll.getTotalMorningLate() > 0){
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Morning Late");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getTotalMorningLate()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalMorningLate());
        }
        if (payroll.getTotalLateAmount() > 0){
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Late Amount");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getTotalLateAmount()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalLateAmount());
        }
        if (payroll.getPayee() > 0){
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Payee");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getPayee()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getPayee());
        }

        if (payroll.getPayee() > 0){
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Task Deduction");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getTotalDeductionForTasks()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalDeductionForTasks());
        }

        if (payroll.getEpfDeduction() > 0){
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("EPF Deduction");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getEpfDeduction()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getEpfDeduction());
        }

        if (payroll.getTotalOt() > 0){
            payroll.setGrossSalary(payroll.getGrossSalary() + payroll.getTotalOt());
            PayrollPdfInfoEarningObject payrollPdfInfoEarningObject = new PayrollPdfInfoEarningObject();
            payrollPdfInfoEarningObject.setTitle("Payroll");
            payrollPdfInfoEarningObject.setEarnings("OT Amount");
            payrollPdfInfoEarningObject.setRate("");
            payrollPdfInfoEarningObject.setHours("");
            list1.add(payrollPdfInfoEarningObject);
            payrollPdfInfoEarningObject.setTotal(String.valueOf(payroll.getTotalOt()));
        }
        payroll.setGrossSalary(payroll.getGrossSalary() - payroll.getTotalNoPay());
        payrollPdfInfoObject.setTotalAmount(String.valueOf(payroll.getGrossSalary() - payroll.getTotalDeductions()));

        payrollPdfInfoObject.setPayrollPdfInfoEarningObjectList(list1);
        payrollPdfInfoObject.setPayrollPdfInfoDeductionObjects(list2);
        payrollPdfInfoObject.setPayrollPdfInfoBasicObjects(list3);

        payrollPdfInfoObject.setPayrollEntityDetails(payroll);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(payrollPdfInfoObject);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList createPayRoll() {

        List<PayrollSummery> payRoll = payrollSummeryRepository.findAll();

        List<PayrollSummery> newList = payRoll.stream().filter(payrollSummery -> payrollSummery
                .getStatus() == 1 || payrollSummery.getStatus() == 3).toList();

        if (!newList.isEmpty()){
            ResponseList responseList = new ResponseList();
            responseList.setCode(400);
            responseList.setMsg("Already Created a payroll for this month");
            return responseList;
        }
        else {
            PayrollSummery payrollSummery = new PayrollSummery();
            payrollSummery.setProcessDate(new Date());
            payrollSummery.setStatus(1);
            payrollSummeryRepository.save(payrollSummery);

            updateWithAllSalaryInfoForMonth();
            startMonthPeopleConfig(1);

            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            return responseList;

        }

    }

    @Override
    public ResponseList changePayRollSummaryStatus(int status) {

        List<PayrollSummery> payRoll = payrollSummeryRepository.findAll();

        List<PayrollSummery> newList = payRoll.stream().filter(payrollSummery -> payrollSummery
                .getStatus() == 1 || payrollSummery.getStatus() == 3).toList();

        if (!newList.isEmpty()){

            if (status == 2){
                allSalaryInfoRepository.deleteAll();
                payrollDetailsRepository.deleteAll();
            }

            PayrollSummery obj = newList.get(0);
            obj.setStatus(status);
            payrollSummeryRepository.save(obj);
            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            return responseList;
        }
        else {
            ResponseList responseList = new ResponseList();
            responseList.setCode(400);
            return responseList;
        }
    }

    @Override
    public ResponseList getPayrollLeaveReportData(String start, String end) {


        List<Object> data = payrollSummeryRepository.getPayrollLeaveReportData();

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(data);
        return responseList;

    }


}
