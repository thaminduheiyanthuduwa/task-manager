package com.taskmanager.task.model.roster;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;

@Getter
@Setter
public class SupervisorRosterList {

    @JoinColumn(name = "id")
    private int id;

    @JoinColumn(name = "email")
    private String email;

    @JoinColumn(name = "nic_no")
    private String nicNo;

    @JoinColumn(name = "given_name")
    private String givenName;

    @JoinColumn(name = "name_in_full")
    private String nameInFull;

    @JoinColumn(name = "contact_no")
    private String contactNo;

    @JoinColumn(name = "supervisor")
    private Integer supervisor;

    @JoinColumn(name = "pending_roster")
    private Integer pendingRoster;

    @JoinColumn(name = "change_requested")
    private Integer changeRequested;

    @JoinColumn(name = "review_needed")
    private Integer reviewNeeded;

    public SupervisorRosterList() {}

}
