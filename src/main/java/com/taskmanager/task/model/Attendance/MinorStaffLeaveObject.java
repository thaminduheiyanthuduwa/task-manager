package com.taskmanager.task.model.Attendance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinorStaffLeaveObject {

    private String id;

    private String name;

    private String leaveCount;

    public MinorStaffLeaveObject(String id, String name, String leaveCount) {
        this.id = id;
        this.name = name;
        this.leaveCount = leaveCount;
    }
}
