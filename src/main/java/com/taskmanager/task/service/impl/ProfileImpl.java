package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.AboutEntity;
import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.ProfileEntity;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.ProfileRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.Profile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ProfileImpl implements Profile {
    ArrayList<String> profileDetails = new ArrayList<String>();
    ArrayList<EmpDetailEntity> aboutDetails = new ArrayList<EmpDetailEntity>();
    private int user_id;
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    EmpDetailRepository empDetailRepository;


    @Override
    public ResponseList getAllProfileDetails(int id) {
        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Profile All data");
        responseList.setData(profileRepository.findById(id));
        return responseList;
    }

    @Override
    public ResponseList getAboutDetails(int id) {

        Optional<EmpDetailEntity> empDetailEntity = empDetailRepository.findById(id);
//        profileRepository.findById(id);
        EmpDetailEntity empDetail = empDetailEntity.get();
        AboutEntity aboutEntity = new AboutEntity();


//        listP.add(profileRepository.findById(id));
        List<AboutEntity> list = new ArrayList<>();

        aboutEntity.setName_in_full(empDetail.getName_in_full());
        aboutEntity.setEmail(empDetail.getEmail());
        aboutEntity.setDate_of_birth(empDetail.getDate_of_birth());
        aboutEntity.setPerm_address(empDetail.getPerm_address());


        list.add(aboutEntity);

        System.out.println("about List:" + list);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Profile about data");
        responseList.setData(list);
        return responseList;
    }

    @Override
    public ResponseList getHierarchyDetails(int id) {

        ResponseList responseList = new ResponseList();
        return responseList;
    }

    @Override
    public ResponseList getPostDetails(int id) {
        ResponseList responseList = new ResponseList();
        return responseList;
    }


    @Override
    public ResponseList getEducationalDetails(int id) {
        ResponseList responseList = new ResponseList();
        return responseList;
    }

    @Override
    public ResponseList getExperiencesDetails(int id) {
        ResponseList responseList = new ResponseList();
        return responseList;
    }


}
