package com.taskmanager.task.service.impl;

import com.taskmanager.task.entity.TaskListEntity;
import com.taskmanager.task.model.CreateTask;
import com.taskmanager.task.repository.TaskListRepository;
import com.taskmanager.task.response.ResponseList;
import com.taskmanager.task.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskManagerImpl implements TaskManager {

    @Autowired
    private TaskListRepository taskListRepository;

    @Override
    public ResponseList createTask(CreateTask createTask) {

        TaskListEntity taskListEntity = new TaskListEntity();

        taskListEntity.setUserId(createTask.getUserId());
        taskListEntity.setCategoryId(createTask.getCategoryId());
        taskListEntity.setTaskTitle(createTask.getTaskTitle());
        taskListEntity.setTaskDescription(createTask.getTaskDescription());
        taskListEntity.setStartDate((new Date()));
        taskListEntity.setEndDate(new Date());
        taskListEntity.setReporterId(createTask.getReporterId());
        taskListEntity.setLabel(createTask.getLabel());
        taskListEntity.setEstimate(createTask.getEstimate());
        taskListEntity.setOriginalEstimate(createTask.getOriginalEstimate());
        taskListEntity.setBlockedTask(createTask.getBlockedTask());
        taskListEntity.setStatus(1);
        taskListEntity.setIsActive(1);
        taskListEntity.setPriority(createTask.getPriority());
        taskListEntity.setLastUpdatedUser(createTask.getLastUpdatedUser());
        taskListEntity.setLastUpdatedDate(new Date());

        taskListRepository.save(taskListEntity);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;
    }

    @Override
    public ResponseList getMyTask(int id) {

        List<TaskListEntity> list = taskListRepository.findByUserIdOrderByIdDesc(id);

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");
        responseList.setData(list);

        return responseList;

    }

    @Override
    public ResponseList deleteRequest(int taskId, int userId) {

        Optional<TaskListEntity> optObj = taskListRepository.findById(taskId);

        TaskListEntity obj = optObj.get();

        obj.setStatus(2);
        obj.setLastUpdatedUser(userId);
        obj.setLastUpdatedDate(new Date());

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;


    }

    @Override
    public ResponseList editTask(CreateTask createTask, int taskId, int userId) {

        Optional<TaskListEntity> task = taskListRepository.findById(taskId);

        if (!task.isEmpty()) {

            TaskListEntity obj = task.get();

            if (createTask.getRating() != null)
                obj.setRating(createTask.getRating());
            if (createTask.getRatingComment() != null)
                obj.setRatingComment(createTask.getRatingComment());
            if (createTask.getSupervisorRating() != null)
                obj.setSupervisorRating(createTask.getSupervisorRating());
            if (createTask.getSupervisorComment() != null)
                obj.setSupervisorComment(createTask.getSupervisorComment());


            obj.setLastUpdatedUser(userId);
            obj.setLastUpdatedDate(new Date());

        }

        ResponseList responseList = new ResponseList();
        responseList.setCode(200);
        responseList.setMsg("Success");

        return responseList;

    }
}
