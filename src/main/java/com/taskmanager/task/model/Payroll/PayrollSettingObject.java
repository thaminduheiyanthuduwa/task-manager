package com.taskmanager.task.model.Payroll;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PayrollSettingObject {

    private Integer id;

    private Integer value;

    private String settingName;

    private String createdDate;

    private Integer createdBy;


    public PayrollSettingObject(Integer id, Integer value, String settingName, String createdDate,
                                Integer createdBy) {
        this.id = id;
        this.value = value;
        this.settingName = settingName;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }
}
