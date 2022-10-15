package com.taskmanager.task.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Response {

    private int code;
    private String msg;
    private Object data;

}
