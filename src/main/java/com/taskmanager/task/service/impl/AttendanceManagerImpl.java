package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.Attendance.*;
import com.taskmanager.task.repository.*;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.response.leave.SupervisorLeaveList;
import com.taskmanager.task.service.AttendanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AttendanceManagerImpl implements AttendanceManager {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    LeaveRepository leaveRepository;

    @Autowired
    AvailableLeaveRepository availableLeaveRepository;

    @Autowired
    private EmpDetailRepository empDetailRepository;

    @Autowired
    private RosterRepository rosterRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private PayrollDetailsRepository payrollDetailsRepository;


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
        Collections.sort(attendanceIssue, comparator.reversed());

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
            if (attendance.getTotalWorkingHours() != null)
                erpAttendance.setTotalWorkingTime(Integer.parseInt(attendance.getTotalWorkingHours())/(60*60)+" Hour "+(Integer.parseInt(attendance.getTotalWorkingHours())%(3600))/60+" Min");
            if (attendance.getLateTime() != null)
                erpAttendance.setLateTime(Integer.parseInt(attendance.getLateTime())/(60*60)+" Hour "+(Integer.parseInt(attendance.getLateTime())%(3600))/60+" Min");
            if (attendance.getOtTime() != null)
                erpAttendance.setOtTime(Integer.parseInt(attendance.getOtTime())/(60*60)+" Hour "+(Integer.parseInt(attendance.getOtTime())%(3600))/60+" Min");

            erpAttendance.setAttendanceStatus(attendance.getPayRollStatus());

            outObj.add(erpAttendance);
        }
