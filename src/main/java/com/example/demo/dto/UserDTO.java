package com.example.demo.dto;
import com.example.demo.Enum.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;

public class UserDTO {

    private String fullName;
    private String email;
    private Role role;// Fix: Change Enum to Role
    @JsonProperty("reportsTo_Id")
    private Long reportsTo_Id;
    //  private UserDTO reportsTo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joining_date;
    private String password;
    private Integer team;
    private String mobile_No;
    @JsonProperty("leaves_allowed")
    private Map<String, Integer> leavesAllowed;

    @Override
    public String toString() {
        return "UserDTO{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", reportsToId=" + reportsTo_Id +
                ", joining_date=" + joining_date +
                ", password=" +password +  // Password ko log mein mat dikhaiye
                ", team='" + team + '\'' +
                ", mobileNo='" + mobile_No + '\'' +
                ", leavesAllowed=" + leavesAllowed +
                '}';
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

    public Long getReportsToId() {
        return reportsTo_Id;
    }

    public void setReportsToId(Long reportsToId) {
        this.reportsTo_Id = reportsToId;
    }

    public LocalDate getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(LocalDate joining_date) {
        this.joining_date = joining_date;
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
        return mobile_No;
    }

    public void setMobileNo(String mobile_No) {
        this.mobile_No = mobile_No;
    }


    //private Map<String, Integer> leavesAllowed;

    // Getters and Setters
    public Map<String, Integer> getLeavesAllowed() {
        return leavesAllowed;
    }

    public void setLeavesAllowed(Map<String, Integer> leavesAllowed) {
        this.leavesAllowed = leavesAllowed;
    }
}