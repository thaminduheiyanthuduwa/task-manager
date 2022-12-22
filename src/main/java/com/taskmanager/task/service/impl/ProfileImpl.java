package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.*;
import com.taskmanager.task.model.profile.CreateProfilePost;
import com.taskmanager.task.model.profile.GetProfilePost;
import com.taskmanager.task.repository.EmpDetailRepository;
import com.taskmanager.task.repository.FeatureAlertRepository;
import com.taskmanager.task.repository.ProfilePostRepository;
import com.taskmanager.task.repository.ProfileRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.Profile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    FeatureAlertRepository featureAlertRepository;

    @Autowired
    ProfilePostRepository profilePostRepository;


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

        aboutEntity.setName_in_full(empDetail.getNameInFull());
        aboutEntity.setEmail(empDetail.getEmail());
        aboutEntity.setDate_of_birth(empDetail.getDateOfBirth());
        aboutEntity.setPerm_address(empDetail.getPermAddress());


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

    @Override
    public ResponseList addProfilePost(int id, CreateProfilePost createPost) {

        Optional<EmpDetailEntity> userId = empDetailRepository.findById(id);

        ProfilePost profilePost = new ProfilePost();
        profilePost.setEmpId(id);
        profilePost.setTitle(createPost.getTitle());
        profilePost.setCategory("");
        profilePost.setSlug(createPost.getSlug());
        profilePost.setStatus(createPost.getStatus());
        profilePost.setContent(createPost.getContent().getBytes());
        profilePost.setCreatedBy(id);
        profilePost.setCreatedDate(new Date());
        profilePost.setUserName(userId.get().getNameInFull());

        profilePostRepository.save(profilePost);

        ResponseList responseList = new ResponseList();
        return responseList;

    }

    @Override
    public ResponseList getProfilePost(int id) {

        List<ProfilePost> value = profilePostRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<GetProfilePost> getProfilePost = new ArrayList<>();

        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        for (ProfilePost obj : value) {

            GetProfilePost post = new GetProfilePost();
            post.setAvatar("");
            post.setUsername(obj.getUserName());
            post.setPostTime(dateFormat2.format(obj.getCreatedDate()));
            String newContent = new String(obj.getContent());
            post.setPostText(newContent);
            getProfilePost.add(post);
        }

        List<NewFeatureAlertEntity> checkStatus = featureAlertRepository.findByEmpId(id);

        if (!checkStatus.isEmpty() && checkStatus.get(0).getCount() > 1){
            NewFeatureAlertEntity obj = checkStatus.get(0);
            obj.setCount(3);
            featureAlertRepository.save(obj);
        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(getProfilePost);
        return responseList;


    }

    @Override
    public ResponseList getNewFeatureAlertCount(int id, int status) {

        List<NewFeatureAlertEntity> checkStatus = featureAlertRepository.findByEmpId(id);

        Integer count = 0;

        if (checkStatus.isEmpty()){
            NewFeatureAlertEntity newFeatureAlertEntity = new NewFeatureAlertEntity();
            newFeatureAlertEntity.setEmpId(id);
            newFeatureAlertEntity.setCount(1);
            featureAlertRepository.save(newFeatureAlertEntity);
        }
        else if (checkStatus.get(0).getCount() == 1){
            NewFeatureAlertEntity obj = checkStatus.get(0);
            obj.setCount(2);
            featureAlertRepository.save(obj);
            count = 1;
        }
        else if (checkStatus.get(0).getCount() == 3){
            count = 2;
        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(count);
        return responseList;

    }


}
