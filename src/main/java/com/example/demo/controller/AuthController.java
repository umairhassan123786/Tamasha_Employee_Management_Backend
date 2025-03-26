package com.example.demo.controller;
import com.example.demo.Enum.Role;
import com.example.demo.Models.Leave;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.LeaveService;
import com.example.demo.Service.UserService;
import com.example.demo.dto.*;
import com.example.demo.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/user")
public class AuthController {
    @Autowired
    private final UserService userService;
    private final UserRepository userRepository;
    // private  final SecurityConfig config;
     private  final JwtUtil jwtUtil;
     private final PasswordEncoder passwordEncoder;
     private final LeaveService leaveService;
    @Autowired
    public AuthController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, LeaveService leaveService)
    {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.leaveService = leaveService;
    }
    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

//login without jwt
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
//        try {
//            String email = payload.get("email");
//            String password = payload.get("password");
//
//            Optional<User> user = userService.validateUser(email, password);
//            if (user.isPresent()) {
//                return ResponseEntity.ok(user.get()); // Send user object directly
//            } else {
//                return ResponseEntity.status(401).body("Invalid credentials");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
//        }
//    }
/// login with jwt  |
/// //             \/
//@PostMapping("/login")
//public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//    Optional<User> user = userRepository.findByemail(request.getEmail());
//
//    if (user.isPresent() && request.getPassword().equals(user.get().getPassword())) {
//        Map<String, String> tokens = jwtUtil.generateTokens(
//                user.get().getUser_Id().toString(),
//                user.get().getEmail(),
//                user.get().getRole().toString()
//        );
//
//        // Response Map
//        Map<String, Object> response = new HashMap<>();
//        response.put("accessToken", tokens.get("accessToken"));
//        response.put("refreshToken", tokens.get("refreshToken"));
//        response.put("user", user.get());
//
//        return ResponseEntity.ok(response);
//    }
//    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//}
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<User> optionalUser = userRepository.findByemail(request.getEmail());

    if (optionalUser.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    User user = optionalUser.get();
    String dbPassword = user.getPassword();
    String enteredPassword = request.getPassword();

    // ✅ Check Password (BCrypt or Plain)
    boolean passwordMatches = dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")
            ? passwordEncoder.matches(enteredPassword, dbPassword) // BCrypt Check
            : enteredPassword.equals(dbPassword); // Plain Password Check

    if (!passwordMatches) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // ✅ Generate Tokens
    Map<String, String> tokens = jwtUtil.generateTokens(
            user.getUser_Id().toString(),
            user.getEmail(),
            user.getRole().toString()
    );

    // ✅ Response Map
    Map<String, Object> response = new HashMap<>();
    response.put("accessToken", tokens.get("accessToken"));
    response.put("refreshToken", tokens.get("refreshToken"));
    response.put("user", user);

    return ResponseEntity.ok(response);
}

    // ✅ Refresh Token Endpoint
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh Token is required");
        }

        try {
            if (!jwtUtil.isTokenExpired(refreshToken)) {
                String userId = jwtUtil.extractUserId(refreshToken);
                String email = jwtUtil.extractEmail(refreshToken);
                String role = jwtUtil.extractClaims(refreshToken).get("role", String.class);

                // ✅ Generate New Tokens
                Map<String, String> newTokens = jwtUtil.generateTokens(userId, email, role);

                return ResponseEntity.ok(Map.of(
                        "accessToken", newTokens.get("accessToken"),
                        "refreshToken", newTokens.get("refreshToken")
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token Expired");
    }

    @GetMapping("/print")
    public String returnc(){
        return "Hello World";
    }
    private static final Logger logger1 = Logger.getLogger(UserService.class.getName());
    @GetMapping("/getUsersByRole")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam String role) {
        List<User> users = userService.getUsersByRole(role);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }



//pehly ye add api call horahi thi without jwt token
//    @PostMapping(value = "/adduser", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public User signup(@RequestBody UserDTO userDTO) {
//        return userService.signup(userDTO);
//    }

//    @GetMapping
//    public ResponseEntity<List<User>> getUsersByRole(@RequestParam String role) {
//        List<User> users = userService.getUsersByRole(role);
//        return ResponseEntity.ok(users);
//    }
    @PutMapping(value = "/updateuser/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
       return userService.updateUser(id, userDTO);
    }
//    @PostMapping
//    public ResponseEntity<User> addUser(@RequestBody @Valid AddUserRequest request,
//                                        @RequestHeader("Authorization") String token) throws AccessDeniedException {
//        String jwtToken = token.replace("Bearer ", "");
//        User newUser = userService.addUser(request, jwtToken);
//        return ResponseEntity.ok(newUser);
//    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserWithReportsDTO>> getAllUsers() {
        List<UserWithReportsDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
//  ye without jwt ka kaam kr rha tha
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok("User deleted successfully");
//    }
/// /////only director can edit all users(managers,team_leads and team_members,managers can update and delete team_lead and
///    team_member and team lead cannot do nothing )
   @PostMapping("/adduser")
   public ResponseEntity<?> addUser(@RequestBody @Valid AddUserRequest request,
                                 @RequestHeader("Authorization") String token) {
    String jwtToken = token.replace("Bearer ", "");
    try {
        User newUser = userService.addUser(request, jwtToken);
        return ResponseEntity.ok(newUser);
    } catch (ResponseStatusException ex) {
        // Log and return error in JSON format to Postman
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    } catch (Exception ex) {
        return ResponseEntity.status(500).body("Internal Server Error: " + ex.getMessage());
    }
   }

    @PutMapping("update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UpdateUserDTO user, @RequestHeader("Authorization") String token) throws AccessDeniedException {
        String jwtToken = token.replace("Bearer ", "");
        User updatedUser = userService.updateUser(userId, user, jwtToken);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId, @RequestHeader("Authorization") String token) throws AccessDeniedException {
        String jwtToken = token.replace("Bearer ", "");
        userService.deleteUser(userId, jwtToken);
        return ResponseEntity.ok("User deleted successfully");
    }
}