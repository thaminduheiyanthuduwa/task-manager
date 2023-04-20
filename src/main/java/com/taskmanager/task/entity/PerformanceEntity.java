package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "performance_values")
public class PerformanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private int userId;

    @Column(name="date")
    private Date date;

    @Column(name="score1")
    private Float score1;

    @Column(name="score2")
    private Float score2;

    @Column(name="score3")
    private Float score3;

    @Column(name="score4")
    private Float score4;

    @Column(name="score5")
    private Float score5;

    @Column(name="score6")
    private Float score6;

    @Column(name="score7")
    private Float score7;

    @Column(name="score1_rank")
    private Integer score1Rank;

    @Column(name="score2_rank")
    private Integer score2Rank;

    @Column(name="score3_rank")
    private Integer score3Rank;

    @Column(name="score4_rank")
    private Integer score4Rank;

    @Column(name="score5_rank")
    private Integer score5Rank;

    @Column(name="score6_rank")
    private Integer score6Rank;

    @Column(name="score7_rank")
    private Integer score7Rank;


}
