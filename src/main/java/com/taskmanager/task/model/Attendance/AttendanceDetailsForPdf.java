package com.taskmanager.task.model.Attendance;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttendanceDetailsForPdf {

    String date;

    String status;

    String otAmount;

    String lateAmount;

    String noPay;

    String morningLate;

}
