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
@Table(name = "news_feed")
public class NewsFeedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "posted_user_id")
    private int user_id;

    @Column(name = "profile_pic")
    private String profile_pic;

    @Column(name="profile_name")
    private String profile_name;

    @Column(name="posted_date")
    private String posted_date;

    @Column(name="post_description")
    private String post_description;

    @Column(name="post_image")
    private String post_image;

    @Column(name="likes_count")
    private int likes_count;

    @Column(name="comments_count")
    private int comments_count;

}
