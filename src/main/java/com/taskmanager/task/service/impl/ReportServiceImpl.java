package com.taskmanager.task.service.impl;

import com.taskmanager.task.const_codes.VariableCodes;
import com.taskmanager.task.model.income_report.IncomeReportDataModel;
import com.taskmanager.task.model.income_report.IncomeReportResponseModel;
import com.taskmanager.task.model.income_report_other_payment.IncomeReportOtherPaymentDataModel;
import com.taskmanager.task.model.income_report_other_payment.IncomeReportOtherPaymentResponseModel;
import com.taskmanager.task.model.out_standing.OutStandingDataModel;
import com.taskmanager.task.model.out_standing.OutstandingResponseModel;
import com.taskmanager.task.model.out_standing_other_payments.OutStandingOtherPaymentDataModel;
import com.taskmanager.task.model.out_standing_other_payments.OutStandingOtherPaymentResponseModel;
import com.taskmanager.task.repository.AttendanceReportRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private AttendanceReportRepository attendanceRepository;

    @Override
    public ResponseList getAllData(int offSet, int pageSize) throws ParseException {
        ResponseList responseList = new ResponseList();

        Page<Map<String, Object>> all = attendanceRepository.gettingAllInfo(PageRequest.of(offSet, pageSize));

        responseList.setCode(200);
        responseList.setData(all);
        return responseList;
    }

    @Override
    public ResponseList getIncomeData() throws ParseException {
        ResponseList responseList = new ResponseList();
        List<Map<String, Object>> allIncome = attendanceRepository.gettinIncomeInfo();
        responseList.setCode(200);
        responseList.setData(allIncome);
        return responseList;
    }

    @Override
    public ResponseList getAll() throws ParseException {
        ResponseList responseList = new ResponseList();
        List<Map<String, Object>> all = attendanceRepository.allInfo();
        responseList.setCode(200);
        responseList.setData(all);
        return responseList;
    }

    @Override
    public ResponseList getAllIncomeDataPaginated(int offSet, int pageSize) throws ParseException {
        ResponseList responseList = new ResponseList();
        Page<Map<String, Object>> incomePaginated = attendanceRepository.gettingIncomeInfoPageination(PageRequest.of(offSet, pageSize));
        responseList.setCode(200);
        responseList.setData(incomePaginated);
        return responseList;
    }

    @Override
    public ResponseList getDueReports(Integer dateRange) throws ParseException {
        ResponseList responseList = new ResponseList();
        List<Map<String, Object>> fetchDueReports = attendanceRepository.fetchDueReports(dateRange);
        if (fetchDueReports.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(fetchDueReports);
        }

        return responseList;
    }

    @Override
    public ResponseList getStudentsWithoutPaymentCards() throws ParseException {
        ResponseList responseList = new ResponseList();
        List<Map<String, Object>> getStudentsWithoutPaymentCards = attendanceRepository.getStudentsWithoutPaymentCards();
        responseList.setCode(200);
        responseList.setData(getStudentsWithoutPaymentCards);
        return responseList;
    }

    @Override
    public ResponseList getOutStandingReport(String startDate) throws ParseException {
        ResponseList responseList = new ResponseList();
        Double amount = 0.0;
        List<OutStandingDataModel> outstandingList = new ArrayList<>();
        OutstandingResponseModel outstandingResponseModel = new OutstandingResponseModel();
        LocalDate currentDate = LocalDate.now();
        List<Map<String, Object>> getOutStandingReport = attendanceRepository.outStandingReport(startDate, currentDate);
        for (Map<String, Object> items : getOutStandingReport) {
            Integer installmentCount = (Integer) items.get(VariableCodes.INSTALLMENT_COUNTER);
            Double dueAmount = items.get(VariableCodes.DUE_AMOUNT) == null ? 0.0 : (Double) items.get(VariableCodes.DUE_AMOUNT);
            String installmentType = (String) items.get(VariableCodes.INSTALLMENT_TYPE);
            OutStandingDataModel outstandingDataModel = new OutStandingDataModel();
            outstandingDataModel.setPaymentPlanCardId((Object) items.get(VariableCodes.ID));
            outstandingDataModel.setBatchName((String) items.get(VariableCodes.BATCH_NAME));
            outstandingDataModel.setStatus((String) items.get(VariableCodes.STATUS));
            outstandingDataModel.setDueDate((Object) items.get(VariableCodes.DUE_DATE));
            outstandingDataModel.setNameInitials((String) items.get(VariableCodes.NAME_INITIAL));
            outstandingDataModel.setDueAmount(dueAmount);
            outstandingDataModel.setRegisteredDate((Object) items.get(VariableCodes.REGISTERED_DATE));
            outstandingDataModel.setInstallmentType(installmentType);
            outstandingDataModel.setStudentId((Integer) items.get(VariableCodes.STUDENT_ID));
            outstandingDataModel.setInstallmentCounter(installmentCount);
            outstandingDataModel.setCourseName((String) items.get(VariableCodes.COURSE_NAME));
            outstandingDataModel.setPaymentStatus(Objects.equals((String) items.get(VariableCodes.PAYMENT_STATUS), "") ? "PENDING" : (String) items.get(VariableCodes.PAYMENT_STATUS));
            outstandingDataModel.setTaxPaid((Double) items.get(VariableCodes.TAX_PAID));
            outstandingDataModel.setPaymentType(paymentType(installmentCount, installmentType));
            outstandingDataModel.setStudentCategory((String) items.get(VariableCodes.STUDENT_CATEGORY));
            outstandingList.add(outstandingDataModel);
            amount += dueAmount;
        }
        outstandingResponseModel.setTotalDueAmount(BigDecimal.valueOf(amount).toPlainString());
        outstandingResponseModel.setDataList(outstandingList);
        if (getOutStandingReport.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(outstandingResponseModel);
        }
        return responseList;
    }

    public String paymentType(Integer count, String type) {
        return type + " " + count;
    }

    @Override
    public ResponseList getOtherPaymentOutStandingReport(String startDate) throws ParseException {
        ResponseList responseList = new ResponseList();
        OutStandingOtherPaymentResponseModel outStandingOtherPaymentResponseModel = new OutStandingOtherPaymentResponseModel();
        List<Map<String, Object>> filterStartDateAndEndDate = new ArrayList<>();
        List<OutStandingOtherPaymentDataModel> outStandingOtherPaymentDataModels = new ArrayList<>();
        Double dueAmount = 0.0;
        Date currentDate = new Date();

        List<Map<String, Object>> otherPaymentOutStandingReport = attendanceRepository.outStandingOtherPaymentReport(startDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateParsed = dateFormat.parse(startDate);
        Date endDateParsed = currentDate;


        String formattedEndDate = dateFormat.format(endDateParsed);

//        for (Map<String, Object> items : otherPaymentOutStandingReport) {
//            String dueDateString = (String) items.get(VariableCodes.DUE_DATE);
//            if (dueDateString != null) {
//                Date dueDate = dateFormat.parse(dueDateString);
//
//                if (dueDate != null && !dueDate.before(startDateParsed) && !dueDate.after(dateFormat.parse(formattedEndDate))) {
//                    filterStartDateAndEndDate.add(items);
//                }
//            }
//        }

        for (Map<String, Object> items : otherPaymentOutStandingReport) {
            OutStandingOtherPaymentDataModel outStandingOtherPaymentDataModel = new OutStandingOtherPaymentDataModel();
            Double amount = items.get(VariableCodes.DUE_AMOUNT) == null ? 0.0 : (Double) items.get(VariableCodes.DUE_AMOUNT);
            outStandingOtherPaymentDataModel.setId((Long) items.get(VariableCodes.ID));
            outStandingOtherPaymentDataModel.setStatus((String) items.get(VariableCodes.PAYMENT_PLAN_CARDS_STATUS));
            outStandingOtherPaymentDataModel.setAmount(amount);
            outStandingOtherPaymentDataModel.setStudentId((Integer) items.get(VariableCodes.STUDENT_ID));
            outStandingOtherPaymentDataModel.setDueDate((Object) items.get(VariableCodes.DUE_DATE));
            outStandingOtherPaymentDataModel.setPaymentType((String) items.get(VariableCodes.NAME));
            outStandingOtherPaymentDataModel.setBatchName((String) items.get(VariableCodes.BATCH_NAME));
            outStandingOtherPaymentDataModel.setCourseName((String) items.get(VariableCodes.COURSE_NAME));
            outStandingOtherPaymentDataModel.setTotalPaid((Double) items.get(VariableCodes.TOTAL_PAID));
            outStandingOtherPaymentDataModel.setDueAmount((Double) items.get(VariableCodes.DUE_AMOUNT));
            outStandingOtherPaymentDataModel.setRegisteredDate((String) items.get(VariableCodes.REGISTERED_DATE));
            outStandingOtherPaymentDataModel.setPaymentStatus((String) items.get(VariableCodes.PAYMENT_STATUS));
            outStandingOtherPaymentDataModel.setStudentCategory((String) items.get(VariableCodes.STUDENT_CATEGORY));
            outStandingOtherPaymentDataModel.setNameInitials((String) items.get(VariableCodes.NAME_INITIAL));
            outStandingOtherPaymentDataModels.add(outStandingOtherPaymentDataModel);
            dueAmount += amount;
        }

        outStandingOtherPaymentResponseModel.setTotalDueAmount(BigDecimal.valueOf(dueAmount).toPlainString());
        outStandingOtherPaymentResponseModel.setDataList(outStandingOtherPaymentDataModels);
        if (otherPaymentOutStandingReport.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(outStandingOtherPaymentResponseModel);
        }

        return responseList;

    }

    @Override
    public ResponseList getIncomeReport(String startDate, String endDate) throws ParseException {
        ResponseList responseList = new ResponseList();
        Double courseFee = 0.0;
        Double registration = 0.0;
        Double initialFee = 0.0;
        Double totalInitials = 0.0;
        IncomeReportResponseModel incomeReportResponseModel = new IncomeReportResponseModel();
        if (endDate.isEmpty() || endDate.equals("null")) {
            LocalDate currentDate = LocalDate.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            endDate = currentDate.format(formatter);

        }
        List<Map<String, Object>> getStudentPaymentPlanCards = attendanceRepository.getIncomeReport(startDate, endDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<IncomeReportDataModel> incomeReportDataModels = new ArrayList<>();

        for (Map<String, Object> items : getStudentPaymentPlanCards) {
            String installmentType = (String) items.get(VariableCodes.INSTALLMENT_TYPE);
            if (installmentType.equals(VariableCodes.COURSE_FEE)) {
                totalInitials = items.get(VariableCodes.TOTAL_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TOTAL_PAID);
                courseFee += totalInitials;
            }
            if (installmentType.equals(VariableCodes.REGISTRATION)) {
                totalInitials = items.get(VariableCodes.TOTAL_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TOTAL_PAID);
                registration += totalInitials;
            }
            if (installmentType.equals(VariableCodes.INITIAL_FEE)) {
                totalInitials += items.get(VariableCodes.TOTAL_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TOTAL_PAID);
                initialFee += totalInitials;
            }
            IncomeReportDataModel incomeReportDataModel = new IncomeReportDataModel();
            incomeReportDataModel.setPaymentPlanCardId((Object) items.get(VariableCodes.ID));
            incomeReportDataModel.setStudentId((Integer) items.get(VariableCodes.STUDENT_ID));
            incomeReportDataModel.setDueDate((Object) items.get(VariableCodes.DUE_DATE));
            incomeReportDataModel.setNameInitials((String) items.get(VariableCodes.NAME_INITIAL));
            incomeReportDataModel.setRegisteredDate((Object) items.get(VariableCodes.REGISTERED_DATE));
            incomeReportDataModel.setStudentCategory((String) items.get(VariableCodes.STUDENT_CATEGORY));
            incomeReportDataModel.setInstallmentType((String) items.get(VariableCodes.INSTALLMENT_TYPE));
            incomeReportDataModel.setInstallmentCounter((Integer) items.get(VariableCodes.INSTALLMENT_COUNTER));
            incomeReportDataModel.setDueAmount(items.get(VariableCodes.DUE_AMOUNT) == null ? 0.0 : (Double) items.get(VariableCodes.DUE_AMOUNT));
            incomeReportDataModel.setAmount(items.get(VariableCodes.AMOUNT) == null ? 0.0 : (Double) items.get(VariableCodes.AMOUNT));
            incomeReportDataModel.setTaxPaid(items.get(VariableCodes.TAX_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TAX_PAID));
            incomeReportDataModel.setTotalPaid(items.get(VariableCodes.TOTAL_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TOTAL_PAID));
            incomeReportDataModel.setTotalLatePaymentPaid(items.get(VariableCodes.TOTAL_LATE_PAYMENT_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TOTAL_LATE_PAYMENT_PAID));
            incomeReportDataModel.setPaymentStatus((String) items.get(VariableCodes.PAYMENT_STATUS));
            incomeReportDataModel.setBatchName((String) items.get(VariableCodes.BATCH_NAME));
            incomeReportDataModel.setBatchId((Long) items.get(VariableCodes.BATCH_ID));
            incomeReportDataModel.setCourseId((Long) items.get(VariableCodes.COURSE_ID));
            incomeReportDataModel.setCourseName((String) items.get(VariableCodes.COURSE_NAME));
            incomeReportDataModel.setDeptId((Integer) items.get(VariableCodes.DEPT_ID));
            incomeReportDataModel.setDeptName((String) items.get(VariableCodes.DEPT_NAME));
            incomeReportDataModel.setFacultyId((Integer) items.get(VariableCodes.FACULTY_ID));
            incomeReportDataModel.setFacultyName((String) items.get(VariableCodes.FACULTY_NAME));
            incomeReportDataModel.setStatus((String) items.get(VariableCodes.STATUS));

            incomeReportDataModels.add(incomeReportDataModel);
        }
        Double total = initialFee + courseFee + registration;
        incomeReportResponseModel.setTotalIncome(BigDecimal.valueOf(total).toPlainString());
        incomeReportResponseModel.setData(incomeReportDataModels);
        incomeReportResponseModel.setInitialIncome(BigDecimal.valueOf(initialFee).toPlainString());
        incomeReportResponseModel.setRegistrationIncome(BigDecimal.valueOf(registration).toPlainString());
        incomeReportResponseModel.setCourseFeeIncome(BigDecimal.valueOf(courseFee).toPlainString());
        if (getStudentPaymentPlanCards.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(incomeReportResponseModel);
        }
        return responseList;

    }

    @Override
    public ResponseList getIncomeReportOtherPayment(String startDate, String endDate) throws ParseException {
        ResponseList responseList = new ResponseList();
        List<IncomeReportOtherPaymentDataModel> incomeReportOtherPaymentDataModels = new ArrayList<>();
        if (endDate.isEmpty() || endDate.equals("null")) {
            LocalDate currentDate = LocalDate.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            endDate = currentDate.format(formatter);

        }

        List<Map<String, Object>> getIncomeReportOtherPayment = attendanceRepository.getIncomeReportOtherPayment(startDate, endDate);
        Double totalIncome = 0.0;
        IncomeReportOtherPaymentResponseModel incomeReportOtherPaymentResponseModel = new IncomeReportOtherPaymentResponseModel();
        for (Map<String, Object> items : getIncomeReportOtherPayment) {
            Double totalPaid = items.get(VariableCodes.TOTAL_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TOTAL_PAID);
            IncomeReportOtherPaymentDataModel incomeReportOtherPaymentDataModel = new IncomeReportOtherPaymentDataModel();
            incomeReportOtherPaymentDataModel.setPaymentPlanCardId((Long) items.get(VariableCodes.ID));
            incomeReportOtherPaymentDataModel.setStudentId((Integer) items.get(VariableCodes.STUDENT_ID));
            incomeReportOtherPaymentDataModel.setNameInitials((String) items.get(VariableCodes.NAME_INITIAL));
            incomeReportOtherPaymentDataModel.setPaymentPlanCardStatus((String) items.get(VariableCodes.PAYMENT_PLAN_CARDS_STATUS));
            incomeReportOtherPaymentDataModel.setAmount(items.get(VariableCodes.AMOUNT) == null ? 0.0 : (Double) items.get(VariableCodes.AMOUNT));
            incomeReportOtherPaymentDataModel.setTotalPaid(items.get(VariableCodes.TOTAL_PAID) == null ? 0.0 : (Double) items.get(VariableCodes.TOTAL_PAID));
            incomeReportOtherPaymentDataModel.setPaymentStatus((String) items.get(VariableCodes.PAYMENT_STATUS));
            incomeReportOtherPaymentDataModel.setDueDate((Object) items.get(VariableCodes.DUE_DATE));
            incomeReportOtherPaymentDataModel.setName((String) items.get(VariableCodes.NAME));
            incomeReportOtherPaymentDataModel.setBatchName((String) items.get(VariableCodes.BATCH_NAME));
            incomeReportOtherPaymentDataModel.setBatchId((Long) items.get(VariableCodes.BATCH_ID));
            incomeReportOtherPaymentDataModel.setCourseId((Long) items.get(VariableCodes.COURSE_ID));
            incomeReportOtherPaymentDataModel.setCourseName((String) items.get(VariableCodes.COURSE_NAME));
            incomeReportOtherPaymentDataModel.setDeptId((Integer) items.get(VariableCodes.DEPT_ID));
            incomeReportOtherPaymentDataModel.setDeptName((String) items.get(VariableCodes.DEPT_NAME));
            incomeReportOtherPaymentDataModel.setFacultyId((Integer) items.get(VariableCodes.FACULTY_ID));
            incomeReportOtherPaymentDataModel.setFacultyName((String) items.get(VariableCodes.FACULTY_NAME));
            incomeReportOtherPaymentDataModel.setStudentCategory((String) items.get(VariableCodes.STUDENT_CATEGORY));
            incomeReportOtherPaymentDataModels.add(incomeReportOtherPaymentDataModel);
            totalIncome += totalPaid;
        }
        incomeReportOtherPaymentResponseModel.setDataList(incomeReportOtherPaymentDataModels);
        incomeReportOtherPaymentResponseModel.setTotalIncome(BigDecimal.valueOf(totalIncome).toPlainString());
        if (getIncomeReportOtherPayment.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(incomeReportOtherPaymentResponseModel);
        }
        return responseList;
    }

    @Override
    public ResponseList getActiveToTemporaryDrop(String startDate, String endDate) {
        ResponseList responseList = new ResponseList();
        List<Map<String, Object>> getActiveToTemporaryDrop = attendanceRepository.getActiveToTemporaryDrop(startDate, endDate);
        if (getActiveToTemporaryDrop.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(getActiveToTemporaryDrop);
        }
        return responseList;
    }

    @Override
    public ResponseList getFullPaymentDetails(String batchId) {
        ResponseList responseList = new ResponseList();
        List<Map<String, Object>> getFullPaymentDetails;
        if (batchId.isEmpty() || batchId.equals("null")) {
            getFullPaymentDetails = attendanceRepository.getFullPaymentDetails();
        } else {
            Long newBatchId = Long.parseLong(batchId);

            getFullPaymentDetails = attendanceRepository.getFullPaymentDetailsWithBatch(newBatchId);
        }

        if (getFullPaymentDetails.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(getFullPaymentDetails);
        }
        return responseList;
    }
    @Override
    public ResponseList workSummaryReport(String startDate, String endDate, String facultyId, String deptId) {
        ResponseList responseList=new ResponseList();
        List<Map<String,Object>> workSummaryReport=attendanceRepository.workSummaryReport(startDate,endDate);
        if (workSummaryReport.isEmpty()) {
            responseList.setCode(204);
        } else {
            responseList.setCode(200);
            responseList.setData(workSummaryReport);
        }
        return responseList;
    }
}
