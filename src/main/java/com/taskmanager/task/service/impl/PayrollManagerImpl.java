package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.Attendance.MinorStaffAttendanceObject;
import com.taskmanager.task.model.Payroll.*;
import com.taskmanager.task.repository.*;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.response.leave.SupervisorLeaveList;
import com.taskmanager.task.service.PayrollManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private AllSalaryInfoBreakdownRepository allSalaryInfoBreakdownRepository;

    @Autowired
    private SalaryInfoMasterCategoryRepository salaryInfoMasterCategoryRepository;

    @Autowired
    private PayrollSettingRepository payrollSettingRepository;


    @Override
    public ResponseList updateWithAllSalaryInfoForMonth() {

        String url = "http://localhost:8085/main-erp/payroll/update-all-salary-info";

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

        String filePath = "path/to/your/new_one.txt";
        String content = "";

        int x = 0;
        for (AllSalaryInfoEntity obj : allSalaryInfoEntities) {
            content += "(" + (obj.getId()) + "," + (obj.getEmpId() != null ? obj.getEmpId() : " ") + ",'" + (obj.getName() != null ? obj.getName() : " ") + "','" + (obj.getCategory() != null ? obj.getCategory() : " ") + "','" + (obj.getType() != null ? obj.getType() : " ") + "'," + (obj.getAmount() != null ? obj.getAmount() : " ") + ",'" + (obj.getMonth() != null ? obj.getMonth() : " ") + "'),\n";
            //allSalaryInfoRepository.save(obj);
            x++;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }


//        allSalaryInfoRepository.saveAll(allSalaryInfoEntities);

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

        List<PayrollEntityDetails> summery = payrollDetailsRepository.getPayrollDetailsByIdList();


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

                List<AllSalaryInfoEntity> allSalaryInfo = allSalaryInfoRepository.getBasicSalaryInfoByEmpId(empDetailEntity.getId());
                List<AllSalaryInfoEntity> grossSalaryInfo = allSalaryInfoRepository.getGrossSalaryInfoByEmpId(empDetailEntity.getId());

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

        if (status == 1) {
            Integer val = obj.getIsOtBasic();
            obj.setIsOtBasic(val == 1 ? 0 : 1);
        }
        if (status == 2) {
            Integer val = obj.getIsNoPayBasic();
            obj.setIsNoPayBasic(val == 1 ? 0 : 1);
        }
        if (status == 3) {
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

        List<AllSalaryInfoEntity> salaryInfo = allSalaryInfoRepository.getAllSalaryInfoByEmpId(emp.getId());

        PayrollPdfInfoObject payrollPdfInfoObject = new PayrollPdfInfoObject();

        payrollPdfInfoObject.setId(String.valueOf(id));
        payrollPdfInfoObject.setName(emp.getNameInFull());
        payrollPdfInfoObject.setEpfNo(emp.getEpfNumber());
        payrollPdfInfoObject.setDesignation(emp.getDesignation());
        payrollPdfInfoObject.setDesignation(emp.getDesignation());
        payrollPdfInfoObject.setDate("2023-Sep");

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
            } else if ((allSalaryInfoEntity.getCategory()
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

        if (payroll.getTotalNoPay() != null && payroll.getTotalNoPay() > 0) {

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
        payrollPdfInfoBasicObject3.setTotal(String.valueOf(payroll.getBasicSalary() - ((payroll.getTotalNoPay() != null) ? payroll.getTotalNoPay() : 0F)));
        list3.add(payrollPdfInfoBasicObject3);

        if (payroll.getTotalMorningLate() > 0) {
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Morning Late");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getTotalMorningLate()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalMorningLate());
        }
        if (payroll.getTotalLateAmount() > 0) {
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Late Amount");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getTotalLateAmount()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalLateAmount());
        }
        if (payroll.getPayee() > 0) {
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Payee");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getPayee()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getPayee());
        }

        if (payroll.getTotalDeductionForTasks() > 0) {
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("Task Deduction");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getTotalDeductionForTasks()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalDeductionForTasks());
        }

        if (payroll.getEpfDeduction() > 0) {
            PayrollPdfInfoDeductionObject payrollPdfInfoDeductionObject = new PayrollPdfInfoDeductionObject();
            payrollPdfInfoDeductionObject.setTitle("Payroll");
            payrollPdfInfoDeductionObject.setDeduction("EPF Deduction");
            payrollPdfInfoDeductionObject.setRate("");
            payrollPdfInfoDeductionObject.setHours("");
            payrollPdfInfoDeductionObject.setTotal(String.valueOf(payroll.getEpfDeduction()));
            list2.add(payrollPdfInfoDeductionObject);
            payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getEpfDeduction());
        }

        if (payroll.getTotalOt() > 0) {
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

        if (!newList.isEmpty()) {
            ResponseList responseList = new ResponseList();
            responseList.setCode(400);
            responseList.setMsg("Already Created a payroll for this month");
            return responseList;
        } else {
            PayrollSummery payrollSummery = new PayrollSummery();
            payrollSummery.setProcessDate(new Date());
            payrollSummery.setStatus(1);
            payrollSummeryRepository.save(payrollSummery);

            //updateWithAllSalaryInfoForMonth();
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

        if (!newList.isEmpty()) {

            if (status == 2) {
//                allSalaryInfoRepository.deleteAll();
                payrollDetailsRepository.deleteAll();
            }

            PayrollSummery obj = newList.get(0);
            obj.setStatus(status);
            payrollSummeryRepository.save(obj);
            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            return responseList;
        } else {
            ResponseList responseList = new ResponseList();
            responseList.setCode(400);
            return responseList;
        }
    }

    @Override
    public ResponseList getPayrollLeaveReportData(String start, String end) {

        List<AllLeaveReportObject> list = new ArrayList<>();

        List<Object> dataList = payrollSummeryRepository.getPayrollLeaveReportData();

        for (Object obj : dataList) {

            Object[] data = ((Object[]) obj);

            AllLeaveReportObject tmpObj = new AllLeaveReportObject(String.valueOf(data[0]),
                    String.valueOf(data[1]), String.valueOf(data[2]),
                    String.valueOf(data[3]), String.valueOf(data[4]), String.valueOf(data[5]),
                    String.valueOf(data[6]), String.valueOf(data[7]),
                    String.valueOf(data[8]), String.valueOf(data[9]), String.valueOf(data[10]),
                    String.valueOf(data[11]), String.valueOf(data[12]), String.valueOf(data[13]));

            list.add(tmpObj);

        }


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(list);
        return responseList;

    }

    @Override
    public ResponseList getTaskCountReportData(String start, String end) {

        List<AllTaskReportObject> list = new ArrayList<>();

        List<Object> dataList = payrollSummeryRepository.getTaskReportData();

        for (Object obj : dataList) {

            Object[] data = ((Object[]) obj);

            AllTaskReportObject tmpObj = new AllTaskReportObject(String.valueOf(data[0]),
                    String.valueOf(data[1]), String.valueOf(data[2]),
                    String.valueOf(data[3]), String.valueOf(data[4]), String.valueOf(data[5]),
                    String.valueOf(data[6]), String.valueOf(data[7]));

            list.add(tmpObj);

        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(list);
        return responseList;
    }

    @Override
    public ResponseList getMorningLateReportData(String start, String end) {

        List<MorningLateReportObject> list = new ArrayList<>();

        List<Object> dataList = payrollSummeryRepository.getMorningLateReportData();

        for (Object obj : dataList) {

            Object[] data = ((Object[]) obj);

            MorningLateReportObject tmpObj = new MorningLateReportObject(String.valueOf(data[0]),
                    String.valueOf(data[1]), String.valueOf(data[2]),
                    String.valueOf(data[3]), String.valueOf(data[4]), String.valueOf(data[5]),
                    String.valueOf(data[6]));

            list.add(tmpObj);

        }


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(list);
        return responseList;

    }

    @Override
    public ResponseList getPayrollReportInfo() {

        List<PayrollReportInfoObject> payrollReportInfoObjectsList = new ArrayList<>();


        List<EmpDetailEntity> emp = empDetailRepository.getEmpDetailListById();

        emp.forEach(empDetailEntity -> {

            if (!(empDetailEntity.getHrEmployeeStatus().equalsIgnoreCase("RESIGNED") ||
                    empDetailEntity.getHrEmployeeStatus().equalsIgnoreCase("TERMINATED"))) {

                PayrollReportInfoObject payrollReportInfoObject = new PayrollReportInfoObject();

                List<PayrollEntityDetails> payrollList = payrollDetailsRepository.findByEmpId(empDetailEntity.getId());

                if (!payrollList.isEmpty()) {
                    PayrollEntityDetails payroll = payrollList.get(0);

                    List<AllSalaryInfoEntity> salaryInfo = allSalaryInfoRepository.getAllSalaryInfoByEmpId(empDetailEntity.getId());

                    payrollReportInfoObject.setId(String.valueOf(empDetailEntity.getId()));
                    payrollReportInfoObject.setName(empDetailEntity.getNameInFull());
                    payrollReportInfoObject.setEpfNo(empDetailEntity.getEpfNumber());
                    payrollReportInfoObject.setDesignation(empDetailEntity.getDesignation());
                    payrollReportInfoObject.setDesignation(empDetailEntity.getDesignation());
                    payrollReportInfoObject.setDate("2023-Sep");

                    salaryInfo.forEach(allSalaryInfoEntity -> {

                        if ((allSalaryInfoEntity.getCategory()
                                .equalsIgnoreCase("Basic Salary") || allSalaryInfoEntity.getCategory()
                                .equalsIgnoreCase("Allowances")) && allSalaryInfoEntity.getAmount() != 0) {

                            if (!(allSalaryInfoEntity.getType().equalsIgnoreCase("basic_salary") ||
                                    allSalaryInfoEntity.getType().equalsIgnoreCase("Budgetary Allowance"))) {

                                if (allSalaryInfoEntity.getType().equalsIgnoreCase("Academic Incentive Payment")) {
                                    payrollReportInfoObject.setAcademic_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Attendance Incentive Payment")) {
                                    payrollReportInfoObject.setAttendance_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Crisis Allowance")) {
                                    payrollReportInfoObject.setCrisis_Allowance(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Dean Incentive Payment")) {
                                    payrollReportInfoObject.setDean_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Fixed OT")) {
                                    payrollReportInfoObject.setFixed_OT(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Fixed Service Charges")) {
                                    payrollReportInfoObject.setFixed_Service_Charges(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("HOD Incentive Payment")) {
                                    payrollReportInfoObject.setHOD_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Insurance Premium")) {
                                    payrollReportInfoObject.setInsurance_Premium(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Leadership Incentive  Payment")) {
                                    payrollReportInfoObject.setLeadership_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Neatness Incentive Payment")) {
                                    payrollReportInfoObject.setNeatness_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Performance Incentive Payment")) {
                                    payrollReportInfoObject.setPerformance_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Rent Reimbursement")) {
                                    payrollReportInfoObject.setRent_Reimbursement(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Research Incentive Payment")) {
                                    payrollReportInfoObject.setResearch_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Service Charges")) {
                                    payrollReportInfoObject.setService_Charges(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Travelling Allowance")) {
                                    payrollReportInfoObject.setTravelling_Allowance(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("MSc Qualification Incentive")) {
                                    payrollReportInfoObject.setMSc_Qualification_Incentive(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Crisis_Allowances _2022")) {
                                    payrollReportInfoObject.setCrisis_Allowances_2022(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("DVC Incentive Allowance")) {
                                    payrollReportInfoObject.setDVC_Incentive_Allowance(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("MSC_Phd_Incentive")) {
                                    payrollReportInfoObject.setMSC_Phd_Incentive(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Performance _2022")) {
                                    payrollReportInfoObject.setPerformance_2022(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Travellening Allo 22")) {
                                    payrollReportInfoObject.setTravelling_Allo_22(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Task Incentive Payment")) {
                                    payrollReportInfoObject.setTask_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Advances")) {
                                    payrollReportInfoObject.setAdvances(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("swa")) {
                                    payrollReportInfoObject.setSwa(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Other Deductions")) {
                                    payrollReportInfoObject.setOtherDeduction(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Quarter Performance  Incentive Payment")) {
                                    payrollReportInfoObject.setQuarter_performance_incentive_payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                                } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Promotional Allowance")) {
                                    payrollReportInfoObject.setPromotional_allowance(String.valueOf(allSalaryInfoEntity.getAmount()));
                                }
                            }
                        } else if ((allSalaryInfoEntity.getCategory()
                                .equalsIgnoreCase("Deductions")) && allSalaryInfoEntity.getAmount() != 0) {

                            if (allSalaryInfoEntity.getType().equalsIgnoreCase("Academic Incentive Payment")) {
                                payrollReportInfoObject.setAcademic_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Attendance Incentive Payment")) {
                                payrollReportInfoObject.setAttendance_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Crisis Allowance")) {
                                payrollReportInfoObject.setCrisis_Allowance(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Dean Incentive Payment")) {
                                payrollReportInfoObject.setDean_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Fixed OT")) {
                                payrollReportInfoObject.setFixed_OT(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Fixed Service Charges")) {
                                payrollReportInfoObject.setFixed_Service_Charges(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("HOD Incentive Payment")) {
                                payrollReportInfoObject.setHOD_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Insurance Premium")) {
                                payrollReportInfoObject.setInsurance_Premium(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Leadership Incentive  Payment")) {
                                payrollReportInfoObject.setLeadership_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Neatness Incentive Payment")) {
                                payrollReportInfoObject.setNeatness_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Performance Incentive Payment")) {
                                payrollReportInfoObject.setPerformance_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Rent Reimbursement")) {
                                payrollReportInfoObject.setRent_Reimbursement(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Research Incentive Payment")) {
                                payrollReportInfoObject.setResearch_Incentive_Payment(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Service Charges")) {
                                payrollReportInfoObject.setService_Charges(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Travelling Allowance")) {
                                payrollReportInfoObject.setTravelling_Allowance(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("MSc Qualification Incentive")) {
                                payrollReportInfoObject.setMSc_Qualification_Incentive(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Crisis_Allowances _2022")) {
                                payrollReportInfoObject.setCrisis_Allowances_2022(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("DVC Incentive Allowance")) {
                                payrollReportInfoObject.setDVC_Incentive_Allowance(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("MSC_Phd_Incentive")) {
                                payrollReportInfoObject.setMSC_Phd_Incentive(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Performance _2022")) {
                                payrollReportInfoObject.setPerformance_2022(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Travellening Allo 22")) {
                                payrollReportInfoObject.setTravelling_Allo_22(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Advances")) {
                                payrollReportInfoObject.setAdvances(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("swa")) {
                                payrollReportInfoObject.setSwa(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Other Deductions")) {
                                payrollReportInfoObject.setOtherDeduction(String.valueOf(allSalaryInfoEntity.getAmount()));
                            } else if (allSalaryInfoEntity.getType().equalsIgnoreCase("Loan")) {
                                payrollReportInfoObject.setLoan(String.valueOf(allSalaryInfoEntity.getAmount()));
                            }
                        }

                    });

                    payrollReportInfoObject.setBasicSalary(String.valueOf(payroll.getBasicSalary()));


                    if (payroll.getTotalNoPay() != null && payroll.getTotalNoPay() > 0) {

                        payrollReportInfoObject.setNoPay(String.valueOf(payroll.getTotalNoPay()));

                    }

                    payrollReportInfoObject.setSetFinalizedBasicSalary(String.valueOf(payroll
                            .getBasicSalary() - ((payroll.getTotalNoPay() != null) ? payroll.getTotalNoPay() : 0F)));


                    if (payroll.getTotalMorningLate() != null && payroll.getTotalMorningLate() > 0) {
                        payrollReportInfoObject.setMorningLate(String.valueOf(payroll.getTotalMorningLate()));
                        payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalMorningLate());
                    }
                    if (payroll.getTotalLateAmount() != null && payroll.getTotalLateAmount() > 0) {
                        payrollReportInfoObject.setLateAttendance(String.valueOf(payroll.getTotalLateAmount()));
                        payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalLateAmount());
                    }
                    if (payroll.getPayee() != null && payroll.getPayee() > 0) {
                        payrollReportInfoObject.setPayee(String.valueOf(payroll.getPayee()));
                        payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getPayee());
                    }

                    if (payroll.getTotalDeductionForTasks() != null && payroll.getTotalDeductionForTasks() > 0) {
                        payrollReportInfoObject.setTaskDeduction(String.valueOf(payroll.getTotalDeductionForTasks()));
                        payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getTotalDeductionForTasks());
                    }

                    if (payroll.getEpfDeduction() != null && payroll.getEpfDeduction() > 0) {
                        payrollReportInfoObject.setEpf8(String.valueOf(payroll.getEpfDeduction()));
                        payroll.setTotalDeductions(payroll.getTotalDeductions() + payroll.getEpfDeduction());
                    }

                    if (payroll.getTotalOt() != null && payroll.getTotalOt() > 0) {
                        payrollReportInfoObject.setOtAmount(String.valueOf(payroll.getTotalOt()));
                        payroll.setGrossSalary(payroll.getGrossSalary() + payroll.getTotalOt());
                    }
                    payrollReportInfoObject.setGrossSalary(String.valueOf(payroll.getGrossSalary() - (payroll.getTotalNoPay() != null ? payroll.getTotalNoPay() : 0)));
                    payrollReportInfoObject.setTotal(String.valueOf(payroll.getGrossSalary() - (payroll.getTotalDeductions() != null ? payroll.getTotalDeductions() : 0)));


                    payrollReportInfoObject.setEpf12(String.valueOf(payroll.getEpfAddition()));
                    payrollReportInfoObject.setEpf8(String.valueOf(payroll.getEpfDeduction()));
                    payrollReportInfoObject.setEtf3(String.valueOf(payroll.getEtf()));
                    payrollReportInfoObject.setTotalDeduction(String.valueOf(payroll.getTotalDeductions()));

                    if (getList1().contains(payroll.getEmpId()))
                        payrollReportInfoObject.setDepartment("Academic");

                    if (getList2().contains(payroll.getEmpId()))
                        payrollReportInfoObject.setDepartment("Non Academic");


                    payrollReportInfoObjectsList.add(payrollReportInfoObject);
                }
            }

        });


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(payrollReportInfoObjectsList);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList getBasicSalaryInfoById(Integer id) {

        List<AllSalaryInfoEntity> list = allSalaryInfoRepository.getAllSalaryInfoByEmpIdAndCategory(id, "Basic Salary");

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(list);
        responseList.setMsg("Success");

        return responseList;


    }

    @Override
    public ResponseList getAllowanceSalaryInfoById(Integer id) {

        List<AllSalaryInfoEntity> list = allSalaryInfoRepository.getAllSalaryInfoByEmpIdAndCategory(id, "Allowances");

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(list);
        responseList.setMsg("Success");

        return responseList;


    }

    @Override
    public ResponseList getDeductionsSalaryInfoById(Integer id) {

        List<AllSalaryInfoEntity> list = allSalaryInfoRepository.getAllSalaryInfoByEmpIdAndCategory(id, "Deductions");

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(list);
        responseList.setMsg("Success");

        return responseList;


    }

    @Override
    @Transactional
    public ResponseList updateSalaryById(Integer id, String category,
                                         String type, Float amount, String reason,
                                         Integer updatedUser, String additionType, String effectiveDate) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date date = formatter.parse(effectiveDate);

        List<AllSalaryInfoEntity> allSalaryInfo = allSalaryInfoRepository.getAllSalaryInfoByEmpIdAndCategoryAndType(id, category, type);

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(id);

        ResponseList responseList = new ResponseList();


        if (emp.isEmpty()) {
            responseList.setCode(400);
            responseList.setMsg("User Not Found");
        } else if (allSalaryInfo.size() > 1) {
            responseList.setCode(300);
            responseList.setMsg("Found More Than One data");
        } else if (allSalaryInfo.isEmpty() && additionType.equalsIgnoreCase("Deduction")) {
            responseList.setCode(301);
            responseList.setMsg("Initial Value Cannot Be a Deduction");
        } else if (allSalaryInfo.isEmpty()) {
            AllSalaryInfoEntity allSalaryInfoEntity = new AllSalaryInfoEntity();
            allSalaryInfoEntity.setEmpId(id);
            allSalaryInfoEntity.setName(emp.get().getNameInFull());
            allSalaryInfoEntity.setCategory(category);
            allSalaryInfoEntity.setType(type);
            allSalaryInfoEntity.setAmount(0F);
            allSalaryInfoEntity.setStatus(0);
            allSalaryInfoEntity.setMonth("");
            allSalaryInfoEntity.setCreatedDate(new Date());
            AllSalaryInfoEntity obj = allSalaryInfoRepository.save(allSalaryInfoEntity);


            AllSalaryInfoBreakdownEntity allSalaryInfoBreakdownEntity
                    = new AllSalaryInfoBreakdownEntity();
            allSalaryInfoBreakdownEntity.setEmpId(id);
            allSalaryInfoBreakdownEntity.setEffectiveDate(date);
            allSalaryInfoBreakdownEntity.setName(emp.get().getNameInFull());
            allSalaryInfoBreakdownEntity.setCategory(category);
            allSalaryInfoBreakdownEntity.setType(type);
            allSalaryInfoBreakdownEntity.setAmount(amount);
            allSalaryInfoBreakdownEntity.setStatus(0);
            allSalaryInfoBreakdownEntity.setReason(reason);
            allSalaryInfoBreakdownEntity.setCreatedDate(new Date());
            allSalaryInfoBreakdownEntity.setMonth("");
            allSalaryInfoBreakdownEntity.setUpdatedDate(new Date());
            allSalaryInfoBreakdownEntity.setUpdatedBy(updatedUser);
            allSalaryInfoBreakdownEntity.setMainDataId(obj.getId());
            if (additionType.equalsIgnoreCase("Addition"))
                allSalaryInfoBreakdownEntity.setAdditionType(1);
            else if (additionType.equalsIgnoreCase("Deduction"))
                allSalaryInfoBreakdownEntity.setAdditionType(0);
            allSalaryInfoBreakdownRepository.save(allSalaryInfoBreakdownEntity);

            responseList.setCode(200);
            responseList.setMsg("Successfully Updated");
        } else {

            AllSalaryInfoBreakdownEntity allSalaryInfoBreakdownEntity
                    = new AllSalaryInfoBreakdownEntity();
            allSalaryInfoBreakdownEntity.setEmpId(id);
            allSalaryInfoBreakdownEntity.setEffectiveDate(date);
            allSalaryInfoBreakdownEntity.setName(emp.get().getNameInFull());
            allSalaryInfoBreakdownEntity.setCategory(category);
            allSalaryInfoBreakdownEntity.setType(type);
            allSalaryInfoBreakdownEntity.setAmount(amount);
            allSalaryInfoBreakdownEntity.setStatus(0);
            allSalaryInfoBreakdownEntity.setMonth("");
            allSalaryInfoBreakdownEntity.setReason(reason);
            allSalaryInfoBreakdownEntity.setUpdatedDate(new Date());
            allSalaryInfoBreakdownEntity.setUpdatedBy(updatedUser);
            allSalaryInfoBreakdownEntity.setCreatedDate(new Date());
            allSalaryInfoBreakdownEntity.setMainDataId(allSalaryInfo.get(0).getId());
            if (additionType.equalsIgnoreCase("Addition"))
                allSalaryInfoBreakdownEntity.setAdditionType(1);
            else if (additionType.equalsIgnoreCase("Deduction"))
                allSalaryInfoBreakdownEntity.setAdditionType(0);
            allSalaryInfoBreakdownRepository.save(allSalaryInfoBreakdownEntity);

            responseList.setCode(200);
            responseList.setMsg("Successfully Updated");

            AllSalaryInfoEntity allSalaryInfoEntity = allSalaryInfo.get(0);
            allSalaryInfoEntity.setStatus(0);
            allSalaryInfoRepository.save(allSalaryInfoEntity);

        }
        return responseList;
    }


    @Override
    public ResponseList getSalaryForEachType(Integer id) {

        List<AllSalaryInfoBreakdownEntity> allSalaryInfo = allSalaryInfoBreakdownRepository.getSalaryByMainTypeId(id);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(allSalaryInfo);
        return responseList;

    }

    @Override
    public ResponseList updateSalaryForEachType(Integer id, Integer status, Integer updatedUser) {

        Optional<AllSalaryInfoBreakdownEntity> allSalaryInfoObj = allSalaryInfoBreakdownRepository.findById(id);
        AllSalaryInfoBreakdownEntity allSalaryInfo = null;

        ResponseList responseList = new ResponseList();

        if (allSalaryInfoObj.isEmpty()) {
            responseList.setCode(400);
            responseList.setMsg("No Salary Breakdown Found");
        } else {
            allSalaryInfo = allSalaryInfoObj.get();
            List<AllSalaryInfoEntity> mainSalary = allSalaryInfoRepository.getAllSalaryInfoByEmpIdAndCategoryAndType(allSalaryInfo.getEmpId(),
                    allSalaryInfo.getCategory(), allSalaryInfo.getType());

            if (mainSalary.isEmpty()) {
                responseList.setCode(400);
                responseList.setMsg("Main Salary Not Found");
            } else if (mainSalary.size() > 1) {
                responseList.setCode(400);
                responseList.setMsg("Found More Than One Main Salary");
            } else {
                AllSalaryInfoBreakdownEntity subSalary = allSalaryInfo;
                subSalary.setUpdatedBy(updatedUser);
                if (status == 1) {
                    subSalary.setApprovedBy(updatedUser);
                    subSalary.setApprovedDate(new Date());
                }
                subSalary.setStatus(status);
                subSalary.setUpdatedDate(new Date());

                List<AllSalaryInfoBreakdownEntity> dataListForMainStatus = allSalaryInfoBreakdownRepository.getSalaryByMainTypeId(subSalary.getMainDataId());

                Integer statusMain = 0;

                for (AllSalaryInfoBreakdownEntity obj : dataListForMainStatus) {
                    if (obj.getStatus() == 0) {
                        statusMain = 0;
                        break;
                    } else if (obj.getStatus() == 1) {
                        statusMain = 1;
                    } else if (statusMain != 1 && obj.getStatus() == 2) {
                        statusMain = 2;
                    }
                }

                AllSalaryInfoEntity mainSalaryObj = mainSalary.get(0);
                mainSalaryObj.setStatus(statusMain);
                if (subSalary.getAdditionType() == 1 && status == 1)
                    mainSalaryObj.setAmount(mainSalaryObj.getAmount() + subSalary.getAmount());
                else if (subSalary.getAdditionType() == 0 && status == 1) {
                    if ((mainSalaryObj.getAmount() - subSalary.getAmount()) < 0) {
                        responseList.setCode(1001);
                        responseList.setMsg("Salary Cannot Be a Negative Value");
                        return responseList;
                    }
                    mainSalaryObj.setAmount(mainSalaryObj.getAmount() - subSalary.getAmount());

                }
                saveToDbForStatusUpdate(subSalary, mainSalaryObj);
                responseList.setCode(200);
                responseList.setMsg("Successfully Updated the Salary");
            }


        }

        return responseList;

    }

    @Override
    public ResponseList updateMasterSalaryCategory(String category, String type, Integer updatedUser) {

        List<SalaryInfoMasterCategory> masterCategory = salaryInfoMasterCategoryRepository.getSalaryMasterTypeByType(type);

        ResponseList responseList = new ResponseList();

        if (masterCategory.isEmpty()){

            SalaryInfoMasterCategory salaryInfoMasterCategory = new SalaryInfoMasterCategory();
            salaryInfoMasterCategory.setCategory(category);
            salaryInfoMasterCategory.setType(type);
            salaryInfoMasterCategory.setCreatedBy(updatedUser);
            salaryInfoMasterCategory.setCreatedDate(new Date());
            salaryInfoMasterCategoryRepository.save(salaryInfoMasterCategory);
            responseList.setCode(200);
            responseList.setMsg("Successfully Updated");

        }
        else {
            responseList.setCode(1001);
            responseList.setMsg("Type Already Exists");
        }


        return responseList;


    }

    @Override
    public ResponseList getMasterSalaryTypeByCategory(String category) {

        List<SalaryInfoMasterCategory> masterCategory = salaryInfoMasterCategoryRepository.getSalaryMasterTypeByCategory(category);

        ArrayList<String> obj = new ArrayList<>();

        masterCategory.forEach(salaryInfoMasterCategory -> {
            obj.add(salaryInfoMasterCategory.getType());
        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(obj);
        return responseList;
    }

    @Override
    public ResponseList getMasterPayrollSettings(Integer id) {

        List<Object> list = payrollSettingRepository.getPayrollSettingByEmpId(id);

        List<PayrollSettingObject> returnList = new ArrayList<>();

        for (Object obj : list){

            Object[] data =  ((Object[]) obj);

            Integer createdBy = null;

            try {
                createdBy = Integer.valueOf(String.valueOf(data[4]));
            }
            catch (Exception e){

            }

            PayrollSettingObject tmpObj = new PayrollSettingObject(Integer.valueOf(String.valueOf(data[0])),
                    Integer.valueOf(String.valueOf(data[1])), String.valueOf(data[2]),
                    String.valueOf(data[3]), createdBy);

            returnList.add(tmpObj);

        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(returnList);
        return responseList;


    }

    @Override
    public ResponseList updateMasterPayrollSettings(Integer id, Integer status) {

        ResponseList responseList = new ResponseList();

        Optional<MasterPayrollSettings> obj = payrollSettingRepository.findById(id);

        if (obj.isEmpty()){
            responseList.setCode(400);
            responseList.setMsg("Setting Not found");
        }
        else {
            MasterPayrollSettings tmpObj = obj.get();
            tmpObj.setSettingValue(status);
            payrollSettingRepository.save(tmpObj);
            responseList.setCode(200);
            responseList.setMsg("Success");
        }

        return responseList;

    }

    @Override
    public ResponseList getPendingApprovalList() {

        List<EmpDetailEntity> list = empDetailRepository.findAll();

        List<AllSalaryInfoBreakdownEntity> pendingList = allSalaryInfoBreakdownRepository
                .getPendingApprovalList();

        List<Integer> empList = new ArrayList<>();

        pendingList.forEach(allSalaryInfoBreakdownEntity -> {
            if (!empList.contains(allSalaryInfoBreakdownEntity.getEmpId()))
                empList.add(allSalaryInfoBreakdownEntity.getEmpId());
        });

        List<SupervisorLeaveList> supervisorLists = new ArrayList<>();

        list.forEach(empDetailEntity -> {

            if (empList.contains(empDetailEntity.getId())) {
                SupervisorLeaveList supervisorList = new SupervisorLeaveList();
                supervisorList.setId(empDetailEntity.getId());
                supervisorList.setEmail(empDetailEntity.getEmail());
                supervisorList.setNicNo(empDetailEntity.getNicNo());
                supervisorList.setGivenName(empDetailEntity.getGivenName());
                supervisorList.setNameInFull(empDetailEntity.getNameInFull());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setSupervisor(empDetailEntity.getSupervisor());
                supervisorLists.add(supervisorList);
            }

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(supervisorLists);

        return responseList;

    }

    @Transactional
    public void saveToDbForStatusUpdate(AllSalaryInfoBreakdownEntity subSalary, AllSalaryInfoEntity mainSalaryObj) {
        allSalaryInfoBreakdownRepository.save(subSalary);
        allSalaryInfoRepository.save(mainSalaryObj);
    }


    private List<Integer> getList1() {

        List<Integer> list = new ArrayList<>();
        list.add(42);
        list.add(50);
        list.add(38);
        list.add(37);
        list.add(36);
        list.add(54);
        list.add(55);
        list.add(58);
        list.add(49);
        list.add(44);
        list.add(103);
        list.add(99);
        list.add(69);
        list.add(91);
        list.add(71);
        list.add(94);
        list.add(553);
        list.add(105);
        list.add(130);
        list.add(161);
        list.add(165);
        list.add(172);
        list.add(169);
        list.add(184);
        list.add(369);
        list.add(383);
        list.add(370);
        list.add(395);
        list.add(372);
        list.add(394);
        list.add(431);
        list.add(439);
        list.add(444);
        list.add(451);
        list.add(452);
        list.add(453);
        list.add(342);
        list.add(462);
        list.add(487);
        list.add(402);
        list.add(484);
        list.add(483);
        list.add(491);
        list.add(477);
        list.add(510);
        list.add(343);
        list.add(507);
        list.add(526);
        list.add(547);
        list.add(556);
        list.add(576);
        list.add(422);
        list.add(578);
        list.add(585);
        list.add(588);
        list.add(380);
        list.add(596);
        list.add(598);
        list.add(599);
        list.add(647);
        list.add(667);

        return list;

    }

    public List<Integer> getList2() {

        List<Integer> list2 = new ArrayList<>();
        list2.add(258);
        list2.add(261);
        list2.add(263);
        list2.add(266);
        list2.add(269);
        list2.add(270);
        list2.add(271);
        list2.add(272);
        list2.add(273);
        list2.add(275);
        list2.add(276);
        list2.add(277);
        list2.add(274);
        list2.add(257);
        list2.add(279);
        list2.add(288);
        list2.add(291);
        list2.add(293);
        list2.add(301);
        list2.add(306);
        list2.add(313);
        list2.add(314);
        list2.add(319);
        list2.add(363);
        list2.add(292);
        list2.add(420);
        list2.add(296);
        list2.add(425);
        list2.add(464);
        list2.add(468);
        list2.add(480);
        list2.add(488);
        list2.add(492);
        list2.add(493);
        list2.add(512);
        list2.add(527);
        list2.add(516);
        list2.add(517);
        list2.add(554);
        list2.add(583);
        list2.add(584);
        list2.add(597);
        list2.add(499);
        list2.add(654);
        list2.add(664);
        list2.add(666);
        list2.add(668);
        list2.add(671);
        list2.add(673);

        return list2;
    }
}

