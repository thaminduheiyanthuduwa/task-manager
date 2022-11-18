package com.taskmanager.task.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) throws IOException {

        File file = new File(
                "C:\\Users\\Thamindu H\\Pictures\\Admin Kui\\Admin Kui\\file2.csv");


        BufferedReader br
                = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String st;
        // Condition holds true till
        // there is character in a string
        while ((st = br.readLine()) != null) {

            Pattern pattern = Pattern.compile("\"\"batchNo\"\":\"\"(.*?)\"\"");
            Matcher matcher = pattern.matcher(st);
            if (matcher.find())
            {
                System.out.println(matcher.group(1));
            }
        }

    }

}
