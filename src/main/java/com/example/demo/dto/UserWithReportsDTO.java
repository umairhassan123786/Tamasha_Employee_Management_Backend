package com.example.demo.dto;

import com.example.demo.Enum.Role;

import java.time.LocalDate;
import java.util.Map;

public class UserWithReportsDTO {

    private Long user_Id;
    private String fullName;
    private String email;
    private Role role;
    private UserWithReportsDTO reportsTo;
    private LocalDate joiningDate;
    private String password;
    private Integer team;
    private String mobileNo;
    private Map<String, Integer> leavesAllowed;

    // Constructor
    public UserWithReportsDTO(Long user_Id, String fullName, String email, Role role,
                              UserWithReportsDTO reportsTo, LocalDate joiningDate, String password,
                              Integer team, String mobileNo, Map<String, Integer> leavesAllowed) {
        this.user_Id = user_Id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.reportsTo = reportsTo;
        this.joiningDate = joiningDate;
        this.password = password;
        this.team = team;
        this.mobileNo = mobileNo;
        this.leavesAllowed = leavesAllowed;
    }

    // Getters and Setters
    public Long getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(Long user_Id) {
        this.user_Id = user_Id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserWithReportsDTO getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(UserWithReportsDTO reportsTo) {
        this.reportsTo = reportsTo;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
