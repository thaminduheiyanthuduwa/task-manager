package com.taskmanager.task.service;
import com.taskmanager.task.entity.TaskListEntity;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.response.ResponseList;

import java.util.List;


public interface TaskManager {

    ResponseList createTask(CreateTask createTask);

    ResponseList getMyTask(int id);

    ResponseList getMyTaskById(int id, int task);

    ResponseList deleteRequest(int taskId, int userId);

    ResponseList editTask(CreateTask createTask, int taskId, int userId);

}
