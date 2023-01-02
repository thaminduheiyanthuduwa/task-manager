package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.AttendanceEntity;
import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.LeaveEntity;
import com.taskmanager.task.model.Attendance.CreateAttendance;
import com.taskmanager.task.model.Attendance.ErpAttendance;
import com.taskmanager.task.model.Attendance.MyAttendance;
import com.taskmanager.task.model.Attendance.YesterdayAttendance;
import com.taskmanager.task.model.leave.CreateLeave;
import com.taskmanager.task.repository.AttendanceRepository;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.AttendanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AttendanceManagerImpl implements AttendanceManager {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    private EmpDetailRepository empDetailRepository;


    @Override
    public ResponseList getAttendanceByID(int id) throws ParseException {

        SimpleDateFormat convertStringToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        SimpleDateFormat convertStringToDateTypeTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat convertDateToTime = new SimpleDateFormat("HH:mm:ss");

        SimpleDateFormat convertDateToDateOnly = new SimpleDateFormat("yyyy-MM-dd");

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(id);

        List<AttendanceEntity> attendanceIssue = attendanceRepository.findByEmpId(id);

        Comparator<AttendanceEntity> comparator = (c1, c2) -> {
            return Long.valueOf(c1.getDate().getTime()).compareTo(c2.getDate().getTime());
        };
        Collections.sort(attendanceIssue, comparator);

        List<ErpAttendance> erpAttendancesListTwo = new ArrayList<>();

        List<MyAttendance> outObj = new ArrayList<>();

        for (AttendanceEntity attendance : attendanceIssue) {
            MyAttendance erpAttendance = new MyAttendance();
            erpAttendance.setId(attendance.getId());
            erpAttendance.setType(attendance.getType());
            erpAttendance.setEmpName(attendance.getName());
            erpAttendance.setDate(convertDateToDateOnly.format(attendance.getDate()));
            erpAttendance.setInTime(attendance.getInTime());
            erpAttendance.setOutTime(attendance.getOutTime());
            erpAttendance.setComment(attendance.getComment());
            erpAttendance.setStatus(attendance.getStatus());
            erpAttendance.setComment(attendance.getComment());
            erpAttendance.setType(attendance.getType());
            erpAttendance.setApprovedBy(attendance.getApprovedBy());
            if (attendance.getApprovedDate() != null) {
                erpAttendance.setApprovedDate(convertDateToDateOnly.format(attendance.getApprovedDate()));
            }
            outObj.add(erpAttendance);
        }


        String url = "http://localhost:8080/main-erp/get-my-attendance/" + emp.get().getSerialNumber();

        RestTemplate restTemplate = new RestTemplate();

        ArrayList newList = ((ArrayList) restTemplate.getForObject(url, Map.class).get("data"));

        List<ErpAttendance> erpAttendances = new ArrayList<>();


        for (Object tempObj : newList) {

            ErpAttendance erpAttendance = new ErpAttendance();
            LinkedHashMap<String, String> temp = (LinkedHashMap) tempObj;
            erpAttendance.setDate(convertDateToDateOnly.parse(convertDateToDateOnly.format(convertStringToDate.parse(temp.get("createdAt").toString()))));
            erpAttendance.setCreatedTime(convertStringToDate.parse(temp.get("createdAt").toString()));
            erpAttendances.add(erpAttendance);

        }

        Map<Date, List<ErpAttendance>> groupedList = erpAttendances.stream().collect(groupingBy(ErpAttendance::getDate));

        if (groupedList.isEmpty()) {
            MyAttendance myAttendance = new MyAttendance();
            myAttendance.setId(id);
            myAttendance.setType("Today's Attendance");
            myAttendance.setEmpName(emp.get().getGivenName());
            myAttendance.setDate(convertDateToDateOnly.format(new Date()));
            myAttendance.setInTime("No Time Found");
            myAttendance.setOutTime("No Time Found");
            outObj.add(0, myAttendance);
        }
        groupedList.forEach((s, erpAttendances1) -> {
            MyAttendance myAttendance = new MyAttendance();
            myAttendance.setId(id);
            myAttendance.setType("Today's Attendance");
            myAttendance.setEmpName(emp.get().getGivenName());
            myAttendance.setDate(convertDateToDateOnly.format(s));

            if (erpAttendances1.size() == 1) {
                myAttendance.setInTime(convertDateToTime.format(erpAttendances1.get(0).getCreatedTime()));
                myAttendance.setOutTime("No Time Found");
            } else if (erpAttendances1.size() > 1) {
                Comparator<ErpAttendance> comparator2 = (c1, c2) -> {
                    return Long.valueOf(c1.getCreatedTime().getTime()).compareTo(c2.getCreatedTime().getTime());
                };
                Collections.sort(erpAttendances1, comparator2);

                myAttendance.setInTime(convertDateToTime.format(erpAttendances1.get(0).getCreatedTime()));
                myAttendance.setOutTime(convertDateToTime.format(erpAttendances1.get(erpAttendances1.size() - 1).getCreatedTime()));

            }

            outObj.add(0, myAttendance);
        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Attendance data find by id");
        responseList.setData(outObj);
        return responseList;
    }

    @Override
    public ResponseList getAllAttendanceDetails() {

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Attendance All data");
        responseList.setData(attendanceRepository.findAll());

        return responseList;
    }

    @Override
    public ResponseList getAttendanceByAttendanceID(Integer id) {
        Optional<AttendanceEntity> attendanceIssue = attendanceRepository.findById(id);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Attendance data find by id");
        responseList.setData(attendanceIssue.get());
        return responseList;

    }

    public ResponseList createAttendance(Integer id, CreateAttendance createAttendance) {

        Optional<AttendanceEntity> attendance = attendanceRepository.findById(id);

        AttendanceEntity attendanceObj = attendance.get();

        if (attendanceObj.getInTime().equalsIgnoreCase("")) {
            attendanceObj.setInTime(createAttendance.getInTimeNew());
        }
        if (attendanceObj.getOutTime().equalsIgnoreCase("")) {
            attendanceObj.setOutTime(createAttendance.getOutTimeNew());
        }
        attendanceObj.setComment(createAttendance.getComment());
        attendanceObj.setStatus(2);

        attendanceRepository.save(attendanceObj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList updateWithYesterdayAttendance() {

        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

        SimpleDateFormat convertDateToDateOnly = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat convertDateToDateOnlyWithTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        SimpleDateFormat convertDateToTime = new SimpleDateFormat("HH:mm:ss");

        String url = "http://localhost:8080/main-erp/get-my-yesterday-attendance";

        RestTemplate restTemplate = new RestTemplate();

        ArrayList newList = ((ArrayList) restTemplate.getForObject(url, Map.class).get("data"));

        List<YesterdayAttendance> erpAttendances = new ArrayList<>();


        for (Object tempObj : newList) {

            YesterdayAttendance yesterdayAttendance = new YesterdayAttendance();
            LinkedHashMap<String, String> temp = (LinkedHashMap) tempObj;
            yesterdayAttendance.setId(Integer.valueOf(String.valueOf(temp.get("id"))));
            yesterdayAttendance.setEmpId(Integer.valueOf(String.valueOf(temp.get("employeeId"))));
            yesterdayAttendance.setDate(String.valueOf(temp.get("createdAt")));
            erpAttendances.add(yesterdayAttendance);

        }

        List<EmpDetailEntity> peopleList = empDetailRepository.findAll();

        Map<Integer, List<YesterdayAttendance>> groupedList = erpAttendances.stream().collect(groupingBy(YesterdayAttendance::getEmpId));

        List<AttendanceEntity> attendanceObj = new ArrayList<>();

        peopleList.forEach(empDetailEntity -> {

            try {


                if (groupedList.containsKey(Integer.valueOf(empDetailEntity.getSerialNumber()))) {

                    AttendanceEntity attendance = new AttendanceEntity();
                    attendance.setName(empDetailEntity.getGivenName());
                    attendance.setEmpId(empDetailEntity.getId());
                    try {
                        attendance.setDate(convertDateToDateOnly.parse(convertDateToDateOnly.format(date)));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    List<YesterdayAttendance> timeList = groupedList.get(Integer.valueOf(empDetailEntity.getSerialNumber()));

                    Comparator<YesterdayAttendance> comparator = (c1, c2) -> {
                        return Integer.valueOf(c1.getId()).compareTo(c2.getId());
                    };
                    Collections.sort(timeList, comparator);
                    if (timeList.size() == 1) {
                        attendance.setInTime(convertDateToTime.format(convertDateToDateOnlyWithTime.parse(timeList.get(0).getDate())));
                        attendance.setOutTime("");
                        attendance.setStatus(1);
                    } else {
                        attendance.setStatus(3);
                        attendance.setInTime(convertDateToTime.format(convertDateToDateOnlyWithTime.parse(timeList.get(0).getDate())));
                        attendance.setOutTime(convertDateToTime.format(convertDateToDateOnlyWithTime.parse(timeList.get(timeList.size() - 1).getDate())));
                    }
                    attendance.setWorkDuration(0l);
                    attendance.setType("Added By System");

                    attendanceObj.add(attendance);
                } else {
                    AttendanceEntity attendance = new AttendanceEntity();
                    attendance.setName(empDetailEntity.getGivenName());
                    attendance.setEmpId(empDetailEntity.getId());
                    try {
                        attendance.setDate(convertDateToDateOnly.parse(convertDateToDateOnly.format(date)));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    attendance.setInTime("");
                    attendance.setOutTime("");

                    attendance.setWorkDuration(0l);
                    attendance.setType("Added By System");
                    attendance.setStatus(1);
                    attendanceObj.add(attendance);
                }
            }
            catch (Exception e){

            }


        });

        attendanceRepository.saveAll(attendanceObj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        return responseList;

    }


}
