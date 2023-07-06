package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.Attendance.*;
import com.taskmanager.task.repository.*;
import com.taskmanager.task.response.AttendanceDateRangeObj;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.response.leave.SupervisorLeaveList;
import com.taskmanager.task.service.AttendanceManager;
import com.taskmanager.task.service.PeopleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PeopleManagerImpl implements PeopleManager {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    LeaveRepository leaveRepository;


    @Autowired
    private EmpDetailRepository empDetailRepository;

    @Override
    public ResponseList updatePeopleTable() throws ParseException {

        String url = "http://localhost:8085/main-erp/people/update-people-list";

        RestTemplate restTemplate = new RestTemplate();

        List<EmpDetailEntity> list = new ArrayList<>();

        ArrayList newList = ((ArrayList) restTemplate.getForObject(url, Map.class).get("data"));

        for (Object tempObj : newList) {
            LinkedHashMap<String, String> temp = (LinkedHashMap) tempObj;
            List<EmpDetailEntity> currentEmp = empDetailRepository.findByNameInFull(String.valueOf(temp.get("nameInFull")));

            if (!currentEmp.isEmpty()){
                currentEmp.forEach(empDetailEntity -> {
                    empDetailEntity.setAdminId(String.valueOf(temp.get("adminId")) != "null" ? String.valueOf(temp.get("adminId")) : "0");
                    empDetailEntity.setEmployeeId(String.valueOf(temp.get("employeeId")) != "null" ? String.valueOf(temp.get("employeeId")) : "0");
                    empDetailEntity.setDesignation(String.valueOf(temp.get("designation")) != "null" ? String.valueOf(temp.get("designation")) : "0");
                    empDetailEntity.setEmpLevel(String.valueOf(temp.get("empLevel")) != "null" ? String.valueOf(temp.get("empLevel")) : "0");
                    empDetailEntity.setAdminDepartmentId(String.valueOf(temp.get("adminDepartmentId")) != "null" ? String.valueOf(temp.get("adminDepartmentId")) : "0");
                    empDetailEntity.setDeptId(String.valueOf(temp.get("deptId")) != "null" ? String.valueOf(temp.get("deptId")) : "0");
                    empDetailEntity.setFacultyId(String.valueOf(temp.get("facultyId")) != "null" ? String.valueOf(temp.get("facultyId")) : "0");
                    empDetailEntity.setTitleId(String.valueOf(temp.get("titleId")) != "null" ? String.valueOf(temp.get("titleId")) : "0");
                    empDetailEntity.setQualificationId(String.valueOf(temp.get("qualificationId")) != "null" ? String.valueOf(temp.get("qualificationId")) : "0");
                    empDetailEntity.setUniversityId(String.valueOf(temp.get("universityId")) != "null" ? String.valueOf(temp.get("universityId")) : "0");
                    empDetailEntity.setQualificationName(String.valueOf(temp.get("qualificationName")) != "null" ? String.valueOf(temp.get("qualificationName")) : "0");
                    empDetailEntity.setQualificationLevelId(String.valueOf(temp.get("qualificationLevelId")) != "null" ? String.valueOf(temp.get("qualificationLevelId")) : "0");
                    empDetailEntity.setAcademicCarderPositionId(String.valueOf(temp.get("academicCarderPositionId")) != "null" ? String.valueOf(temp.get("academicCarderPositionId")) : "0");
                    empDetailEntity.setSerialNumber(String.valueOf(temp.get("serialNumber")) != "null" ? String.valueOf(temp.get("serialNumber")) : "0");
                    empDetailEntity.setOldEmployeeNumber(String.valueOf(temp.get("oldEmployeeNumber")) != "null" ? String.valueOf(temp.get("oldEmployeeNumber")) : "0");
                    empDetailEntity.setEpfNumber(String.valueOf(temp.get("epfNumber")) != "null" ? String.valueOf(temp.get("epfNumber")) : "0");
                    empDetailEntity.setPersonType(String.valueOf(temp.get("personType")) != "null" ? String.valueOf(temp.get("personType")) : "0");
                    empDetailEntity.setStaffType(String.valueOf(temp.get("staffType")) != "null" ? String.valueOf(temp.get("staffType")) : "0");
                    empDetailEntity.setNameInFull(String.valueOf(temp.get("nameInFull")) != "null" ? String.valueOf(temp.get("nameInFull")) : "0");
                    empDetailEntity.setNameWithInit(String.valueOf(temp.get("nameWithInit")) != "null" ? String.valueOf(temp.get("nameWithInit")) : "0");
                    empDetailEntity.setGivenName(String.valueOf(temp.get("givenName")) != "null" ? String.valueOf(temp.get("givenName")) : "0");
                    empDetailEntity.setSurname(String.valueOf(temp.get("surname")) != "null" ? String.valueOf(temp.get("surname")) : "0");
//                    empDetailEntity.setDateOfBirth(String.valueOf(temp.get("dateOfBirth")) != "null" ? String.valueOf(temp.get("dateOfBirth")) : "0");
                    empDetailEntity.setNicNo(String.valueOf(temp.get("nicNo")) != "null" ? String.valueOf(temp.get("nicNo")) : "0");
                    empDetailEntity.setPassportNo(String.valueOf(temp.get("passportNo")) != "null" ? String.valueOf(temp.get("passportNo")) : "0");
                    empDetailEntity.setPermAddress(String.valueOf(temp.get("permAddress")) != "null" ? String.valueOf(temp.get("permAddress")) : "0");
                    empDetailEntity.setPermWorkAddress(String.valueOf(temp.get("permWorkAddress")) != "null" ? String.valueOf(temp.get("permWorkAddress")) : "0");
                    empDetailEntity.setContactNo(String.valueOf(temp.get("contactNo")) != "null" ? String.valueOf(temp.get("contactNo")) : "0");
                    empDetailEntity.setEmergencyContactName(String.valueOf(temp.get("emergencyContactName")) != "null" ? String.valueOf(temp.get("emergencyContactName")) : "0");
                    empDetailEntity.setEmergencyContactNumber(String.valueOf(temp.get("emergencyContactNumber")) != "null" ? String.valueOf(temp.get("emergencyContactNumber")) : "0");
                    empDetailEntity.setEmergencyContactAddress(String.valueOf(temp.get("emergencyContactAddress")) != "null" ? String.valueOf(temp.get("emergencyContactAddress")) : "0");
                    empDetailEntity.setEmergencyContactNic(String.valueOf(temp.get("emergencyContactNic")) != "null" ? String.valueOf(temp.get("emergencyContactNic")) : "0");
                    empDetailEntity.setEmergencyContactRalation(String.valueOf(temp.get("emergencyContactRalation")) != "null" ? String.valueOf(temp.get("emergencyContactRalation")) : "0");
                    empDetailEntity.setEmail(String.valueOf(temp.get("email")) != "null" ? String.valueOf(temp.get("email")) : "0");
                    empDetailEntity.setQualifiedYear(String.valueOf(temp.get("qualifiedYear")) != "null" ? String.valueOf(temp.get("qualifiedYear")) : "0");
                    empDetailEntity.setRemarks(String.valueOf(temp.get("remarks")) != "null" ? String.valueOf(temp.get("remarks")) : "0");
                    empDetailEntity.setStatus(String.valueOf(temp.get("status")) != "null" ? String.valueOf(temp.get("status")) : "0");
                    empDetailEntity.setHrEmployeeStatus(String.valueOf(temp.get("hrEmployeeStatus")) != "null" ? String.valueOf(temp.get("hrEmployeeStatus")) : "0");
                    empDetailEntity.setApprovalStatus(String.valueOf(temp.get("approvalStatus")) != "null" ? String.valueOf(temp.get("approvalStatus")) : "0");
                    empDetailEntity.setMinorStaffType(String.valueOf(temp.get("minorStaffType")) != "null" ? String.valueOf(temp.get("minorStaffType")) : "0");

//                    empDetailEntity.setCreatedBy(String.valueOf(temp.get("createdBy")));
//                    empDetailEntity.setUpdatedBy(String.valueOf(temp.get("updatedBy")));
//                    empDetailEntity.setDeletedBy(String.valueOf(temp.get("deletedBy")));
//                    empDetailEntity.setCreatedAt(String.valueOf(temp.get("createdAt")));
//                    empDetailEntity.setUpdatedAt(String.valueOf(temp.get("updatedAt")));
//                    empDetailEntity.setDeletedAt(String.valueOf(temp.get("deletedAt")));
                    list.add(empDetailEntity);
                });
            }
            else {
                EmpDetailEntity obj = new EmpDetailEntity();
                obj.setAdminId(String.valueOf(temp.get("adminId")) != "null" ? String.valueOf(temp.get("adminId")) : "0");
                obj.setEmployeeId(String.valueOf(temp.get("employeeId")) != "null" ? String.valueOf(temp.get("employeeId")) : "0");
                obj.setDesignation(String.valueOf(temp.get("designation")) != "null" ? String.valueOf(temp.get("designation")) : "0");
                obj.setEmpLevel(String.valueOf(temp.get("empLevel")) != "null" ? String.valueOf(temp.get("empLevel")) : "0");
                obj.setAdminDepartmentId(String.valueOf(temp.get("adminDepartmentId")) != "null" ? String.valueOf(temp.get("adminDepartmentId")) : "0");
                obj.setDeptId(String.valueOf(temp.get("deptId")) != "null" ? String.valueOf(temp.get("deptId")) : "0");
                obj.setFacultyId(String.valueOf(temp.get("facultyId")) != "null" ? String.valueOf(temp.get("facultyId")) : "0");
                obj.setTitleId(String.valueOf(temp.get("titleId")) != "null" ? String.valueOf(temp.get("titleId")) : "0");
                obj.setQualificationId(String.valueOf(temp.get("qualificationId")) != "null" ? String.valueOf(temp.get("qualificationId")) : "0");
                obj.setUniversityId(String.valueOf(temp.get("universityId")) != "null" ? String.valueOf(temp.get("universityId")) : "0");
                obj.setQualificationName(String.valueOf(temp.get("qualificationName")) != "null" ? String.valueOf(temp.get("qualificationName")) : "0");
                obj.setQualificationLevelId(String.valueOf(temp.get("qualificationLevelId")) != "null" ? String.valueOf(temp.get("qualificationLevelId")) : "0");
                obj.setAcademicCarderPositionId(String.valueOf(temp.get("academicCarderPositionId")) != "null" ? String.valueOf(temp.get("academicCarderPositionId")) : "0");
                obj.setSerialNumber(String.valueOf(temp.get("serialNumber")) != "null" ? String.valueOf(temp.get("serialNumber")) : "0");
                obj.setOldEmployeeNumber(String.valueOf(temp.get("oldEmployeeNumber")) != "null" ? String.valueOf(temp.get("oldEmployeeNumber")) : "0");
                obj.setEpfNumber(String.valueOf(temp.get("epfNumber")) != "null" ? String.valueOf(temp.get("epfNumber")) : "0");
                obj.setPersonType(String.valueOf(temp.get("personType")) != "null" ? String.valueOf(temp.get("personType")) : "0");
                obj.setStaffType(String.valueOf(temp.get("staffType")) != "null" ? String.valueOf(temp.get("staffType")) : "0");
                obj.setNameInFull(String.valueOf(temp.get("nameInFull")) != "null" ? String.valueOf(temp.get("nameInFull")) : "0");
                obj.setNameWithInit(String.valueOf(temp.get("nameWithInit")) != "null" ? String.valueOf(temp.get("nameWithInit")) : "0");
                obj.setGivenName(String.valueOf(temp.get("givenName")) != "null" ? String.valueOf(temp.get("givenName")) : "0");
                obj.setSurname(String.valueOf(temp.get("surname")) != "null" ? String.valueOf(temp.get("surname")) : "0");
//                obj.setDateOfBirth(String.valueOf(temp.get("dateOfBirth")) != "null" ? String.valueOf(temp.get("dateOfBirth")) : "0");
                obj.setNicNo(String.valueOf(temp.get("nicNo")) != "null" ? String.valueOf(temp.get("nicNo")) : "0");
                obj.setPassportNo(String.valueOf(temp.get("passportNo")) != "null" ? String.valueOf(temp.get("passportNo")) : "0");
                obj.setPermAddress(String.valueOf(temp.get("permAddress")) != "null" ? String.valueOf(temp.get("permAddress")) : "0");
                obj.setPermWorkAddress(String.valueOf(temp.get("permWorkAddress")) != "null" ? String.valueOf(temp.get("permWorkAddress")) : "0");
                obj.setContactNo(String.valueOf(temp.get("contactNo")) != "null" ? String.valueOf(temp.get("contactNo")) : "0");
                obj.setEmergencyContactName(String.valueOf(temp.get("emergencyContactName")) != "null" ? String.valueOf(temp.get("emergencyContactName")) : "0");
                obj.setEmergencyContactNumber(String.valueOf(temp.get("emergencyContactNumber")) != "null" ? String.valueOf(temp.get("emergencyContactNumber")) : "0");
                obj.setEmergencyContactAddress(String.valueOf(temp.get("emergencyContactAddress")) != "null" ? String.valueOf(temp.get("emergencyContactAddress")) : "0");
                obj.setEmergencyContactNic(String.valueOf(temp.get("emergencyContactNic")) != "null" ? String.valueOf(temp.get("emergencyContactNic")) : "0");
                obj.setEmergencyContactRalation(String.valueOf(temp.get("emergencyContactRalation")) != "null" ? String.valueOf(temp.get("emergencyContactRalation")) : "0");
                obj.setEmail(String.valueOf(temp.get("email")) != "null" ? String.valueOf(temp.get("email")) : "0");
                obj.setQualifiedYear(String.valueOf(temp.get("qualifiedYear")) != "null" ? String.valueOf(temp.get("qualifiedYear")) : "0");
                obj.setRemarks(String.valueOf(temp.get("remarks")) != "null" ? String.valueOf(temp.get("remarks")) : "0");
                obj.setStatus(String.valueOf(temp.get("status")) != "null" ? String.valueOf(temp.get("status")) : "0");
                obj.setHrEmployeeStatus(String.valueOf(temp.get("hrEmployeeStatus")) != "null" ? String.valueOf(temp.get("hrEmployeeStatus")) : "0");
                obj.setApprovalStatus(String.valueOf(temp.get("approvalStatus")) != "null" ? String.valueOf(temp.get("approvalStatus")) : "0");

//                obj.setCreatedBy(String.valueOf(temp.get("createdBy")));
//                obj.setUpdatedBy(String.valueOf(temp.get("updatedBy")));
//                obj.setDeletedBy(String.valueOf(temp.get("deletedBy")));
//                obj.setCreatedAt(String.valueOf(temp.get("createdAt")));
//                obj.setUpdatedAt(String.valueOf(temp.get("updatedAt")));
//                obj.setDeletedAt(String.valueOf(temp.get("deletedAt")));
                list.add(obj);
            }

        }

        empDetailRepository.saveAll(list);




        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        return responseList;
    }
}
