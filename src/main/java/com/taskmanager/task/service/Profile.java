package com.taskmanager.task.service;

import com.taskmanager.task.model.profile.CreateProfilePost;
import com.taskmanager.task.response.ResponseList;

public interface Profile {

    ResponseList getAllProfileDetails(int id);
    ResponseList getHierarchyDetails(int id);
    ResponseList getPostDetails(int id);
    ResponseList getAboutDetails(int id);
    ResponseList getEducationalDetails(int id);
    ResponseList getExperiencesDetails(int id);
    ResponseList addProfilePost(int id, CreateProfilePost createPost);

    ResponseList getProfilePost(int id);

    ResponseList getNewFeatureAlertCount(int id, int status);


}
