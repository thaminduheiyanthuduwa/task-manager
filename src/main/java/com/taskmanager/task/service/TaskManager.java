package com.taskmanager.task.service;
import com.taskmanager.task.entity.TaskListEntity;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.response.ResponseList;

import java.text.ParseException;
import java.util.List;


public interface TaskManager {


    ResponseList addProfile();

    ResponseList updateProfile(int id);


    ResponseList getEmpDetailEntityById(int id);

    ResponseList createTask(CreateTask createTask);

    ResponseList getMyTask(int id, Integer type);

    ResponseList getMyTaskById(int id, int task);

    ResponseList deleteRequest(int taskId, int userId);

    ResponseList deleteBySupervisor(int taskId, int userId);

    ResponseList revertBySupervisor(int taskId, int userId);

    ResponseList editTask(CreateTask createTask, int taskId, int userId) throws ParseException;

    ResponseList login(String email, String id);

    ResponseList getChildListForSupervisor(int id);

    ResponseList getCompletedTaskCount(int id) throws ParseException;

    ResponseList getLastSevenDaysCompletedRate(int id) throws ParseException;

    ResponseList getLastFourDue(int id) throws ParseException;

    ResponseList getTotalStoryPoints(int id) throws ParseException;

    void sendMorningEmail();

    ResponseList updateNotApplicable(int taskId, int userId, int supervisor);

    ResponseList getIsSupervisorReviewNeeded(int supervisor);

    ResponseList getTotalEstimateByTask(int task);

    void setRecurring();

    void changeCompleteStatus();

    ResponseList getDailyTaskCount(Integer id);

}
