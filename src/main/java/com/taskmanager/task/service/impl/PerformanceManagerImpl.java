package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.Attendance.*;
import com.taskmanager.task.repository.*;
import com.taskmanager.task.response.AttendanceDateRangeObj;
import com.taskmanager.task.response.PerformanceDetailObj;
import com.taskmanager.task.response.PerformanceOutObj;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.response.leave.SupervisorLeaveList;
import com.taskmanager.task.service.AttendanceManager;
import com.taskmanager.task.service.PerformanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PerformanceManagerImpl implements PerformanceManager {

    @Autowired
    PerformanceRepository performanceRepository;


    @Override
    public ResponseList getPerformanceByUserId(Integer id) {

        List<PerformanceEntity> performance = performanceRepository.findByUserId(id);

        PerformanceOutObj performanceOutObj = new PerformanceOutObj();

        List<PerformanceDetailObj> list = new ArrayList<>();

        PerformanceDetailObj performanceDetailObj1 = new PerformanceDetailObj();
        performanceDetailObj1.setId(1);
        performanceDetailObj1.setCategory("Task Estimation");
        performanceDetailObj1.setScore(String.valueOf(performance.get(0).getScore1())+"%");
        performanceDetailObj1.setIdealRating("100%");
        performanceDetailObj1.setRank("N/A");

        PerformanceDetailObj performanceDetailObj2 = new PerformanceDetailObj();
        performanceDetailObj2.setId(2);
        performanceDetailObj2.setCategory("Task Rating");
        performanceDetailObj2.setScore(String.valueOf(performance.get(0).getScore2())+"%");
        performanceDetailObj2.setIdealRating("100%");
        performanceDetailObj2.setRank("N/A");

        PerformanceDetailObj performanceDetailObj3 = new PerformanceDetailObj();
        performanceDetailObj3.setId(3);
        performanceDetailObj3.setCategory("Working Hour Score");
        performanceDetailObj3.setScore(String.valueOf(performance.get(0).getScore3())+"%");
        performanceDetailObj3.setIdealRating("100%");
        performanceDetailObj3.setRank("N/A");

        PerformanceDetailObj performanceDetailObj4 = new PerformanceDetailObj();
        performanceDetailObj4.setId(4);
        performanceDetailObj4.setCategory("OT Hour Score");
        performanceDetailObj4.setScore(String.valueOf(performance.get(0).getScore4())+"%");
        performanceDetailObj4.setIdealRating("100%");
        performanceDetailObj4.setRank("N/A");

        PerformanceDetailObj performanceDetailObj5 = new PerformanceDetailObj();
        performanceDetailObj5.setId(5);
        performanceDetailObj5.setCategory("Sunday Work Score");
        performanceDetailObj5.setScore(String.valueOf(performance.get(0).getScore5())+"%");
        performanceDetailObj5.setIdealRating("100%");
        performanceDetailObj5.setRank("N/A");


        PerformanceDetailObj performanceDetailObj6 = new PerformanceDetailObj();
        performanceDetailObj6.setId(6);
        performanceDetailObj6.setCategory("No Pay Score");
        performanceDetailObj6.setScore(String.valueOf(performance.get(0).getScore6())+"%");
        performanceDetailObj6.setIdealRating("100%");
        performanceDetailObj6.setRank("N/A");

        list.add(performanceDetailObj1);
        list.add(performanceDetailObj2);
        list.add(performanceDetailObj3);
        list.add(performanceDetailObj4);
        list.add(performanceDetailObj5);
        list.add(performanceDetailObj6);


        Float total = (float) (performance.get(0).getScore1()*0.6 + performance.get(0).getScore2() * 0.1
                        + performance.get(0).getScore7() * 0.2 + performance.get(0).getScore6()* 0.1);

        performanceOutObj.setName(performance.get(0).getName());
        performanceOutObj.setPerformanceDetailObj(list);
        performanceOutObj.setStatus("N/A");
        performanceOutObj.setRank("N/A");
        performanceOutObj.setTotalTaskManagerScore((performance.get(0).getScore1() + performance.get(0).getScore2())/2);
        performanceOutObj.setTotalAttendanceScore((performance.get(0).getScore7()));
        performanceOutObj.setTotalNoPayScore(performance.get(0).getScore6());
        performanceOutObj.setTotalScore(total);
        performanceOutObj.setStatus("Pending");


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setData(performanceOutObj);
        responseList.setMsg("Success");
        return responseList;
    }
}
