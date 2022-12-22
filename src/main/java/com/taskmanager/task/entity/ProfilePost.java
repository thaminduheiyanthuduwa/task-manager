package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "profile_post")
public class ProfilePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "user_id")
    private Integer empId;

    @Column(name = "title")
    private String title;

    @Column(name = "category")
    private String category;

    @Column(name = "slug")
    private String slug;

    @Column(name = "status")
    private String status;

    @Lob
    @Column(name = "content")
    @Type(type = "org.hibernate.type.ImageType")
    byte[] content;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "user_name")
    private String userName;

    public ProfilePost() {}
}
