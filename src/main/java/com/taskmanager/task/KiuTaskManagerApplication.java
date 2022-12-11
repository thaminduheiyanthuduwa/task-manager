package com.taskmanager.task;

import com.taskmanager.task.service.impl.ProfileImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class KiuTaskManagerApplication {

    public static void main(String[] args) {

        SpringApplication.run(KiuTaskManagerApplication.class, args);


        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR, 11);
        date.set(Calendar.MINUTE, 43);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
//						TaskController task = new TaskController();
//						task.setRecurring();
                    }
                },
                date.getTime(),
                1000 * 60 * 20
        );


//        ProfileImpl profile= new ProfileImpl();
//        profile.setHierarchy();
    }

}
