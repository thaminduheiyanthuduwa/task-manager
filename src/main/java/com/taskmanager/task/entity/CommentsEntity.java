package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class CommentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "commented_user_id")
    private int user_id;

    @Column(name = "profile_pic")
    private String profile_pic;

    @Column(name = "profile_name")
    private String profile_name;

    @Column(name = "comment")
    private String comment;

    @Column(name="likes_count")
    private int likes_count;

}
