package com.taskmanager.task.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) throws IOException {




        try {
            FileWriter myWriter = new FileWriter("filename.txt");

            List<String> list = new ArrayList<>();
            list.add("C:\\Users\\Thamindu H\\Desktop\\buddothpado\\file\\extract-2023-03-06T10_02_35.750Z.csv");
            list.add("C:\\Users\\Thamindu H\\Desktop\\buddothpado\\file\\extract-2023-03-06T10_04_45.681Z.csv");
            list.add("C:\\Users\\Thamindu H\\Desktop\\buddothpado\\file\\extract-2023-03-06T10_06_18.165Z.csv");
            list.add("C:\\Users\\Thamindu H\\Desktop\\buddothpado\\file\\extract-2023-03-06T10_07_09.579Z.csv");
            list.add("C:\\Users\\Thamindu H\\Desktop\\buddothpado\\file\\extract-2023-03-06T10_07_48.798Z.csv");


            for (String obj : list){
                int state = 0;

                File file = new File(
                        obj);

                BufferedReader br
                        = new BufferedReader(new FileReader(file));

                String st;
                while ((st = br.readLine()) != null) {

                    if (st.contains("/short")){
                        System.out.println(st);
                    }

                    Pattern pattern = Pattern.compile("sequence=(\\d+)");
                    Pattern pattern2 = Pattern.compile("resending=([A-Z])");
                    Matcher matcher = pattern.matcher(st);
                    Matcher matcher2 = pattern2.matcher(st);

                    if (matcher.find())
                    {
                        if (state == Integer.parseInt(matcher.group(1))){
                            if (matcher2.find())
                            {
                                if (matcher2.group(1).equalsIgnoreCase("N")){
                                    myWriter.write(st);
                                    myWriter.write("\n");
                                }
                            }
                        }
                        else {
                            state = Integer.parseInt(matcher.group(1));
                        }
                    }
                }
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

}
