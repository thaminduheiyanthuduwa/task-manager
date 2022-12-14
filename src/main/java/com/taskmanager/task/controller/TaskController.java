package com.taskmanager.task.controller;

import com.taskmanager.task.entity.TaskListEntity;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

import java.text.ParseException;
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
    public ResponseList getTask(@RequestParam(value = "user_id") int id,
                                @RequestParam(value = "type") @Nullable Integer type) {

        return taskManager.getMyTask(id, type);

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

    @RequestMapping(value = "/delete-task-supervisor", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList deleteTaskBySupervisor(@RequestParam(value = "task_id") int task,
                                   @RequestParam(value = "user_id") int id) {

        return taskManager.deleteBySupervisor(task,id);

    }

    @RequestMapping(value = "/revert-task-supervisor", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseList revertTaskBySupervisor(@RequestParam(value = "task_id") int task,
                                               @RequestParam(value = "user_id") int id) {

        return taskManager.revertBySupervisor(task,id);

    }

    @RequestMapping(value = "/edit_task", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseList createTask(@RequestBody CreateTask createTask, @RequestParam(value = "task_id") int task,
                                   @RequestParam(value = "user_id") int id) {

        return taskManager.editTask(createTask, task, id);

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList login(@RequestParam(value = "email") String email,
                                   @RequestParam(value = "id") String id) {

        return taskManager.login(email, id);

    }

    @RequestMapping(value = "/get-child-for-supervisor", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList login(@RequestParam(value = "supervisor") int id) {

        return taskManager.getChildListForSupervisor(id);

    }

    @RequestMapping(value = "/get-completed-task-count", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getCompletedTaskCount(@RequestParam(value = "id") int id) throws ParseException {

        return taskManager.getCompletedTaskCount(id);

    }

    @RequestMapping(value = "/get-last-seven-days-count", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getLastSevenDaysCount(@RequestParam(value = "id") int id) throws ParseException {

        return taskManager.getLastSevenDaysCompletedRate(id);

    }

    @RequestMapping(value = "/get-last-due-four", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getLastDueFour(@RequestParam(value = "id") int id) throws ParseException {

        return taskManager.getLastFourDue(id);

    }

    @RequestMapping(value = "/total-story-points-per-day", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getTotalStoryPoints(@RequestParam(value = "id") int id) throws ParseException {

        return taskManager.getTotalStoryPoints(id);

    }

    @RequestMapping(value = "/send-morning-msg", method = RequestMethod.GET, headers = "Accept=application/json")
    public void sendMorningMsg() throws ParseException {

        taskManager.sendMorningEmail();

    }


    @RequestMapping(value = "/update-not-applicable", method = RequestMethod.PUT, headers = "Accept=application/json")
    public void updateNotApplicable(@RequestParam(value = "task_id") int task,
                                    @RequestParam(value = "is_supervisor") int supervisor,
                                    @RequestParam(value = "user_id") int id) throws ParseException {

        taskManager.updateNotApplicable(task,id,supervisor);

    }

    @RequestMapping(value = "/review-needed", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList checkReviewNeeded(@RequestParam(value = "supervisor_id") int supervisor) throws ParseException {

        return taskManager.getIsSupervisorReviewNeeded(supervisor);

    }

    @RequestMapping(value = "/total-estimate-by-task", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getTotalEstimateByTask(@RequestParam(value = "task") int task) throws ParseException {

        return taskManager.getTotalEstimateByTask(task);

    }

    @RequestMapping(value = "/start-recurring", method = RequestMethod.GET, headers = "Accept=application/json")
    public void setRecurring() {

        taskManager.setRecurring();

    }


    @RequestMapping(value = "/start-auto-complete", method = RequestMethod.GET, headers = "Accept=application/json")
    public void setAutoComplete() {

        taskManager.changeCompleteStatus();

    }


}
