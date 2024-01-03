package com.taskmanager.task.controller;

import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.impl.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin
@RequestMapping("main-erp/report")
public class ReportController {
    @Autowired
    private ReportServiceImpl reportService;
    @GetMapping("/get-all/{offSet}/{pageSize}")
    public ResponseList getAllPageination(@PathVariable int offSet, @PathVariable int pageSize) throws ParseException {
        return  reportService.getAllData(offSet,pageSize);
    }
    @GetMapping("/all")
    public ResponseList getAll() throws  ParseException{
        return  reportService.getAll();
    }
    @GetMapping("/get-income")
    public ResponseList getAllIncomeData() throws  ParseException{
        return  reportService.getIncomeData();
    }
    @GetMapping("/get-income/{offSet}/{pageSize}")
    public ResponseList getIncomePaginated(@PathVariable int offSet,@PathVariable int pageSize) throws  ParseException{
        return reportService.getAllIncomeDataPaginated( offSet,pageSize);
    }
    @GetMapping("/get-due-reports/{dateRange}")
    public ResponseList getDueReports(@PathVariable Integer dateRange) throws  ParseException{
        return reportService.getDueReports(dateRange);
    }
    @GetMapping("/get-due-without-payment-cards")
    public ResponseList getStudentsWithoutPaymentCards() throws  ParseException{
        return reportService.getStudentsWithoutPaymentCards();
    }
    @GetMapping("/get-out-standing-report/{startDate}")
    public ResponseList getOutStandingReport(@PathVariable String startDate) throws ParseException{
        return reportService.getOutStandingReport(startDate);
    }
    @GetMapping("/get-out-standing-other-payments/{startDate}")
    public ResponseList getOtherPaymentOutStandingReport(@PathVariable String startDate) throws ParseException{
        return reportService.getOtherPaymentOutStandingReport(startDate);
    }
    @GetMapping("/get-income-report")
    public ResponseList getIncomeReport(@RequestParam(value = "start_date") String startDate,@RequestParam(value = "end_date") String endDate) throws ParseException{
        return reportService.getIncomeReport(startDate,endDate);
    }
    @GetMapping("/get-income-report-other-payment")
    public ResponseList getIncomeReportOtherPayment(@RequestParam(value = "start_date") String startDate,@RequestParam(value = "end_date") String endDate) throws ParseException{
        return reportService.getIncomeReportOtherPayment(startDate,endDate);
    }
    @GetMapping("/get-active-to-temporary-drop/{startDate}/{endDate}")
    public ResponseList getActiveToTemporaryDrop(@PathVariable String startDate,@PathVariable String endDate) throws ParseException{
        return reportService.getActiveToTemporaryDrop(startDate,endDate);
    }
    @GetMapping("/get-full-payment-details")
    public  ResponseList getFullPaymentDetails(@RequestParam(value = "batch_id") String batchId){
        return reportService.getFullPaymentDetails(batchId);
    }
    @GetMapping("/work-summary-report")
    public ResponseList workSummaryReport(@RequestParam (value = "start_date") String startDate,@RequestParam (value = "end_date") String endDate, @RequestParam (value = "faculty_id") String facultyId,@RequestParam (value = "dept_id") String deptId){
        return  reportService.workSummaryReport(startDate,endDate,facultyId,deptId);
    }
}
