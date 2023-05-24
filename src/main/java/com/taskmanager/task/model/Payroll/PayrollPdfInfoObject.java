package com.taskmanager.task.model.Payroll;

import com.taskmanager.task.entity.PayrollEntityDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PayrollPdfInfoObject {

    private String id;

    private String name;

    private String epfNo;

    private String designation;

    private String date;

    private String totalAmount;

    private List<PayrollPdfInfoEarningObject> payrollPdfInfoEarningObjectList;

    private List<PayrollPdfInfoBasicObject> payrollPdfInfoBasicObjects;

    private List<PayrollPdfInfoDeductionObject> payrollPdfInfoDeductionObjects;

    private PayrollEntityDetails payrollEntityDetails;


}
