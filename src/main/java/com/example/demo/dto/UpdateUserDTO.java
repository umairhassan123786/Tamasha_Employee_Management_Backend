package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Map;

public class UpdateUserDTO {

    private String fullName;
    private String email;
    private Long reportsTo_Id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;

    private Integer team;
    private String mobileNo;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
    @JsonProperty("leaves_allowed")
    private Map<String, Integer> leavesAllowed;

    // ======== Getters and Setters =========

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getReportsTo_Id() {
        return reportsTo_Id;
    }

    public void setReportsTo_Id(Long reportsTo_Id) {
        this.reportsTo_Id = reportsTo_Id;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Map<String, Integer> getLeavesAllowed() {
        return leavesAllowed;
    }

    public void setLeavesAllowed(Map<String, Integer> leavesAllowed) {
        this.leavesAllowed = leavesAllowed;
    }
}
