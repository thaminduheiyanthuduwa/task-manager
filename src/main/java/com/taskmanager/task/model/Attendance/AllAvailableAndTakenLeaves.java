package com.taskmanager.task.model.Attendance;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AllAvailableAndTakenLeaves {

    String taken;

    String available;

    public AllAvailableAndTakenLeaves(String taken, String available) {
        this.taken = taken;
        this.available = available;
    }
}
