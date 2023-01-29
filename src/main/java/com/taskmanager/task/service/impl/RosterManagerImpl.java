package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.RosterEntity;
import com.taskmanager.task.model.roster.*;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.RosterRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.RosterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class RosterManagerImpl implements RosterManager {

    @Autowired
    RosterRepository rosterRepository;

    @Autowired
    private EmpDetailRepository empDetailRepository;


    @Override
    public ResponseList addMyRoster(CreateRoster createRoster, Integer user) throws ParseException {

        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);

        List<RosterEntity> list = new ArrayList<>();

        for (UserRoster users : createRoster.getExtendedProps().getGuests()) {
            RosterEntity rosterEntity = new RosterEntity();
            rosterEntity.setTitle(createRoster.getTitle());
            rosterEntity.setCalendar(createRoster.getExtendedProps().getCalendar());
            rosterEntity.setStartDate(createRoster.getStart());
            rosterEntity.setEndDate(createRoster.getEnd());
            rosterEntity.setUser(Integer.parseInt(users.getName().split("-")[1]));
            rosterEntity.setCreatedUser(user);
            rosterEntity.setAllDay(createRoster.getAllDay() ? 1 : 0);
            rosterEntity.setDescription(createRoster.getExtendedProps().getDescription());
            rosterEntity.setStatus(1);
            rosterEntity.setUniqueId(datetime);
            list.add(rosterEntity);
        }


        rosterRepository.saveAll(list);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        return responseList;

    }

    @Override
    public ResponseList getMyRoster(Integer user) throws ParseException {

        List<RosterEntity> roster = rosterRepository.findByUserOrderByIdDesc(user);

        List<GetRoster> getMyRoster = new ArrayList<>();

        roster.forEach(rosterEntity -> {

            GetRoster getRoster = new GetRoster();
            getRoster.setId(rosterEntity.getId());
            getRoster.setUrl("");
            getRoster.setTitle(rosterEntity.getTitle());
            getRoster.setStart(rosterEntity.getStartDate());
            getRoster.setEnd(rosterEntity.getEndDate());
            getRoster.setAllDay(rosterEntity.getAllDay() == 1);

            ExtendedRosterForGetRoster extendedRosterForGetRoster = new ExtendedRosterForGetRoster();
            extendedRosterForGetRoster.setCalendar(rosterEntity.getCalendar());
            extendedRosterForGetRoster.setDescription(rosterEntity.getDescription());
            getRoster.setExtendedProps(extendedRosterForGetRoster);

            if (!(rosterEntity.getStatus() == 7 || rosterEntity.getStatus() == 5))
                getMyRoster.add(getRoster);

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(getMyRoster);
        return responseList;
    }

    @Override
    public ResponseList getMyRosterForTable(Integer user, Integer status) throws ParseException {
        List<RosterEntity> roster = rosterRepository.findByUserOrderByIdDesc(user);


        List<GetRosterForTable> getMyRoster = new ArrayList<>();

        roster.forEach(rosterEntity -> {


            GetRosterForTable getRoster = new GetRosterForTable();
            getRoster.setId(rosterEntity.getId());
            getRoster.setUrl("");
            getRoster.setTitle(rosterEntity.getTitle());
            getRoster.setStart(rosterEntity.getStartDate());
            getRoster.setEnd(rosterEntity.getEndDate());
            getRoster.setAllDay(rosterEntity.getAllDay() == 1);
            getRoster.setCalendar(rosterEntity.getCalendar());
            getRoster.setDescription(rosterEntity.getDescription());
            getRoster.setStatus(rosterEntity.getStatus());
            getRoster.setRequestedForName(rosterEntity.getRequestForName());
            getRoster.setRequestedBy(rosterEntity.getRequestedUserName());
            if (status == 0) {
                getMyRoster.add(getRoster);
            }
            else if (status == rosterEntity.getStatus()) {
                getMyRoster.add(getRoster);
            }

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(getMyRoster);
        return responseList;
    }

    @Override
    public ResponseList getMyRosterForId(Integer user, Integer id) throws ParseException {
        Optional<RosterEntity> roster = rosterRepository.findById(id);

        RosterEntity rosterEntity = roster.get();

        GetRosterForTable getRoster = new GetRosterForTable();
        getRoster.setId(rosterEntity.getId());
        getRoster.setUrl("");
        getRoster.setTitle(rosterEntity.getTitle());
        getRoster.setStart(rosterEntity.getStartDate());
        getRoster.setEnd(rosterEntity.getEndDate());
        getRoster.setAllDay(rosterEntity.getAllDay() == 1);
        getRoster.setCalendar(rosterEntity.getCalendar());
        getRoster.setDescription(rosterEntity.getDescription());
        getRoster.setStatus(rosterEntity.getStatus());

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(getRoster);
        return responseList;
    }

    @Override
    public ResponseList getUserList(Integer type, Integer user) throws ParseException {

        int x = 0;

        List<String> name = new ArrayList<>();

        List<EmpDetailEntity> empList = empDetailRepository.findAll();

        List<GetRosterEmpList> getRosterEmpLists = new ArrayList<>();

        for (EmpDetailEntity emp : empList) {

            x++;

            if (!name.contains(emp.getNameInFull())) {
                GetRosterEmpList getRosterEmpList = new GetRosterEmpList();
                getRosterEmpList.setAvatar("");
                getRosterEmpList.setName(emp.getNameInFull() + "-" + emp.getId());
                name.add(emp.getNameInFull());
                getRosterEmpLists.add(getRosterEmpList);
            }

        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(getRosterEmpLists);
        return responseList;

    }

    @Override
    public ResponseList requestChangeUser(Integer task, Integer user, List<UserRoster> users, String reason) throws ParseException {

        Optional<RosterEntity> roster = rosterRepository.findById(task);

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(user);

        RosterEntity rosterEntity = roster.get();

        List<RosterEntity> newList = new ArrayList<>();

        String name = "";

        for (UserRoster userRoster : users){



            RosterEntity tempRosterEntity = (RosterEntity) rosterEntity.clone();
            tempRosterEntity.setId(null);
            tempRosterEntity.setUser(Integer.parseInt(userRoster.getName().split("-")[1]));
            tempRosterEntity.setStatus(3);
            tempRosterEntity.setReason(reason);
            tempRosterEntity.setSubTask(rosterEntity.getId());
            tempRosterEntity.setRequestedUserId(emp.get().getId());
            tempRosterEntity.setRequestedUserName(emp.get().getNameInFull());
            tempRosterEntity.setRequestForId(Integer.parseInt(userRoster.getName().split("-")[1]));
            name += userRoster.getName().split("-")[0] + ",";
            tempRosterEntity.setRequestForName(userRoster.getName().split("-")[0]);
            newList.add(tempRosterEntity);

        }
        StringBuffer sb= new StringBuffer(name);
        sb.deleteCharAt(sb.length()-1);

        rosterEntity.setStatus(3);
        rosterEntity.setRequestForName(sb.toString());
        rosterEntity.setRequestedUserName(emp.get().getNameInFull());
        newList.add(rosterEntity);

        rosterRepository.saveAll(newList);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        return responseList;
    }

    @Override
    public ResponseList changeStatus(Integer task, Integer user, Integer status) throws ParseException {

        Optional<RosterEntity> roster = rosterRepository.findById(task);

        RosterEntity rosterEntity = roster.get();

        rosterEntity.setStatus(status);

        rosterRepository.save(rosterEntity);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        return responseList;


    }

    @Override
    public ResponseList getChildListForSupervisor(int id) {

        List<EmpDetailEntity> list = empDetailRepository.findBySupervisor(id);

        List<SupervisorRosterList> supervisorLists = new ArrayList<>();

        list.forEach(empDetailEntity -> {

            SupervisorRosterList supervisorList = new SupervisorRosterList();
            supervisorList.setId(empDetailEntity.getId());
            supervisorList.setEmail(empDetailEntity.getEmail());
            supervisorList.setNicNo(empDetailEntity.getNicNo());
            supervisorList.setGivenName(empDetailEntity.getGivenName());
            supervisorList.setNameInFull(empDetailEntity.getNameInFull());
            supervisorList.setContactNo(empDetailEntity.getContactNo());
            supervisorList.setContactNo(empDetailEntity.getContactNo());
            supervisorList.setSupervisor(empDetailEntity.getSupervisor());

            supervisorList.setPendingRoster(rosterRepository.getPendingCount(empDetailEntity.getId()));
            supervisorList.setChangeRequested(rosterRepository.getChangeRequestedCount(empDetailEntity.getId()));
            supervisorList.setReviewNeeded(rosterRepository.getReviewNeededCount(empDetailEntity.getId()));

            supervisorLists.add(supervisorList);

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(supervisorLists);

        return responseList;

    }

    @Override
    public ResponseList getMyLatestRoster(Integer user) throws ParseException {

        List<RosterEntity> roster = rosterRepository.findByUserOrderByIdDesc(user);

        RosterEntity obj = roster.get(0);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(obj);

        return responseList;
    }
}
