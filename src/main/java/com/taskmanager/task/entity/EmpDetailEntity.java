package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "people")
public class EmpDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "nic_no")
    private String nicNo;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "name_in_full")
    private String nameInFull;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "supervisor")
    private Integer supervisor;

    public EmpDetailEntity() {}

}
