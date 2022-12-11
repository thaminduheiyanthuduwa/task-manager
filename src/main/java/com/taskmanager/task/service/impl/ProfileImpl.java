package com.taskmanager.task.service.impl;

import com.taskmanager.task.repository.ProfileRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.Profile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Data
@Service
public class ProfileImpl implements Profile {
    ArrayList<String> profileDetails = new ArrayList<String>();
    private int user_id;
    @Autowired
    ProfileRepository profileRepository;

    @Override
    public ResponseList getAllProfileDetails(int id) {
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Profile All data");
        responseList.setData(profileRepository.findById(id));
        return responseList;
    }
    @Override
    public  ResponseList getAboutDetails(int id){
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Profile All data");
        responseList.setData(profileRepository.getUserAboutDetails(id));
        return responseList;
    }
    @Override
    public ResponseList getHierarchyDetails(int id) {
//        int id = getUser_id();
//        System.out.println(profileRepository.findById(id));
//        profileDetails.add(profileRepository.findById(id).toString());
        System.out.println("djhs");


        ResponseList responseList = new ResponseList();
        return responseList;
    }
    @Override
    public ResponseList getPostDetails(int id){
        ResponseList responseList = new ResponseList();
        return responseList;
    }



    @Override
    public  ResponseList getEducationalDetails(int id){
        ResponseList responseList = new ResponseList();
        return responseList;
    }

    @Override
    public  ResponseList getExperiencesDetails(int id){
        ResponseList responseList = new ResponseList();
        return responseList;
    }






}
