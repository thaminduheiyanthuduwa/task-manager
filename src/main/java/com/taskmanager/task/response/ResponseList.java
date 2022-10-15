package com.taskmanager.task.response;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.Any;

import java.util.List;

@Getter
@Setter
public class ResponseList {

    private int code;
    private String msg;
    private Object data;
}
