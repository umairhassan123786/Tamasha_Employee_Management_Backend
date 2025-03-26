package com.example.demo.dto;

import com.example.demo.Enum.Role;
import jakarta.validation.constraints.*;

import java.util.Map;

public class AddUserRequest {

    @NotEmpty(message = "Full Name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;

    private Long reportsTo_Id;

    @NotEmpty(message = "Password is required")
    private String password;

//    @NotNull(message = "Team is required")
    private Integer team;

   // @Pattern(regexp = "\\d{9,11}", message = "Mobile number must be 9 to 11 digits")
    private String mobile_No;

    private Map<String, Integer> leavesAllowed;

    // ======== Getters and Setters =========
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getReportsTo_Id() { return reportsTo_Id; }
    public void setReportsTo_Id(Long reportsTo_Id) { this.reportsTo_Id = reportsTo_Id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getTeam() { return team; }
    public void setTeam(Integer team) { this.team = team; }

    public String getMobile_No() { return mobile_No; }
    public void setMobile_No(String mobile_No) { this.mobile_No = mobile_No; }

    public Map<String, Integer> getLeavesAllowed() { return leavesAllowed; }
    public void setLeavesAllowed(Map<String, Integer> leavesAllowed) { this.leavesAllowed = leavesAllowed; }
}
