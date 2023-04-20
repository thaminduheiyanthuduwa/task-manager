package com.taskmanager.task.controller;

import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.PerformanceManager;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/performance")
@CrossOrigin(origins = "*")
public class PerformanceController {

    @Autowired
    PerformanceManager performanceManager;

    @RequestMapping(value = "/get-performance-by-id/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getAttendance(@PathVariable(value = "id") int id) {
        return performanceManager.getPerformanceByUserId(id);

    }



}
