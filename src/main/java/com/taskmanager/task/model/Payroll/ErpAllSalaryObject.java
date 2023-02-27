package com.taskmanager.task.model.Payroll;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErpAllSalaryObject {

    private String id;

    private Integer empId;

    private String name;

    private String category;

    private String type;

    private Float amount;

    public ErpAllSalaryObject(String id, Integer empId, String name, String category, String type, Float amount) {
        this.id = id;
        this.empId = empId;
        this.name = name;
        this.category = category;
        this.type = type;
        this.amount = amount;
    }
}
