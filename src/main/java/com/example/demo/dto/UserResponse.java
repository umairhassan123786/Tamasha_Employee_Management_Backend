//package com.example.demo.dto;
//
//import com.example.demo.Enum.Role;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDate;
//import java.util.Map;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserResponse {
//    private Long user_Id;
//    private String fullName;
//    private String email;
//    private Role role;
//    private UserResponse reportsTo;
//    private LocalDate joiningDate;
//    private String password;
//    private Integer team;
//    private String mobileNo;
//    private Map<String,Integer> leavesAllowed;
//
//    public UserResponse(Long user_Id, String fullName, String email, Role role, UserResponse reportsTo,
//                        LocalDate joiningDate, String password, Integer team, String mobileNo, Map<String,Integer> leavesAllowed) {
//        this.user_Id = user_Id;
//        this.fullName = fullName;
//        this.email = email;
//        this.role = role;
//        this.reportsTo = reportsTo;
//        this.joiningDate = joiningDate;
//        this.password = password;
//        this.team = team;
//        this.mobileNo = mobileNo;
//        this.leavesAllowed = leavesAllowed;
//    }
//}
