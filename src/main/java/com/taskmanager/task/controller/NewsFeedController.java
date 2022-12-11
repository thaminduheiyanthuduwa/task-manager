package com.taskmanager.task.controller;

import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task/profile/feed")
@CrossOrigin(origins = "*")
public class NewsFeedController {

    @Autowired
    TaskManager taskManager;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getProfileDetails(@PathVariable int userId) {
        return taskManager.getEmpDetailEntityById(userId);
    }


}
