package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.TaskListEntity;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.TaskListRepository;
import com.taskmanager.task.response.*;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskManagerImpl implements TaskManager {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private EmpDetailRepository empDetailRepository;

    @Override
    public ResponseList createTask(CreateTask createTask) {

        TaskListEntity taskListEntity = new TaskListEntity();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD", Locale.ENGLISH);

        Date createdDate = new Date();
        Date dueDate = new Date();

        try {
            createdDate = formatter.parse(createTask.getStartDate());
            dueDate = formatter.parse(createTask.getEndDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        taskListEntity.setUserId(createTask.getUserId());
        taskListEntity.setCategoryId(createTask.getCategoryId());
        taskListEntity.setTaskTitle(createTask.getTaskTitle());
        taskListEntity.setTaskDescription(createTask.getTaskDescription());
        taskListEntity.setStartDate((createdDate));
        taskListEntity.setEndDate(dueDate);
        taskListEntity.setReporterId(createTask.getReporterId());
        taskListEntity.setLabel(createTask.getLabel());
        taskListEntity.setEstimate(createTask.getEstimate());
        taskListEntity.setOriginalEstimate(createTask.getOriginalEstimate());
        taskListEntity.setBlockedTask(createTask.getBlockedTask());
        taskListEntity.setStatus(1);
        taskListEntity.setIsActive(1);
        taskListEntity.setPriority(createTask.getPriority());
        taskListEntity.setLastUpdatedUser(createTask.getLastUpdatedUser());
        taskListEntity.setLastUpdatedDate(new Date());

        taskListRepository.save(taskListEntity);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList getMyTask(int id) {

        List<TaskListEntity> list = taskListRepository.findByUserIdOrderByIdDesc(id);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(list);

        return responseList;

    }

    @Override
    public ResponseList getMyTaskById(int id, int task) {

        Optional<TaskListEntity> list = taskListRepository.findById(task);

        List<TaskListEntity> objSet = new ArrayList<>();

        if (!list.isEmpty()){

            objSet.add(list.get());
        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(objSet);

        return responseList;

    }

    @Override
    public ResponseList deleteRequest(int taskId, int userId) {

        Optional<TaskListEntity> optObj = taskListRepository.findById(taskId);

        TaskListEntity obj = optObj.get();

        obj.setStatus(2);
        obj.setLastUpdatedUser(userId);
        obj.setLastUpdatedDate(new Date());
        obj.setDeletedDate(new Date());

        taskListRepository.save(obj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;


    }

    @Override
    public ResponseList deleteBySupervisor(int taskId, int userId) {

        Optional<TaskListEntity> optObj = taskListRepository.findById(taskId);

        TaskListEntity obj = optObj.get();

        obj.setStatus(4);
        obj.setLastUpdatedUser(userId);
        obj.setLastUpdatedDate(new Date());

        taskListRepository.save(obj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList revertBySupervisor(int taskId, int userId) {

        Optional<TaskListEntity> optObj = taskListRepository.findById(taskId);

        TaskListEntity obj = optObj.get();

        obj.setStatus(1);
        obj.setLastUpdatedUser(userId);
        obj.setLastUpdatedDate(new Date());

        taskListRepository.save(obj);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList editTask(CreateTask createTask, int taskId, int userId) {

        Optional<TaskListEntity> task = taskListRepository.findById(taskId);

        if (!task.isEmpty()) {

            TaskListEntity obj = task.get();

            if (createTask.getRating() != null)
                obj.setRating(createTask.getRating());
            if (createTask.getEstimate() != null)
                obj.setEstimate(createTask.getEstimate());
            if (createTask.getRatingComment() != null)
                obj.setRatingComment(createTask.getRatingComment());
            if (createTask.getSupervisorRating() != null)
                obj.setSupervisorRating(createTask.getSupervisorRating());
            if (createTask.getSupervisorComment() != null)
                obj.setSupervisorComment(createTask.getSupervisorComment());
            if (createTask.getStatus() != null)
                obj.setStatus(createTask.getStatus());

            if (createTask.getStatus() == 3) {
                obj.setCompletedDate(new Date());
            }


            obj.setLastUpdatedUser(userId);
            obj.setLastUpdatedUser(userId);
            obj.setLastUpdatedDate(new Date());

            taskListRepository.save(obj);
        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }

    @Override
    public ResponseList login(String email, String id) {

        List<EmpDetailEntity> emp = empDetailRepository.findByEmailAndNicNo(email, id);

        ResponseList responseList = new ResponseList();

        if (emp.isEmpty()){
            responseList.setCode(400);
            responseList.setMsg("Wrong Email or Password");
        }
        else {
            responseList.setCode(200);
            responseList.setMsg("Success");
            responseList.setData(emp);
        }

        return responseList;

    }

    @Override
    public ResponseList getChildListForSupervisor(int id) {

        List<EmpDetailEntity> list = empDetailRepository.findBySupervisor(id);

        List<SupervisorList> supervisorLists = new ArrayList<>();

        list.forEach(empDetailEntity -> {

            SupervisorList supervisorList = new SupervisorList();
            supervisorList.setId(empDetailEntity.getId());
            supervisorList.setEmail(empDetailEntity.getEmail());
            supervisorList.setNicNo(empDetailEntity.getNicNo());
            supervisorList.setGivenName(empDetailEntity.getGivenName());
            supervisorList.setNameInFull(empDetailEntity.getNameInFull());
            supervisorList.setContactNo(empDetailEntity.getContactNo());
            supervisorList.setContactNo(empDetailEntity.getContactNo());
            supervisorList.setSupervisor(empDetailEntity.getSupervisor());

            supervisorList.setDeleteRequested(taskListRepository.getDeleteRequestedCount(empDetailEntity.getId()));
            supervisorList.setReviewNeeded(taskListRepository.getReviewNeededCount(empDetailEntity.getId()));
            supervisorList.setPendingTask(taskListRepository.getPendingCount(empDetailEntity.getId()));

            supervisorLists.add(supervisorList);

        });

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(supervisorLists);

        return responseList;

    }

    @Override
    public ResponseList getCompletedTaskCount(int id) throws ParseException {

        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-DD", Locale.ENGLISH);

        List<TaskListEntity> list = taskListRepository.findByUserIdOrderByIdDesc(id);

        List<TaskListEntity> todayList = new ArrayList<>();
        List<TaskListEntity> deletedList = new ArrayList<>();
        List<TaskListEntity> pendingList = new ArrayList<>();
        List<TaskListEntity> high = new ArrayList<>();
        List<TaskListEntity> medium = new ArrayList<>();
        List<TaskListEntity> low = new ArrayList<>();

        for (TaskListEntity taskListEntity : list) {

            if (taskListEntity.getCompletedDate() != null && taskListEntity.getCompletedDate().toString().contains(date)){
                todayList.add(taskListEntity);
            }

            if (taskListEntity.getStatus() == 2 || taskListEntity.getStatus() == 4){
                deletedList.add(taskListEntity);
            }
            if (taskListEntity.getStatus() == 1){
                pendingList.add(taskListEntity);
            }

            if (taskListEntity.getPriority().equalsIgnoreCase("High")){
                high.add(taskListEntity);
            }
            else if (taskListEntity.getPriority().equalsIgnoreCase("Medium")){
                medium.add(taskListEntity);
            }
            else if (taskListEntity.getPriority().equalsIgnoreCase("Low")){
                low.add(taskListEntity);
            }


        }

        StatList statList = new StatList();
        statList.setCompletedTaskForToday(todayList.size());
        statList.setAllTask(list.size());
        statList.setDeletedTask(deletedList.size());
        statList.setPendingTask(pendingList.size());
        statList.setHighPriorityTask(high.size());
        statList.setMediumPriorityTask(medium.size());
        statList.setLowPriorityTask(low.size());

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(statList);

        return responseList;
    }

    @Override
    public ResponseList getLastSevenDaysCompletedRate(int id) throws ParseException {

        List<TaskListEntity> list = taskListRepository.findByUserIdOrderByIdDesc(id);

        LastSevenDayChart lastSevenDayChart = new LastSevenDayChart();

        List<LastSevenDaySeriesObj> lastSevenDaySeriesObjs =  new ArrayList<>();

        LastSevenDaySeriesObj lastSevenDaySeriesObj = new LastSevenDaySeriesObj();

        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(0);
        integers.add(list.size());
        integers.add(0);
        integers.add(0);
        integers.add(0);
        integers.add(0);

        lastSevenDaySeriesObj.setName("Sessions");
        lastSevenDaySeriesObj.setDate(integers);

        lastSevenDaySeriesObjs.add(lastSevenDaySeriesObj);

        lastSevenDayChart.setSeries(lastSevenDaySeriesObjs);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(lastSevenDayChart);

        return responseList;
    }

    @Override
    public ResponseList getLastFourDue(int id) throws ParseException {

        List<TaskListEntity> task = taskListRepository.findByUserIdOrderByEndDate(id);

        LastFourDayDueObj lastFourDayDueObj = new LastFourDayDueObj();

        for (int x = 0; x < 4; x++){

            if (task.size() > x){

                if (x == 0){
                    lastFourDayDueObj.setDue1(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority1(task.get(x).getPriority());
                    lastFourDayDueObj.setTask1(task.get(x).getTaskTitle());
                }
                else if (x == 1){
                    lastFourDayDueObj.setDue2(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority2(task.get(x).getPriority());
                    lastFourDayDueObj.setTask2(task.get(x).getTaskTitle());
                }
                else if (x == 2){
                    lastFourDayDueObj.setDue3(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority3(task.get(x).getPriority());
                    lastFourDayDueObj.setTask3(task.get(x).getTaskTitle());
                }
                else if (x == 3){
                    lastFourDayDueObj.setDue4(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority4(task.get(x).getPriority());
                    lastFourDayDueObj.setTask4(task.get(x).getTaskTitle());
                }
            }
            else {
                if (x == 0){
                    lastFourDayDueObj.setDue1("No Task Found");
                    lastFourDayDueObj.setPriority1("Low");
                    lastFourDayDueObj.setTask1("");
                }
                else if (x == 1){
                    lastFourDayDueObj.setDue2("No Task Found");
                    lastFourDayDueObj.setPriority2("Low");
                    lastFourDayDueObj.setTask2("");
                }
                else if (x == 2){
                    lastFourDayDueObj.setDue3("No Task Found");
                    lastFourDayDueObj.setPriority3("Low");
                    lastFourDayDueObj.setTask3("");
                }
                else if (x == 3){
                    lastFourDayDueObj.setDue4("No Task Found");
                    lastFourDayDueObj.setPriority4("Low");
                    lastFourDayDueObj.setTask4("");
                }
            }

        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(lastFourDayDueObj);

        return responseList;

    }

    @Override
    public ResponseList getTotalStoryPoints(int id) throws ParseException {

        List<TaskListEntity> list = taskListRepository.findByUserIdOrderByIdDesc(id);

        int count = 0;
        int percentage = 0;

        for (TaskListEntity taskListEntity : list) {
            count += taskListEntity.getEstimate();
        }

        percentage = (count*100)/9;

        String status = "";

        if (count < 5){
            status = "Ver Low";
        }
        else if (count < 9){
            status = "Low";
        }
        else {
            status = "Perfect";
        }

        StoryPoints storyPoints = new StoryPoints();
        storyPoints.setTickets(list.size());
        storyPoints.setCounts(9);
        storyPoints.setPercentage(percentage);
        storyPoints.setTotalStory(count);
        storyPoints.setStatus(status);


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(storyPoints);

        return responseList;

    }
}
