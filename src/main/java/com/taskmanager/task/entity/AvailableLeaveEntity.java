package com.taskmanager.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "available_leaves")
public class AvailableLeaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "type")
    private String type;

    @Column(name="available_leaves")
    private Float availableLeaves;

    @Column(name="original_leaves")
    private Integer originalLeaves;

}
