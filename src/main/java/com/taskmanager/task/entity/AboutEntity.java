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
public class AboutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name_in_full")
    private String name_in_full;

    @Column(name = "date_of_birth")
    private String date_of_birth;

    @Column(name = "perm_address")
    private String perm_address;

    @Column(name = "email")
    private String email;

}
