package com.example.demo.Models;

import com.example.demo.Enum.Role;
import com.example.demo.config.LeavesAllowedConverter;
import com.example.demo.converter.RoleConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)  // If you want auto-increment ID
    private Long user_Id;

    @Column(nullable = false, unique = true,name = "fullName")
    @NotEmpty(message = "Full Name is required")
    private String fullName;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    private String email;

    @Convert(converter = RoleConverter.class)
    @NotNull(message = "Role is required")
    private Role role;

    @JsonProperty("reportsTo_Id")
    private Long reportsTo_Id;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joining_date;

    @PrePersist
    protected void onCreate() {
        if (joining_date == null) {
            joining_date = LocalDate.now();
        }
    }
    @PreUpdate
    protected void onUpdate() {
        // Do nothing, joining_date will stay the same unless explicitly updated
    }
    @NotEmpty(message = "Password is required")
    private String password;

    @NotNull(message = "Team is required")
    private Integer team;

  //  @Column(length = 11)
  //   @Pattern(regexp = "\\d{9,11}", message = "Mobile number must be 9 to 11 digits")
    private String mobile_No;


    @Convert(converter = LeavesAllowedConverter.class)
    @Column(columnDefinition = "TEXT")  // JSON format in DB
    private Map<String, Integer> leaves_allowed;

    // ======== Getters and Setters =========
    public Long getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(Long user_Id) {
        this.user_Id = user_Id;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullname) {
        this.fullName = fullname;
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

    @JsonProperty("reportsTo_Id")
    public Long getReportsTo_Id() {
        return reportsTo_Id;
    }

    @JsonProperty("reportsTo_Id")
    public void setReportsTo_Id(Long reportsTo_Id) {
        this.reportsTo_Id = reportsTo_Id;
    }

@JsonProperty("joining_date")
    public LocalDate getJoiningDate() {
        return joining_date;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joining_date = joiningDate;
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

    public String getMobile_No() {
        return mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        this.mobile_No = mobile_No;
    }

    public Map<String, Integer> getLeavesAllowed() {
        return leaves_allowed;
    }

    public void setLeavesAllowed(Map<String, Integer> leavesAllowed) {
        this.leaves_allowed = leavesAllowed;
    }
}