//        outObj.sort(Comparator.comparing(MyAttendance::getId).reversed());

        String url = "http://localhost:8080/main-erp/get-my-attendance/" + emp.get().getSerialNumber();

        RestTemplate restTemplate = new RestTemplate();

        ArrayList newList = ((ArrayList) restTemplate.getForObject(url, Map.class).get("data"));

        List<ErpAttendance> erpAttendances = new ArrayList<>();


        for (Object tempObj : newList) {

            ErpAttendance erpAttendance = new ErpAttendance();
            LinkedHashMap<String, String> temp = (LinkedHashMap) tempObj;
            erpAttendance.setDate(convertDateToDateOnly.parse(convertDateToDateOnly.format(convertStringToDateTypeTwo.parse(temp.get("dateTimeFullText").toString()))));
            erpAttendance.setCreatedTime(convertStringToDateTypeTwo.parse(temp.get("dateTimeFullText").toString()));
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
    public ResponseList getAttendanceByIDForApproval(int id) throws ParseException {

        SimpleDateFormat convertStringToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        SimpleDateFormat convertStringToDateTypeTwo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat convertDateToTime = new SimpleDateFormat("HH:mm:ss");

        SimpleDateFormat convertDateToDateOnly = new SimpleDateFormat("yyyy-MM-dd");

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(id);

        List<AttendanceEntity> attendanceIssue = attendanceRepository.findByEmpId(id);

        Comparator<AttendanceEntity> comparator = (c1, c2) -> {
            return Long.valueOf(c1.getDate().getTime()).compareTo(c2.getDate().getTime());
        };
        Collections.sort(attendanceIssue, comparator.reversed());

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
            if (attendance.getTotalWorkingHours() != null)
                erpAttendance.setTotalWorkingTime(Integer.parseInt(attendance.getTotalWorkingHours())/(60*60)+" Hour "+(Integer.parseInt(attendance.getTotalWorkingHours())%(3600))/60+" Min");
            if (attendance.getLateTime() != null)
                erpAttendance.setLateTime(Integer.parseInt(attendance.getLateTime())/(60*60)+" Hour "+(Integer.parseInt(attendance.getLateTime())%(3600))/60+" Min");
           if (attendance.getOtTime() != null)
                erpAttendance.setOtTime(Integer.parseInt(attendance.getOtTime())/(60*60)+" Hour "+(Integer.parseInt(attendance.getOtTime())%(3600))/60+" Min");

           if (attendance.getApplyOt() != null){
               erpAttendance.setApplyOt(attendance.getApplyOt());
           }
           else {
               erpAttendance.setApplyOt(0);
           }

           erpAttendance.setAttendanceStatus(attendance.getPayRollStatus());

            outObj.add(erpAttendance);
        }

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

        AttendanceEntity newObj = attendanceIssue.get();

        SimpleDateFormat convertDateToDateOnly = new SimpleDateFormat("yyyy-MM-dd");

        AttendanceEntityGetIdObj attendanceEntityGetIdObj = new AttendanceEntityGetIdObj();
        attendanceEntityGetIdObj.setId(newObj.getId());
        attendanceEntityGetIdObj.setName(newObj.getName());
        attendanceEntityGetIdObj.setEmpId(newObj.getEmpId());
        attendanceEntityGetIdObj.setDate(convertDateToDateOnly.format(newObj.getDate()));
        attendanceEntityGetIdObj.setInTime(newObj.getInTime());
        attendanceEntityGetIdObj.setOutTime(newObj.getOutTime());
        attendanceEntityGetIdObj.setWorkDuration(newObj.getWorkDuration());
        attendanceEntityGetIdObj.setType(newObj.getType());
        attendanceEntityGetIdObj.setComment(newObj.getComment());
        attendanceEntityGetIdObj.setApprovedBy(newObj.getApprovedBy());
        attendanceEntityGetIdObj.setApprovedDate(convertDateToDateOnly.format(newObj.getApprovedDate()));
        attendanceEntityGetIdObj.setStatus(newObj.getStatus());
        attendanceEntityGetIdObj.setApproved_by_id(newObj.getApproved_by_id());
        attendanceEntityGetIdObj.setSupervisorComment(newObj.getSupervisorComment());

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Attendance data find by id");
        responseList.setData(attendanceEntityGetIdObj);
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

        Date date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000 * 6);

        SimpleDateFormat convertDateToDateOnly = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat convertDateToDateOnlyWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            yesterdayAttendance.setDate(String.valueOf(temp.get("dateTimeFullText")));
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

    @Override
    public ResponseList getChildListForSupervisor(int id) {

        if (id == -1){
            List<EmpDetailEntity> list = empDetailRepository.findAll();

            List<SupervisorLeaveList> supervisorLists = new ArrayList<>();

            list.forEach(empDetailEntity -> {

                SupervisorLeaveList supervisorList = new SupervisorLeaveList();
                supervisorList.setId(empDetailEntity.getId());
                supervisorList.setEmail(empDetailEntity.getEmail());
                supervisorList.setNicNo(empDetailEntity.getNicNo());
                supervisorList.setGivenName(empDetailEntity.getGivenName());
                supervisorList.setNameInFull(empDetailEntity.getNameInFull());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setSupervisor(empDetailEntity.getSupervisor());

//            supervisorList.setDeleteRequested(attendanceRepository.getDeleteRequestedCount(empDetailEntity.getId()));
//                supervisorList.setPendingLeave(attendanceRepository.getPendingRequestedCount(empDetailEntity.getId()));

                supervisorLists.add(supervisorList);

            });

            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            responseList.setMsg("Success");
            responseList.setData(supervisorLists);

            return responseList;
        }
        else {

            List<EmpDetailEntity> list = empDetailRepository.findBySupervisor(id);

            List<SupervisorLeaveList> supervisorLists = new ArrayList<>();

            list.forEach(empDetailEntity -> {

                SupervisorLeaveList supervisorList = new SupervisorLeaveList();
                supervisorList.setId(empDetailEntity.getId());
                supervisorList.setEmail(empDetailEntity.getEmail());
                supervisorList.setNicNo(empDetailEntity.getNicNo());
                supervisorList.setGivenName(empDetailEntity.getGivenName());
                supervisorList.setNameInFull(empDetailEntity.getNameInFull());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setSupervisor(empDetailEntity.getSupervisor());

//            supervisorList.setDeleteRequested(attendanceRepository.getDeleteRequestedCount(empDetailEntity.getId()));
                supervisorList.setPendingLeave(attendanceRepository.getPendingRequestedCount(empDetailEntity.getId()));

                supervisorLists.add(supervisorList);

            });

            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            responseList.setMsg("Success");
            responseList.setData(supervisorLists);

            return responseList;
        }

    }

    @Override
    public ResponseList getMinorStaffList(int id) {

        if (id == -1){
            List<EmpDetailEntity> list = empDetailRepository.findAll();

            List<SupervisorLeaveList> supervisorLists = new ArrayList<>();

            list.forEach(empDetailEntity -> {

                if (empDetailEntity.getPersonType().equalsIgnoreCase("minor staff")) {

                    SupervisorLeaveList supervisorList = new SupervisorLeaveList();
                    supervisorList.setId(empDetailEntity.getId());
                    supervisorList.setEmail(empDetailEntity.getEmail());
                    supervisorList.setNicNo(empDetailEntity.getNicNo());
                    supervisorList.setGivenName(empDetailEntity.getGivenName());
                    supervisorList.setNameInFull(empDetailEntity.getNameInFull());
                    supervisorList.setContactNo(empDetailEntity.getContactNo());
                    supervisorList.setContactNo(empDetailEntity.getContactNo());
                    supervisorList.setSupervisor(empDetailEntity.getSupervisor());

                    supervisorLists.add(supervisorList);
                }

            });

            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            responseList.setMsg("Success");
            responseList.setData(supervisorLists);

            return responseList;
        }
        else {

            List<EmpDetailEntity> list = empDetailRepository.findBySupervisor(id);

            List<SupervisorLeaveList> supervisorLists = new ArrayList<>();

            list.forEach(empDetailEntity -> {

                SupervisorLeaveList supervisorList = new SupervisorLeaveList();
                supervisorList.setId(empDetailEntity.getId());
                supervisorList.setEmail(empDetailEntity.getEmail());
                supervisorList.setNicNo(empDetailEntity.getNicNo());
                supervisorList.setGivenName(empDetailEntity.getGivenName());
                supervisorList.setNameInFull(empDetailEntity.getNameInFull());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setContactNo(empDetailEntity.getContactNo());
                supervisorList.setSupervisor(empDetailEntity.getSupervisor());

                supervisorList.setPendingLeave(attendanceRepository.getPendingRequestedCount(empDetailEntity.getId()));

                supervisorLists.add(supervisorList);

            });

            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            responseList.setMsg("Success");
            responseList.setData(supervisorLists);

            return responseList;
        }

    }


    @Override
    public ResponseList changeStatusForAttendance(int attendance, int user, int status, String comment) {

        Optional<AttendanceEntity> attendanceObj = attendanceRepository.findById(attendance);

        AttendanceEntity obj = attendanceObj.get();


        obj.setStatus(status);
        obj.setApprovedBy(empDetailRepository.findById(user).get().getNameInFull());
        obj.setApproved_by_id(user);
        obj.setApprovedDate(new Date());
        obj.setComment(comment);

        attendanceRepository.save(obj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList getTodayInTime(int id) throws ParseException {

        ResponseList result = getAttendanceByID(id);

        List<MyAttendance> attendance = (List<MyAttendance>) result.getData();

        MyAttendance todayTime = attendance.get(0);

        TodayAttendance todayAttendance = new TodayAttendance();

        if (todayTime.getInTime().equalsIgnoreCase("No Time Found")){
            todayAttendance.setInTime("No Time Found");
            todayAttendance.setMsg("No Time Found");
            todayAttendance.setLate(true);
        }
        else {
            todayAttendance.setInTime(todayTime.getInTime());
            if (Integer.parseInt((todayTime.getInTime().split(":")[0])) < 9) {
                todayAttendance.setMsg("On Time Good Luck");
                todayAttendance.setLate(false);
            }
            else {
                todayAttendance.setMsg("Your Late");
                todayAttendance.setLate(true);
            }

        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(todayAttendance);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList getAttendanceStat(int id) throws ParseException {

        TodayAttendance todayInTime = (TodayAttendance) getTodayInTime(id).getData();

        List<AttendanceStat> attendanceStatList = new ArrayList<>();

        AttendanceStat attendanceStat1 = new AttendanceStat("TrendingUpIcon",
                "light-primary", todayInTime.getInTime(), "In Time","mb-2 mb-xl-0");

        AttendanceStat attendanceStat2 = new AttendanceStat("UserIcon",
                "light-info", String.valueOf(leaveRepository.getPendingRequestedCount(id)), "Pending Leaves","mb-2 mb-xl-0");

        AttendanceStat attendanceStat3 = new AttendanceStat("BoxIcon",
                "light-danger", String.valueOf(availableLeaveRepository.getAvailableOffDayLeave(id)), "Available Day Off","mb-2 mb-sm-0");

        AttendanceStat attendanceStat4 = new AttendanceStat("DollarSignIcon",
                "light-success", String.valueOf(rosterRepository.getAvailableRosterCount(id)), "Pending Roster Count","mb-2 mb-sm-0");


        attendanceStatList.add(attendanceStat1);
        attendanceStatList.add(attendanceStat2);
        attendanceStatList.add(attendanceStat3);
        attendanceStatList.add(attendanceStat4);


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(attendanceStatList);
        responseList.setMsg("Success");

        return responseList;


    }

    @Override
    public ResponseList getTotalLeavesAndTakenLeaves(int id) throws ParseException {

        Integer allLeaves = availableLeaveRepository.getAllLeaves(id);

        Integer availableLeaves = availableLeaveRepository.getAvailable(id);

        AllAvailableAndTakenLeaves allAvailableAndTakenLeaves = new AllAvailableAndTakenLeaves(
                String.valueOf(allLeaves-availableLeaves),String.valueOf(availableLeaves));

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(allAvailableAndTakenLeaves);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList getTotalPenalty(int id) throws ParseException {

        Integer totalAmount = penaltyRepository.getAllPenaltyValue(id);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(totalAmount);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList updatePastMonthAttendance() throws ParseException {

        List<AttendanceEntity> list = attendanceRepository.getPastMonthAttendance();

        List<AttendanceEntity> newList = new ArrayList<>();

        list.forEach(attendanceEntity -> {
            try {

                LocalTime outTime = LocalTime.parse(attendanceEntity.getOutTime());
                LocalTime inTime = LocalTime.parse(attendanceEntity.getInTime());
                Duration duration = Duration.between(inTime, outTime);
                long seconds = duration.getSeconds();

                attendanceEntity.setTotalWorkingHours(String.valueOf(seconds));

                long diff = 32400 - seconds;

                if (diff > 0) {
                    attendanceEntity.setLateTime(String.valueOf(diff));
                    attendanceEntity.setOtTime("0");
                } else if (diff < 0) {
                    attendanceEntity.setLateTime("0");
                    attendanceEntity.setOtTime(String.valueOf(diff * (-1)));
                } else {
                    attendanceEntity.setOtTime("0");
                    attendanceEntity.setLateTime("0");
                }
            }
            catch (Exception e){}
            newList.add(attendanceEntity);
        });


        attendanceRepository.saveAll(newList);
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList changeStatus(Integer id, Integer status) throws ParseException {

        if (status == 2 || status == 3){
            ResponseList objList = getAttendanceByIDForApproval(id);

            List<AttendanceEntity> list = new ArrayList<>();

            for (MyAttendance datum : (List<MyAttendance>) objList.getData()) {
                Optional<AttendanceEntity> detail = attendanceRepository.findById(datum.getId());

                AttendanceEntity obj = detail.get();
                if (status == 2)
                    obj.setApplyOt(1);
                if (status == 3)
                    obj.setApplyOt(0);
                list.add(obj);
            }
            attendanceRepository.saveAll(list);

            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            responseList.setMsg("Success");

            return responseList;

        }
        else {
            Optional<AttendanceEntity> detail = attendanceRepository.findById(id);

            AttendanceEntity obj = detail.get();

            if (status == 1) {
                Integer val = obj.getApplyOt() != null ? obj.getApplyOt() : 0;
                obj.setApplyOt(val == 1 ? 0 : 1);
            }

            attendanceRepository.save(obj);

            ResponseList responseList = new ResponseList();
            responseList.setCode(200);
            responseList.setMsg("Success");

            return responseList;
        }

    }

    @Override
    public ResponseList updateLeaveWithAttendance() throws ParseException {

        List<LeaveEntity> listObj = leaveRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        SimpleDateFormat convertDateToDateOnly = new SimpleDateFormat("yyyy-MM-dd");
        List<AttendanceEntity> newObj = new ArrayList<>();
        listObj.forEach(leaveEntity -> {
            if (leaveEntity.getStatus() == 5) {
                String s = convertDateToDateOnly.format(leaveEntity.getFromDate());
                String e = convertDateToDateOnly.format(leaveEntity.getToDate());
                LocalDate start = LocalDate.parse(s);
                LocalDate end = LocalDate.parse(e);
                List<LocalDate> totalDates = new ArrayList<>();
                while (!start.isAfter(end)) {
                    totalDates.add(start);
                    start = start.plusDays(1);
                }
                int val = 0;
                for (LocalDate date : totalDates) {
                    val++;
                    Date d1 = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    List<AttendanceEntity> attendanceForLEave = attendanceRepository
                            .getAttendanceByDateAndEmpID(convertDateToDateOnly.format(d1), leaveEntity.getEmpId());

                    if (!attendanceForLEave.isEmpty()) {
                        AttendanceEntity tempObj = attendanceForLEave.get(0);
                        tempObj.setLeaveId(leaveEntity.getId());
                        Float tempNum = leaveEntity.getTotalLeave() % 1;
                        int finalTempNum = (int) (tempNum * 10);
                        if (finalTempNum != 0 && val == totalDates.size())
                            tempObj.setLeaveTime(16200);
                        else
                            tempObj.setLeaveTime(32400);


                        newObj.add(tempObj);

                    }
                }
            }
        });

        attendanceRepository.saveAll(newObj);
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList updateRosterDatesWithAttendance() throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleDateFormat convertDateToDateOnly = new SimpleDateFormat("yyyy-MM-dd");

        List<AttendanceEntity> listObj = attendanceRepository.findAll();

        List<AttendanceEntity> newList = new ArrayList<>();

        Map<Integer, List<AttendanceEntity>> groupOt = listObj.stream().collect(groupingBy(AttendanceEntity::getEmpId));

        groupOt.forEach((integer, attendanceEntities) -> {

            attendanceEntities.forEach(attendanceEntity -> {

                LocalDate date = LocalDate
                        .parse(convertDateToDateOnly.format(attendanceEntity.getDate()), formatter);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                String dayName = dayOfWeek.name().toLowerCase();
                if (dayName.equalsIgnoreCase("sunday") || dayName.equalsIgnoreCase("saturday")){
                    if (dayName.equalsIgnoreCase("saturday") && ((attendanceEntity.getLeaveTime() == null || (attendanceEntity.getLeaveTime() != null && attendanceEntity.getLeaveTime() < 16200)) && attendanceEntity.getTotalWorkingHours() == null)){
                        attendanceEntity.setIsWorkingDay(-999F);
                        attendanceEntity.setPayRollStatus(-1);//no pay
                    }
                    else if (dayName.equalsIgnoreCase("saturday") && attendanceEntity.getLeaveTime()!= null && attendanceEntity.getLeaveTime() > 0){
                        attendanceEntity.setIsWorkingDay(-0.5F);
                        attendanceEntity.setPayRollStatus(0);//saturday half day
                    }
                    else if (dayName.equalsIgnoreCase("sunday") && (attendanceEntity.getTotalWorkingHours() == null || (attendanceEntity.getTotalWorkingHours() != null && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) == 0) )){
                        attendanceEntity.setIsWorkingDay(-1F);
                        attendanceEntity.setPayRollStatus(1);//sunday leave
                    }
                    else {
                        String tempDate = convertDateToDateOnly.format(attendanceEntity.getDate());
                        List<RosterEntity> roster = rosterRepository.getRosterBetweenDateAndForUserId(tempDate, attendanceEntity.getEmpId());


                        if (roster.isEmpty()  && dayName.equalsIgnoreCase("saturday") && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) < 16200){
                            attendanceEntity.setIsWorkingDay(0.5F);
                            attendanceEntity.setPayRollStatus(2);//saturday normal work
                        }
                        else if (!roster.isEmpty() && dayName.equalsIgnoreCase("saturday") && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) > 0){
                            attendanceEntity.setIsWorkingDay(1F);//saturday full and day off cover
                            attendanceEntity.setPayRollStatus(3);
                        }
                        else if (dayName.equalsIgnoreCase("sunday") && !roster.isEmpty() && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) > 16200 ){
                            attendanceEntity.setIsWorkingDay(1F);//sunday full day
                            attendanceEntity.setPayRollStatus(4);
                        }
                        else if (dayName.equalsIgnoreCase("sunday") && !roster.isEmpty() && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) > 0 ){
                            attendanceEntity.setIsWorkingDay(-0.5F);//sunday half day
                            attendanceEntity.setPayRollStatus(5);
                        }
                        else if (dayName.equalsIgnoreCase("sunday") && roster.isEmpty() && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) > 16200) {
                            attendanceEntity.setIsExtraWorking(1); //
                            attendanceEntity.setPayRollStatus(6); //sunday extra full work
                        }
                        else if (dayName.equalsIgnoreCase("sunday") && roster.isEmpty() && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) > 0) {
                            attendanceEntity.setIsExtraWorking(1); //
                            attendanceEntity.setPayRollStatus(12); //sunday extra half work
                        }
                        else if (dayName.equalsIgnoreCase("saturday") && roster.isEmpty() && Integer.parseInt(attendanceEntity.getTotalWorkingHours()) > 16200) {
                            attendanceEntity.setIsExtraWorking(1);
                            attendanceEntity.setIsWorkingDay(0.5F);//saturday normal day and extra work
                            attendanceEntity.setPayRollStatus(7);
                        }
                        else{
                            System.out.println("");
                        }
                    }
                }
                else {
                    if ((attendanceEntity.getLeaveTime() == null || (attendanceEntity.getLeaveTime() != null && attendanceEntity.getLeaveTime() < 16200)) && attendanceEntity.getTotalWorkingHours() == null){
                        attendanceEntity.setIsWorkingDay(-999F);
                        attendanceEntity.setPayRollStatus(8);//weekday no pay
                    }
                    if (attendanceEntity.getLeaveTime()!= null && attendanceEntity.getLeaveTime() == 16200){
                        attendanceEntity.setIsWorkingDay(-0.5F);
                        attendanceEntity.setPayRollStatus(9);//week day half day
                    }
                    else if (attendanceEntity.getLeaveTime()!= null && attendanceEntity.getLeaveTime() == 32400){
                        attendanceEntity.setIsWorkingDay(-1F);
                        attendanceEntity.setPayRollStatus(10); //week day full day leave
                    }
                    else {
                        attendanceEntity.setIsWorkingDay(1F); //
                        attendanceEntity.setPayRollStatus(11); //week day working day
                    }
                }


                newList.add(attendanceEntity);

            });
        });

        attendanceRepository.saveAll(newList);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList processOt(Integer id, Integer status) throws ParseException {
        //Don't Run
        List<AttendanceEntity> listObj = attendanceRepository.findAll();

        List<AttendanceEntity> newList = new ArrayList<>();

        Map<Integer, List<AttendanceEntity>> groupOt = listObj.stream().collect(groupingBy(AttendanceEntity::getEmpId));

        groupOt.forEach((integer, attendanceEntities) -> {

            attendanceEntities.forEach(attendanceEntity -> {

                Optional<PayrollEntityDetails> detailConfig = payrollDetailsRepository
                        .findById(attendanceEntity.getEmpId());

                if (!detailConfig.isEmpty() && detailConfig.get().getIsOt() == 1){
                    if (detailConfig.get().getIsOtBasic() != null && detailConfig.get().getIsOtBasic() == 1
                            && attendanceEntity.getOtTime()!=null &&
                            (Integer.parseInt(attendanceEntity.getOtTime())/60) >60
                            && attendanceEntity.getLeaveTime() != null &&  attendanceEntity.getLeaveTime() > 0){
                        attendanceEntity.setOtAmount((float) ((detailConfig.get().getBasicSalary()/240)/1.5));

                    }
                    else if (detailConfig.get().getIsOtBasic() != null && detailConfig.get().getIsOtBasic() == 0
                            && attendanceEntity.getOtTime()!=null &&
                            (Integer.parseInt(attendanceEntity.getOtTime())/60) >60
                            && attendanceEntity.getLeaveTime() != null &&  attendanceEntity.getLeaveTime() > 0){
                        attendanceEntity.setOtAmount((float) ((detailConfig.get().getGrossSalary()/240)/1.5));
                    }
                }



            });

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }


    @Override
    public ResponseList setPayRollStatus() {

        List<AttendanceEntity> listObj = attendanceRepository.getPastMonthAttendance();

        List<AttendanceEntity> newList = new ArrayList<>();

        Map<Integer, List<AttendanceEntity>> groupOt = listObj.stream()
                .collect(groupingBy(AttendanceEntity::getEmpId));


        groupOt.forEach((integer, attendanceEntities) -> {

//            List<AttendanceEntity> result = attendanceEntities.stream()
//                    .collect(Collectors.toMap(AttendanceEntity::getDate, Function.identity(), (obj1, obj2) -> obj1))
//                    .values()
//                    .stream().toList();

            attendanceEntities.forEach(attendanceEntity -> {

                if (attendanceEntity.getIsWorkingDay() != null && attendanceEntity.getIsWorkingDay() == -1F){
                    attendanceEntity.setPayRollStatus(-1);//no pay
                }
                if (attendanceEntity.getLeaveTime() != null && attendanceEntity.getLeaveTime() > 0 && attendanceEntity.getIsWorkingDay() != null && attendanceEntity.getIsWorkingDay() == 0.5F){
                    attendanceEntity.setPayRollStatus(1);//half day leave
                }
                else if (attendanceEntity.getLeaveTime() != null &&attendanceEntity.getLeaveTime() > 0 && attendanceEntity.getIsWorkingDay() != null && attendanceEntity.getIsWorkingDay() == 1F){
                    attendanceEntity.setPayRollStatus(2);//full day leave
                }
                else if (attendanceEntity.getIsWorkingDay() != null && attendanceEntity.getIsWorkingDay() == 1F){
                    attendanceEntity.setPayRollStatus(3);
                }
                else if (attendanceEntity.getIsWorkingDay() != null && attendanceEntity.getIsWorkingDay() == 0.5F){
                    attendanceEntity.setPayRollStatus(4);
                }
                else if (attendanceEntity.getIsExtraWorking() != null && attendanceEntity.getIsExtraWorking() == 1 && attendanceEntity.getIsWorkingDay() != null && attendanceEntity.getIsWorkingDay() == 0.5F){
                    attendanceEntity.setPayRollStatus(5);//saturday extra
                }
                else if (attendanceEntity.getIsExtraWorking() != null && attendanceEntity.getIsExtraWorking() == 1 && attendanceEntity.getIsWorkingDay() == null){
                    attendanceEntity.setPayRollStatus(6);//sunday extra
                }

                newList.add(attendanceEntity);


            });

        });

        attendanceRepository.saveAll(newList);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }


}
