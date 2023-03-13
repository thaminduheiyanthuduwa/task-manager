package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.ProfileRepository;
import com.taskmanager.task.repository.TaskBreakdownRepository;
import com.taskmanager.task.repository.TaskListRepository;
import com.taskmanager.task.response.*;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Period;
import java.time.ZoneId;
import java.util.Properties;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

    @Autowired
    private TaskBreakdownRepository taskBreakdownRepository;

    @Autowired
    private ProfileRepository profileRepository;


    @Override
    public ResponseList addProfile() {
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Profile All data");
        responseList.setData(profileRepository.findAll());
        return responseList;
    }

    @Override
    public ResponseList updateProfile(int id) {
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Profile All data");
        responseList.setData(profileRepository.findById(id));
        return responseList;
    }



    @Override
    public ResponseList getEmpDetailEntityById(int id) {

//        ProfileEntity profileEntity = new ProfileEntity();
//        List<profileEntity>

        ResponseList responseList = new ResponseList();

        responseList.setCode(200);
        responseList.setMsg("Profile data");
        responseList.setData(empDetailRepository.findById(id));
//
        return responseList;
    }

    @Override
    public ResponseList createTask(CreateTask createTask) {

        TaskListEntity taskListEntity = new TaskListEntity();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

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
        taskListEntity.setRecurring(createTask.getRecurring());
        taskListEntity.setAutoStatus(3);

        if (createTask.getRecurring() != null && createTask.getRecurring().equalsIgnoreCase("Ad-hoc")) {
            taskListEntity.setSubId(1);
        } else {
            taskListEntity.setSubId(0);
        }

//        taskListEntity.setIsActive(1);
        taskListEntity.setPriority(createTask.getPriority());
        taskListEntity.setLastUpdatedUser(createTask.getLastUpdatedUser());
        taskListEntity.setLastUpdatedDate(new Date());


        taskListRepository.save(taskListEntity);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        new Thread() {
            public void run() {
                sendNewTaskAddEmail(createTask.getUserId(), createTask.getTaskTitle());//Call your function
            }
        }.start();

        return responseList;
    }


    @Override
    public ResponseList getMyTask(int id, Integer type) {

        List<TaskListEntity> list3 = taskListRepository.findByUserIdOrderByIdDesc(id);

        List<TaskListEntity> list2 = null;

        Date date = new Date();

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat2.format(date);


        if (type != null && type == 1) {
            list2 = list3.stream().filter(taskListEntity ->

//                    (dateFormat2.format(taskListEntity.getStartDate()).equals(strDate) ||
//                            (taskListEntity.getEndDate().after(date) && taskListEntity.getStartDate().before(date))) &&
                            taskListEntity.getStatus() == 1
            ).collect(Collectors.toList());
        } else if (type != null && type == 2) {
            list2 = list3.stream().filter(taskListEntity -> taskListEntity.getStatus() == 2
            ).collect(Collectors.toList());
        } else if (type != null && type == 3) {
            list2 = list3.stream().filter(taskListEntity -> taskListEntity.getStatus() == 3
            ).collect(Collectors.toList());
        } else if (type != null && type == 4) {
            list2 = list3.stream().filter(taskListEntity -> taskListEntity.getStatus() == 5
            ).collect(Collectors.toList());
        } else {
            list2 = list3;
        }


        List<TaskListEntityResponse> list = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (TaskListEntity taskListEntity : list2) {
            TaskListEntityResponse taskListEntityResponse = new TaskListEntityResponse();
            taskListEntityResponse.setId(taskListEntity.getId());
            taskListEntityResponse.setUserId(taskListEntity.getUserId());
            taskListEntityResponse.setCategoryId(taskListEntity.getCategoryId());
            taskListEntityResponse.setTaskTitle(taskListEntity.getTaskTitle());
            taskListEntityResponse.setTaskDescription(taskListEntity.getTaskDescription());
            taskListEntityResponse.setStartDate((taskListEntity.getStartDate() != null) ? dateFormat.format(taskListEntity.getStartDate()) : null);
            taskListEntityResponse.setEndDate((taskListEntity.getEndDate() != null) ? dateFormat.format(taskListEntity.getEndDate()) : null);
            taskListEntityResponse.setReporterId(taskListEntity.getReporterId());
            taskListEntityResponse.setLabel(taskListEntity.getLabel());
            taskListEntityResponse.setRecurring(taskListEntity.getRecurring());
            taskListEntityResponse.setSubId(taskListEntity.getSubId());
            taskListEntityResponse.setEstimate(taskListEntity.getEstimate());
            taskListEntityResponse.setOriginalEstimate(taskListEntity.getOriginalEstimate());
            taskListEntityResponse.setBlockedTask(taskListEntity.getBlockedTask());
            taskListEntityResponse.setStatus(taskListEntity.getStatus());
            taskListEntityResponse.setIsActive(taskListEntity.getIsActive());
            taskListEntityResponse.setPriority(taskListEntity.getPriority());
            taskListEntityResponse.setRating(taskListEntity.getRating());
            taskListEntityResponse.setRatingComment(taskListEntity.getRatingComment());
            taskListEntityResponse.setSupervisorRating(taskListEntity.getSupervisorRating());
            taskListEntityResponse.setSupervisorComment(taskListEntity.getSupervisorComment());
            taskListEntityResponse.setLastUpdatedUser(taskListEntity.getLastUpdatedUser());
            taskListEntityResponse.setLastUpdatedDate((taskListEntity.getLastUpdatedDate() != null) ? dateFormat.format(taskListEntity.getLastUpdatedDate()) : null);
            taskListEntityResponse.setCompletedDate((taskListEntity.getCompletedDate() != null) ? dateFormat.format(taskListEntity.getCompletedDate()) : null);
            taskListEntityResponse.setDeletedDate((taskListEntity.getDeletedDate() != null) ? dateFormat.format(taskListEntity.getDeletedDate()) : null);
            taskListEntityResponse.setAutoStatus(taskListEntity.getAutoStatus());
            list.add(taskListEntityResponse);

        }

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

        if (!list.isEmpty()) {

            TaskListEntity tempObj = list.get();

            if (id == tempObj.getUserId() && tempObj.getStatus() == 1) {
                tempObj.setEstimate(null);
            }

            objSet.add(tempObj);
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

        new Thread() {
            public void run() {
                sendDeleteTaskEmail(userId, obj.getTaskTitle());//Call your function
            }
        }.start();

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
    @Transactional
    public ResponseList revertBySupervisor(int taskId, int userId) {

        Optional<TaskListEntity> optObj = taskListRepository.findById(taskId);

        TaskListEntity obj = optObj.get();

        obj.setStatus(1);
        obj.setLastUpdatedUser(userId);
        obj.setLastUpdatedDate(new Date());
        obj.setEstimate(0D);
        obj.setIsReverted(1);

        taskBreakdownRepository.deleteByTaskId(obj.getId());

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
            if (createTask.getEstimate() != null) {
                TaskBreakdownEntity taskBreakdownEntity = new TaskBreakdownEntity();
                taskBreakdownEntity.setTaskId(obj.getId());
                taskBreakdownEntity.setUserId(userId);
                taskBreakdownEntity.setEstimate(createTask.getEstimate());
                if (obj.getIsReverted() == 1) {
                    if (obj.getCompletedDate() != null)
                        taskBreakdownEntity.setDate(obj.getCompletedDate());
                    else
                        taskBreakdownEntity.setDate(new Date());
                }
                else
                    taskBreakdownEntity.setDate(new Date());
                taskBreakdownRepository.save(taskBreakdownEntity);
                Double tempEstimate = obj.getEstimate();
                if (tempEstimate == null) {
                    tempEstimate = 0d;
                }
                tempEstimate += createTask.getEstimate();
                obj.setEstimate(tempEstimate);
            }
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
                new Thread() {
                    public void run() {
                        sendCompletedTaskEmail(obj.getUserId(), obj.getTaskTitle());//Call your function
                    }
                }.start();
            }
            else
                obj.setAutoStatus(0);


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

        if (emp.isEmpty()) {
            responseList.setCode(400);
            responseList.setMsg("Wrong Email or Password");
        } else {
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

        List<TaskListEntity> newList = list.stream().filter(taskListEntity ->
                taskListEntity.getStatus() == 1).collect(Collectors.toList());

        List<TaskListEntity> todayList = new ArrayList<>();
        List<TaskListEntity> deletedList = new ArrayList<>();
        List<TaskListEntity> pendingList = new ArrayList<>();
        List<TaskListEntity> high = new ArrayList<>();
        List<TaskListEntity> medium = new ArrayList<>();
        List<TaskListEntity> low = new ArrayList<>();

        for (TaskListEntity taskListEntity : list) {

            if ((taskListEntity.getRating() != null && taskListEntity.getRating() != 6) && taskListEntity.getStatus() == 3 && taskListEntity.getCompletedDate() != null && taskListEntity.getCompletedDate().toString().contains(date)) {
                todayList.add(taskListEntity);
            }

            if ((taskListEntity.getRating() != null && taskListEntity.getStatus() == 2) || taskListEntity.getStatus() == 4) {
                deletedList.add(taskListEntity);
            }
            if ((taskListEntity.getStatus() != null && taskListEntity.getStatus() == 1)) {
                pendingList.add(taskListEntity);
            }

            if (taskListEntity.getPriority().equalsIgnoreCase("High") && taskListEntity.getStatus() == 1) {
                high.add(taskListEntity);
            } else if (taskListEntity.getPriority().equalsIgnoreCase("Medium") && taskListEntity.getStatus() == 1) {
                medium.add(taskListEntity);
            } else if (taskListEntity.getPriority().equalsIgnoreCase("Low") && taskListEntity.getStatus() == 1) {
                low.add(taskListEntity);
            }


        }

        StatList statList = new StatList();
        statList.setCompletedTaskForToday(todayList.size());
        statList.setAllTask(newList.size());
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

        List<LastSevenDaySeriesObj> lastSevenDaySeriesObjs = new ArrayList<>();

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

        List<TaskListEntity> newTask = taskListRepository.findByUserIdOrderByEndDate(id);

        List<TaskListEntity> task = newTask.stream().filter(taskListEntity ->
                taskListEntity.getStatus() == 1).collect(Collectors.toList());


        LastFourDayDueObj lastFourDayDueObj = new LastFourDayDueObj();

        for (int x = 0; x < 4; x++) {

            if (task.size() > x) {

                if (x == 0) {
                    lastFourDayDueObj.setDue1(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority1(task.get(x).getPriority());
                    lastFourDayDueObj.setTask1(task.get(x).getTaskTitle());
                } else if (x == 1) {
                    lastFourDayDueObj.setDue2(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority2(task.get(x).getPriority());
                    lastFourDayDueObj.setTask2(task.get(x).getTaskTitle());
                } else if (x == 2) {
                    lastFourDayDueObj.setDue3(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority3(task.get(x).getPriority());
                    lastFourDayDueObj.setTask3(task.get(x).getTaskTitle());
                } else if (x == 3) {
                    lastFourDayDueObj.setDue4(task.get(x).getEndDate().toString());
                    lastFourDayDueObj.setPriority4(task.get(x).getPriority());
                    lastFourDayDueObj.setTask4(task.get(x).getTaskTitle());
                }
            } else {
                if (x == 0) {
                    lastFourDayDueObj.setDue1("No Task Found");
                    lastFourDayDueObj.setPriority1("Low");
                    lastFourDayDueObj.setTask1("");
                } else if (x == 1) {
                    lastFourDayDueObj.setDue2("No Task Found");
                    lastFourDayDueObj.setPriority2("Low");
                    lastFourDayDueObj.setTask2("");
                } else if (x == 2) {
                    lastFourDayDueObj.setDue3("No Task Found");
                    lastFourDayDueObj.setPriority3("Low");
                    lastFourDayDueObj.setTask3("");
                } else if (x == 3) {
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

        List<TaskBreakdownEntity> taskBreakDown = taskBreakdownRepository.findByUserId(id);

        DecimalFormat df = new DecimalFormat("0.00");

        Date date = new Date();

        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat2.format(date);


//        List<TaskListEntity> newList = taskListRepository.findByUserIdOrderByIdDesc(id);


        double count = 0;
        int numberCount = 0;
        double percentage = 0;

        for (TaskBreakdownEntity taskBreakdownEntity : taskBreakDown) {

            if (taskBreakdownEntity.getDate() != null && dateFormat2.format(taskBreakdownEntity.getDate()).equals(dateFormat2.format(date))) {

                count += taskBreakdownEntity.getEstimate();
                numberCount += 1;

            }
        }


        percentage = (count * 100) / 8;

        String status = "";

        if (count < 4) {
            status = "Very Low";
        } else if (count < 6) {
            status = "Low";
        } else if (count < 8) {
            status = "Medium";
        } else if (count >= 8 && count < 15) {
            status = "Perfect";
        } else {
            status = "Awesome";
        }

        StoryPoints storyPoints = new StoryPoints();
        storyPoints.setTickets(numberCount);
        storyPoints.setCounts(8);
        storyPoints.setPercentage(Double.parseDouble(df.format(percentage)));
        storyPoints.setTotalStory(Double.parseDouble(df.format(count)));
        storyPoints.setStatus(status);


        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(storyPoints);

        return responseList;

    }

    @Override
    public void sendMorningEmail() {

        HashMap<Integer, String> map = new HashMap<>();
        map.put(464, "vinura@kiu.ac.lk");

        List<String> list = new ArrayList<>();

        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateObj.format(formatter);

        HashMap<String, Integer> map2 = new HashMap<>();

        map.forEach((integer, s) -> {

            List<TaskListEntity> count = new ArrayList<>();

            List<TaskListEntity> listNew = taskListRepository.findByUserIdOrderByIdDesc(integer);

            Integer count2 = listNew.stream().filter(taskListEntity -> taskListEntity.getStatus() == 1).collect(Collectors.toList()).size();

            map2.put(s, count2);


        });


        String to = "kiuhrportal@gmail.com";
        String to2 = "kiuhrportal@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "kiuhrportal@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("kiuhrportal@gmail.com", "zifvywhfnwqldrew");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);


        map2.forEach((s, integer) -> {
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(s));
                message.setSubject("KIU TASK-MANAGER MORNING REMINDER !");
                if (integer > 0) {
                    message.setContent(
                            "<h1>Good Morning</h1>\n" +
                                    "      <h2>You Have Assigned " + integer + " Task Today</h2>\n" +
                                    "      <h3>please complete them on time !</h3>",
                            "text/html");
                } else {
                    message.setContent(
                            "<h1>Good Morning</h1>\n" +
                                    "      <h2>Currently You Don't Have Task Today</h2>\n" +
                                    "      <h3>Please Contact the Supervisor !</h3>",
                            "text/html");
                }

                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        });


    }

    @Override
    public ResponseList updateNotApplicable(int taskId, int userId, int supervisor) {

        Optional<TaskListEntity> task = taskListRepository.findById(taskId);

        if (!task.isEmpty()) {

            TaskListEntity obj = task.get();

            obj.setRating(6);
            if (supervisor == 2) {
                obj.setStatus(5);
            } else {
                obj.setStatus(3);
            }


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
    public ResponseList getIsSupervisorReviewNeeded(int supervisor) {

        Integer count = taskListRepository.getIsPendingSupervisorReview(supervisor);
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(count);

        return responseList;
    }

    @Override
    public ResponseList getTotalEstimateByTask(int task) {

        List<TaskBreakdownEntity> taskList = taskBreakdownRepository.findByTaskId(task);

        List<TaskBreakdownEntity> newList = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Double totalEstimate = 0d;

        for (TaskBreakdownEntity taskBreakdownEntity : taskList) {
            TaskBreakdownEntity tempObj = taskBreakdownEntity;
            String tempDate = formatter.format(tempObj.getDate());
            try {
                tempObj.setDate(formatter.parse(tempDate));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            totalEstimate += taskBreakdownEntity.getEstimate();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = "";

        if (!taskList.isEmpty()) {
            strDate = dateFormat.format(taskList.get(0).getDate());
        }

        GetEstimateByTaskResponse getEstimateByTaskResponse = new GetEstimateByTaskResponse();
        getEstimateByTaskResponse.setTotal(totalEstimate);
        getEstimateByTaskResponse.setLastUpdatedTime(strDate);
        getEstimateByTaskResponse.setTaskBreakdownEntities(taskList);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(getEstimateByTaskResponse);

        return responseList;

    }

    public void sendNewTaskAddEmail(int id, String title) {

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(id);

        EmpDetailEntity obj = emp.get();

        Optional<EmpDetailEntity> sup = null;
        EmpDetailEntity obj2 = null;

        if (obj.getSupervisor() != null) {
            sup = empDetailRepository.findById(obj.getSupervisor());
            obj2 = sup.get();
        }


        String from = "kiuhrportal@gmail.com";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("kiuhrportal@gmail.com", "zifvywhfnwqldrew");

            }

        });

        session.setDebug(true);


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(obj.getEmail()));
            message.setSubject("New Task Alert !");
            message.setContent(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"utf-8\"> <!-- utf-8 works for most cases -->\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width\"> <!-- Forcing initial-scale shouldn't be necessary -->\n" +
                            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <!-- Use the latest (edge) version of IE rendering engine -->\n" +
                            "    <meta name=\"x-apple-disable-message-reformatting\">  <!-- Disable auto-scale in iOS 10 Mail entirely -->\n" +
                            "    <title></title> <!-- The title tag shows in email notifications, like Android 4.4. -->\n" +
                            "\n" +
                            "    <link href=\"https://fonts.googleapis.com/css?family=Lato:300,400,700\" rel=\"stylesheet\">\n" +
                            "\n" +
                            "    <!-- CSS Reset : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "        /* What it does: Remove spaces around the email design added by some email clients. */\n" +
                            "        /* Beware: It can remove the padding / margin and add a background color to the compose a reply window. */\n" +
                            "        html,\n" +
                            "body {\n" +
                            "    margin: 0 auto !important;\n" +
                            "    padding: 0 !important;\n" +
                            "    height: 100% !important;\n" +
                            "    width: 100% !important;\n" +
                            "    background: #f1f1f1;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops email clients resizing small text. */\n" +
                            "* {\n" +
                            "    -ms-text-size-adjust: 100%;\n" +
                            "    -webkit-text-size-adjust: 100%;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Centers email on Android 4.4 */\n" +
                            "div[style*=\"margin: 16px 0\"] {\n" +
                            "    margin: 0 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Stops Outlook from adding extra spacing to tables. */\n" +
                            "table,\n" +
                            "td {\n" +
                            "    mso-table-lspace: 0pt !important;\n" +
                            "    mso-table-rspace: 0pt !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Fixes webkit padding issue. */\n" +
                            "table {\n" +
                            "    border-spacing: 0 !important;\n" +
                            "    border-collapse: collapse !important;\n" +
                            "    table-layout: fixed !important;\n" +
                            "    margin: 0 auto !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Uses a better rendering method when resizing images in IE. */\n" +
                            "img {\n" +
                            "    -ms-interpolation-mode:bicubic;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Windows 10 Mail from underlining links despite inline CSS. Styles for underlined links should be inline. */\n" +
                            "a {\n" +
                            "    text-decoration: none;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: A work-around for email clients meddling in triggered links. */\n" +
                            "*[x-apple-data-detectors],  /* iOS */\n" +
                            ".unstyle-auto-detected-links *,\n" +
                            ".aBn {\n" +
                            "    border-bottom: 0 !important;\n" +
                            "    cursor: default !important;\n" +
                            "    color: inherit !important;\n" +
                            "    text-decoration: none !important;\n" +
                            "    font-size: inherit !important;\n" +
                            "    font-family: inherit !important;\n" +
                            "    font-weight: inherit !important;\n" +
                            "    line-height: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from displaying a download button on large, non-linked images. */\n" +
                            ".a6S {\n" +
                            "    display: none !important;\n" +
                            "    opacity: 0.01 !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Prevents Gmail from changing the text color in conversation threads. */\n" +
                            ".im {\n" +
                            "    color: inherit !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* If the above doesn't work, add a .g-img class to any image in question. */\n" +
                            "img.g-img + div {\n" +
                            "    display: none !important;\n" +
                            "}\n" +
                            "\n" +
                            "/* What it does: Removes right gutter in Gmail iOS app: https://github.com/TedGoas/Cerberus/issues/89  */\n" +
                            "/* Create one of these media queries for each additional viewport size you'd like to fix */\n" +
                            "\n" +
                            "/* iPhone 4, 4S, 5, 5S, 5C, and 5SE */\n" +
                            "@media only screen and (min-device-width: 320px) and (max-device-width: 374px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 320px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6, 6S, 7, 8, and X */\n" +
                            "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 375px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "/* iPhone 6+, 7+, and 8+ */\n" +
                            "@media only screen and (min-device-width: 414px) {\n" +
                            "    u ~ div .email-container {\n" +
                            "        min-width: 414px !important;\n" +
                            "    }\n" +
                            "}\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "    <!-- CSS Reset : END -->\n" +
                            "\n" +
                            "    <!-- Progressive Enhancements : BEGIN -->\n" +
                            "    <style>\n" +
                            "\n" +
                            "\t    .primary{\n" +
                            "\tbackground: #30e3ca;\n" +
                            "}\n" +
                            ".bg_white{\n" +
                            "\tbackground: #ffffff;\n" +
                            "}\n" +
                            ".bg_light{\n" +
                            "\tbackground: #fafafa;\n" +
                            "}\n" +
                            ".bg_black{\n" +
                            "\tbackground: #000000;\n" +
                            "}\n" +
                            ".bg_dark{\n" +
                            "\tbackground: rgba(0,0,0,.8);\n" +
                            "}\n" +
                            ".email-section{\n" +
                            "\tpadding:2.5em;\n" +
                            "}\n" +
                            "\n" +
                            "/*BUTTON*/\n" +
                            ".btn{\n" +
                            "\tpadding: 10px 15px;\n" +
                            "\tdisplay: inline-block;\n" +
                            "}\n" +
                            ".btn.btn-primary{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".btn.btn-white{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: #ffffff;\n" +
                            "\tcolor: #000000;\n" +
                            "}\n" +
                            ".btn.btn-white-outline{\n" +
                            "\tborder-radius: 5px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 1px solid #fff;\n" +
                            "\tcolor: #fff;\n" +
                            "}\n" +
                            ".btn.btn-black-outline{\n" +
                            "\tborder-radius: 0px;\n" +
                            "\tbackground: transparent;\n" +
                            "\tborder: 2px solid #000;\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-weight: 700;\n" +
                            "}\n" +
                            "\n" +
                            "h1,h2,h3,h4,h5,h6{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tcolor: #000000;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            "\n" +
                            "body{\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tfont-size: 15px;\n" +
                            "\tline-height: 1.8;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "}\n" +
                            "\n" +
                            "a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "table{\n" +
                            "}\n" +
                            "/*LOGO*/\n" +
                            "\n" +
                            ".logo h1{\n" +
                            "\tmargin: 0;\n" +
                            "}\n" +
                            ".logo h1 a{\n" +
                            "\tcolor: #30e3ca;\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 700;\n" +
                            "\tfont-family: 'Lato', sans-serif;\n" +
                            "}\n" +
                            "\n" +
                            "/*HERO*/\n" +
                            ".hero{\n" +
                            "\tposition: relative;\n" +
                            "\tz-index: 0;\n" +
                            "}\n" +
                            "\n" +
                            ".hero .text{\n" +
                            "\tcolor: rgba(0,0,0,.3);\n" +
                            "}\n" +
                            ".hero .text h2{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 40px;\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tfont-weight: 400;\n" +
                            "\tline-height: 1.4;\n" +
                            "}\n" +
                            ".hero .text h3{\n" +
                            "\tfont-size: 24px;\n" +
                            "\tfont-weight: 300;\n" +
                            "}\n" +
                            ".hero .text h2 span{\n" +
                            "\tfont-weight: 600;\n" +
                            "\tcolor: #30e3ca;\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "/*HEADING SECTION*/\n" +
                            ".heading-section{\n" +
                            "}\n" +
                            ".heading-section h2{\n" +
                            "\tcolor: #000000;\n" +
                            "\tfont-size: 28px;\n" +
                            "\tmargin-top: 0;\n" +
                            "\tline-height: 1.4;\n" +
                            "\tfont-weight: 400;\n" +
                            "}\n" +
                            ".heading-section .subheading{\n" +
                            "\tmargin-bottom: 20px !important;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(0,0,0,.4);\n" +
                            "\tposition: relative;\n" +
                            "}\n" +
                            ".heading-section .subheading::after{\n" +
                            "\tposition: absolute;\n" +
                            "\tleft: 0;\n" +
                            "\tright: 0;\n" +
                            "\tbottom: -10px;\n" +
                            "\tcontent: '';\n" +
                            "\twidth: 100%;\n" +
                            "\theight: 2px;\n" +
                            "\tbackground: #30e3ca;\n" +
                            "\tmargin: 0 auto;\n" +
                            "}\n" +
                            "\n" +
                            ".heading-section-white{\n" +
                            "\tcolor: rgba(255,255,255,.8);\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tfont-family: \n" +
                            "\tline-height: 1;\n" +
                            "\tpadding-bottom: 0;\n" +
                            "}\n" +
                            ".heading-section-white h2{\n" +
                            "\tcolor: #ffffff;\n" +
                            "}\n" +
                            ".heading-section-white .subheading{\n" +
                            "\tmargin-bottom: 0;\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tfont-size: 13px;\n" +
                            "\ttext-transform: uppercase;\n" +
                            "\tletter-spacing: 2px;\n" +
                            "\tcolor: rgba(255,255,255,.4);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "ul.social{\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            "ul.social li{\n" +
                            "\tdisplay: inline-block;\n" +
                            "\tmargin-right: 10px;\n" +
                            "}\n" +
                            "\n" +
                            "/*FOOTER*/\n" +
                            "\n" +
                            ".footer{\n" +
                            "\tborder-top: 1px solid rgba(0,0,0,.05);\n" +
                            "\tcolor: rgba(0,0,0,.5);\n" +
                            "}\n" +
                            ".footer .heading{\n" +
                            "\tcolor: #000;\n" +
                            "\tfont-size: 20px;\n" +
                            "}\n" +
                            ".footer ul{\n" +
                            "\tmargin: 0;\n" +
                            "\tpadding: 0;\n" +
                            "}\n" +
                            ".footer ul li{\n" +
                            "\tlist-style: none;\n" +
                            "\tmargin-bottom: 10px;\n" +
                            "}\n" +
                            ".footer ul li a{\n" +
                            "\tcolor: rgba(0,0,0,1);\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "@media screen and (max-width: 500px) {\n" +
                            "\n" +
                            "\n" +
                            "}\n" +
                            "\n" +
                            "\n" +
                            "    </style>\n" +
                            "\n" +
                            "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                            "</head>\n" +
                            "\n" +
                            "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n" +
                            "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                            "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                            "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                            "    </div>\n" +
                            "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                            "    \t<!-- BEGIN BODY -->\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "      \t<tr>\n" +
                            "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n" +
                            "          \t<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                            "          \t\t<tr>\n" +
                            "          \t\t\t<td class=\"logo\" style=\"text-align: center;\">\n" +
                            "\t\t\t            <h1><a href=\"#\">KIU HR</a></h1>\n" +
                            "\t\t\t          </td>\n" +
                            "          \t\t</tr>\n" +
                            "          \t</table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "\t      <tr>\n" +
                            "          <td valign=\"center\" class=\"hero bg_white\" style=\"padding: 3em 0 2em 0;\">\n" +
                            "            <i class=\"fa fa-tasks\" style=\"font-size:100px;color:rgb(22, 22, 22);width: 100px; max-width: 200px; height: auto; margin: auto; display: block; \"></i>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "\t\t\t\t<tr>\n" +
                            "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n" +
                            "            <table>\n" +
                            "            \t<tr>\n" +
                            "            \t\t<td>\n" +
                            "            \t\t\t<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n" +
                            "            \t\t\t\t<h2>Hi, New Task Has Been Added</h2>\n" +
                            "            \t\t\t\t<h3>Task Title</h3>\n" +
                            "\t\t\t\t\t\t\t<h3 style=\"color: red\">" + title + "</h3>\n" +
                            "\t\t\t\t\t\t\t<h3>Please complete them on time !</h3>\n" +
                            "            \t\t\t\t<p><a href=\"http://13.232.138.190:8087/apps/myTask\" class=\"btn btn-primary\">Go To Portal</a></p>\n" +
                            "            \t\t\t</div>\n" +
                            "            \t\t</td>\n" +
                            "            \t</tr>\n" +
                            "            </table>\n" +
                            "          </td>\n" +
                            "\t      </tr><!-- end tr -->\n" +
                            "      <!-- 1 Column Text + Button : END -->\n" +
                            "      </table>\n" +
                            "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                            "    \n" +
                            "        <tr>\n" +
                            "          <td class=\"bg_light\" style=\"text-align: center;\">\n" +
                            "          \t<p>This ia an auto generated email <a href=\"#\" style=\"color: rgba(0,0,0,.8);\"><i class=\"fa fa-heart\" style=\"font-size:20px;color:red\"></i> By KIU HR</a></p>\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>\n" +
                            "  </center>\n" +
                            "</body>\n" +
                            "</html>",
                    "text/html");


            Transport.send(message);


            if (obj2 != null) {
                MimeMessage message2 = new MimeMessage(session);
                message2.setFrom(new InternetAddress(from));
                message2.addRecipient(Message.RecipientType.TO, new InternetAddress(obj2.getEmail()));
                message2.setSubject("New Task Alert !");
                message2.setContent(
                        "<h1>New Task Has Been Added For " + obj.getGivenName() + "</h1>\n" +
                                "      <h2>Task Title :" + title + "</h2>\n" +
                                "      <h3>Please Review It !</h3>",
                        "text/html");


                Transport.send(message2);
            }

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }

    public void sendCompletedTaskEmail(int id, String title) {

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(id);

        EmpDetailEntity obj = emp.get();

        Optional<EmpDetailEntity> sup = empDetailRepository.findById(obj.getSupervisor());

        EmpDetailEntity obj2 = sup.get();

        String from = "kiuhrportal@gmail.com";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("kiuhrportal@gmail.com", "zifvywhfnwqldrew");

            }

        });

        session.setDebug(true);


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(obj.getEmail()));
            message.setSubject("New Task Alert !");
            message.setContent(
                    "<h1>Hi, Task Has Been Completed</h1>\n" +
                            "      <h2>Task Title :" + title + "</h2>\n" +
                            "      <h3></h3>",
                    "text/html");


            Transport.send(message);


            MimeMessage message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(from));
            message2.addRecipient(Message.RecipientType.TO, new InternetAddress(obj2.getEmail()));
            message2.setSubject("New Task Alert !");
            message2.setContent(
                    "<h1>New Task Has Been Completed By " + obj.getGivenName() + "</h1>\n" +
                            "      <h2>Task Title :" + title + "</h2>\n" +
                            "      <h3>Please Review It !</h3>",
                    "text/html");


            Transport.send(message2);

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }

    public void sendDeleteTaskEmail(int id, String title) {

        Optional<EmpDetailEntity> emp = empDetailRepository.findById(id);

        EmpDetailEntity obj = emp.get();

        Optional<EmpDetailEntity> sup = empDetailRepository.findById(obj.getSupervisor());

        EmpDetailEntity obj2 = sup.get();

        String from = "kiuhrportal@gmail.com";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("kiuhrportal@gmail.com", "zifvywhfnwqldrew");

            }

        });

        session.setDebug(true);


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(obj.getEmail()));
            message.setSubject("New Task Alert !");
            message.setContent(
                    "<h1>Hi, Task Has Been Deleted</h1>\n" +
                            "      <h2>Task Title :" + title + "</h2>\n" +
                            "      <h3></h3>",
                    "text/html");


            Transport.send(message);


            MimeMessage message2 = new MimeMessage(session);
            message2.setFrom(new InternetAddress(from));
            message2.addRecipient(Message.RecipientType.TO, new InternetAddress(obj2.getEmail()));
            message2.setSubject("New Task Alert !");
            message2.setContent(
                    "<h1>Task Has Been Deleted By " + obj.getGivenName() + "</h1>\n" +
                            "      <h2>Task Title :" + title + "</h2>\n" +
                            "      <h3>Please Review It !</h3>",
                    "text/html");


            Transport.send(message2);

            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }

    @Transactional
    public void setRecurring() {

        List<TaskListEntity> listOjb = taskListRepository.findBySubId(0);

        List<TaskListEntity> list = listOjb.stream().filter(taskListEntity ->
                taskListEntity.getStatus() == 1 || taskListEntity.getStatus() == 5
                        || taskListEntity.getStatus() == 3).collect(Collectors.toList());

        List<TaskListEntity> newList = new ArrayList<>();

        list.forEach(taskListEntity -> {

            TaskListEntity tempEntity = (TaskListEntity) taskListEntity.clone();

            LocalDate dateObj = LocalDate.now();

            LocalDate newDate = taskListEntity.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Integer days = Period.between(newDate, dateObj).getDays();

            Integer newDiff = daysBetween(taskListEntity.getEndDate(), taskListEntity.getStartDate());

            if (taskListEntity.getRecurring() != null && taskListEntity.getRecurring()
                    .equalsIgnoreCase("Daily") && days == 1) {

                TaskListEntity tempNew = (TaskListEntity) tempEntity.clone();
                TaskListEntity oldTask = (TaskListEntity) tempEntity.clone();

                tempNew.setCompletedDate(null);
                tempNew.setId(null);
                tempNew.setStartDate(new Date());
                tempNew.setLastUpdatedDate(new Date());
                tempNew.setSupervisorComment(null);
                tempNew.setSupervisorRating(null);
                tempNew.setEstimate(0D);
                tempNew.setRating(null);
                tempNew.setRatingComment(null);
                tempNew.setEndDate(addDays(new Date(), newDiff));
                tempNew.setAutoStatus(3);
                tempNew.setStatus(1);

                newList.add(tempNew);
                oldTask.setSubId(1);
                newList.add(oldTask);

            } else if (taskListEntity.getRecurring() != null && taskListEntity.getRecurring()
                    .equalsIgnoreCase("Weekly") && days == 7) {

                TaskListEntity tempNew = tempEntity;

                tempNew.setCompletedDate(null);
                tempNew.setStartDate(new Date());
                tempNew.setLastUpdatedDate(new Date());
                tempNew.setSupervisorComment(null);
                tempNew.setSupervisorRating(null);
                tempNew.setRating(null);
                tempNew.setId(null);
                tempNew.setEstimate(0D);
                tempNew.setRatingComment(null);
                tempNew.setEndDate(addDays(new Date(), newDiff));
                tempNew.setStatus(1);

                newList.add(tempNew);
                tempEntity.setSubId(1);
                newList.add(tempEntity);

            }


        });

        taskListRepository.saveAll(newList);


    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public void changeCompleteStatus() {

        List<TaskListEntity> listOjb = taskListRepository.findByAutoStatus(3);

        Date date = new Date();

        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat2.format(date);


        List<TaskListEntity> newList = new ArrayList<>();

        listOjb.forEach(taskListEntity -> {

            TaskListEntity tempEntity = (TaskListEntity) taskListEntity.clone();

            try {
                if (taskListEntity.getEndDate().before(dateFormat2.parse(strDate)) && taskListEntity.getStatus() == 1) {

                    tempEntity.setAutoStatus(1);
                    tempEntity.setStatus(3);
                    tempEntity.setRating(1);
                    tempEntity.setRatingComment("Auto Completed By the System");
                    if (tempEntity.getEstimate() == null || (tempEntity.getEstimate() != null && tempEntity.getEstimate() < 1))
                        tempEntity.setEstimate(0D);

                    newList.add(tempEntity);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });

        taskListRepository.saveAll(newList);

    }


}
