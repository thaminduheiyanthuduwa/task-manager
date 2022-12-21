package com.taskmanager.task.controller;

import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.Profile;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task/profile")
@CrossOrigin(origins = "*")
public class ProfileController {
    @Autowired
    TaskManager taskManager;

   @Autowired
   Profile profile;

    @RequestMapping(value = "/details/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getProfileDetails(@PathVariable int userId) {
        return profile.getAllProfileDetails(userId);
    }

    @RequestMapping(value = "/post_details/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getPost(@PathVariable int userId) {
        return profile.getPostDetails(userId);
    }

    @RequestMapping(
            value = "/aboutDetails/{userId}",
            method = RequestMethod.GET,
            headers = "Accept=application/json")
    public ResponseList getAbout(@PathVariable int userId) {
        return profile.getAboutDetails(userId);
    }
    @RequestMapping(value = "/hierarchy_details/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getHierarchy(@PathVariable int userId) {
        return profile.getHierarchyDetails(userId);
    }
    @RequestMapping(value = "/educational_details/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getEducational(@PathVariable int userId) {
        return profile.getEducationalDetails(userId);
    }
    @RequestMapping(value = "/experiences_details/{userId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseList getExperiences(@PathVariable int userId) {
        return profile.getExperiencesDetails(userId);
    }



}
