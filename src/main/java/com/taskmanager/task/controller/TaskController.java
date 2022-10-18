package com.taskmanager.task.controller;

import com.taskmanager.task.entity.TaskListEntity;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    TaskManager taskManager;


    @RequestMapping(value = "/create_task", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList createTask(@RequestBody CreateTask createTask) {

        return taskManager.createTask(createTask);

    }

    @RequestMapping(value = "/get-my-task", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getTask(@RequestParam(value = "user_id") int id) {

        return taskManager.getMyTask(id);

    }

    @RequestMapping(value = "/get-my-task-by-id", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getTask(@RequestParam(value = "user_id") int id, @RequestParam(value = "task") int task) {

        return taskManager.getMyTaskById(id, task);

    }

    @RequestMapping(value = "/delete-task", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList deleteTask(@RequestParam(value = "task_id") int task,
                                   @RequestParam(value = "user_id") int id) {

        return taskManager.deleteRequest(task,id);

    }

    @RequestMapping(value = "/edit_task", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList createTask(@RequestBody CreateTask createTask, @RequestParam(value = "task_id") int task,
                                   @RequestParam(value = "user_id") int id) {

        return taskManager.editTask(createTask, task, id);

    }


}
