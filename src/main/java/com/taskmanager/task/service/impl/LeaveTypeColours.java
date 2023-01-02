package com.taskmanager.task.service.impl;

public enum LeaveTypeColours  {

    Annual("success"),
    Casual("primary"),
    LieuLeave("warning"),
    Special("danger"),
    ProbationHalfDay("success"),
    ShortLeave("primary"),
    MinorStaffMonthly("warning"),
    CleaningStaff("danger");


    LeaveTypeColours(String success) {
    }


    public static String valueOfLabel(String label) {
        for (LeaveTypeColours e : values()) {
            if (e.toString().equalsIgnoreCase((label))) {
                return LeaveTypeColours.Annual.name();
            }
        }
        return null;
    }
}
