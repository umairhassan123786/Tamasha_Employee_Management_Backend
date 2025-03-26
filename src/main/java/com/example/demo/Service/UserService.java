package com.example.demo.Service;
import com.example.demo.Enum.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.*;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<User> validateUser(String email, String password) {
        Optional<User> user = userRepository.findByemail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }

        return Optional.empty();
    }
    public User updateUser(Long userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFullname(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setRole(userDTO.getRole());
        existingUser.setReportsTo_Id(userDTO.getReportsToId());
    if (userDTO.getJoining_date() != null) {
            existingUser.setJoiningDate(userDTO.getJoining_date());
        } else {
            existingUser.setJoiningDate(existingUser.getJoiningDate());
        }

        existingUser.setPassword(userDTO.getPassword());
        existingUser.setTeam(userDTO.getTeam());
        existingUser.setMobile_No(userDTO.getMobileNo());
        existingUser.setLeavesAllowed(userDTO.getLeavesAllowed());

        logger.info("User updated: " + existingUser);
        return userRepository.save(existingUser);
    }
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
//     public User signup(UserDTO userDTO) {
//        logger.info("User added request: " + userDTO.toString());
//        System.out.println(userDTO);
//        // Max ID logic
//        Long maxId = userRepository.findMaxUserId();
//        Long newId = maxId + 1;
//        // User object create
//        User user = new User();
//        user.setUser_Id(newId);
//        user.setFullname(userDTO.getFullName());
//        user.setEmail(userDTO.getEmail());
//        user.setRole(userDTO.getRole());
//        user.setReportsTo_Id(userDTO.getReportsToId());
//        user.setJoiningDate(userDTO.getJoining_date() != null ? userDTO.getJoining_date() : LocalDate.now());
//        user.setPassword(userDTO.getPassword());
//        user.setTeam(userDTO.getTeam());
//        user.setMobile_No(userDTO.getMobileNo());
//        user.setLeavesAllowed(userDTO.getLeavesAllowed());
//        logger.info("User to be saved: " + userDTO.toString());
//        //logger.info((Supplier<String>) userDTO);
//        // Save user
//        return userRepository.save(user);
//     }

    public List<User> getUsersByRole(String roleStr) {
        try {
            // ✅ Token extract kar lo
            String token = getTokenFromSecurityContext();
            String userId = jwtUtil.extractUserId(token);
            if (userId == null) {
                throw new RuntimeException("User ID not found in token");
            }


            // ✅ Step 1: User fetch karna taake team mil sake
            User user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long teamId = Long.valueOf(user.getTeam());  // Team ID lena
            Role role = Role.fromValue(roleStr);  // Role ko enum mein convert karna

            // ✅ Step 2: Higher roles nikalna
            List<Role> upperRoles = getUpperRoles(role);
            if (upperRoles.isEmpty()) {
                logger.warning("No upper roles found for: " + roleStr);
                return Collections.emptyList();
            }

            // ✅ Step 3: Same team ke higher roles wale users fetch karna
            logger.info("User Requested - Team ID: " + teamId + ", Upper Roles: " + upperRoles);
            return userRepository.findByTeamAndRoleIn(teamId, upperRoles);

        } catch (Exception e) {
            logger.warning("Error fetching users by role: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private String getTokenFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warning("User not authenticated");
            throw new RuntimeException("User not authenticated");
        }

        // 🟢 Token ko log karo
        String token = (String) authentication.getCredentials();
        logger.info("Extracted Token: " + token);

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("JWT Token is null or empty");
        }

        return token;
    }


    // Helper method to get upper roles
    private List<Role> getUpperRoles(Role role) {
        switch (role) {
            case Team_Member:
                return Arrays.asList(Role.Team_Lead, Role.Manager, Role.Director);
            case Team_Lead:
                return Arrays.asList(Role.Manager, Role.Director);
            case Manager:
                return Collections.singletonList(Role.Director);
            case Director:
                return Collections.emptyList();
            default:
                return Collections.emptyList();
        }
    }






//    public User updateUser(Long userId, User updatedUser) {
//        User existingUser = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        existingUser.setFullname(updatedUser.getFullname());
//        existingUser.setRole(updatedUser.getRole());
//        existingUser.setReportsTo_Id(updatedUser.getReportsTo_Id());
//        existingUser.setTeam(updatedUser.getTeam());
//        existingUser.setMobile_No(updatedUser.getMobile_No());
//        existingUser.setLeaves_allowed(updatedUser.getLeaves_allowed());
//
//        return userRepository.save(existingUser);
//    }
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
        throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
    public User addUser(AddUserRequest request, String token) {
        String role = jwtUtil.extractRoleFromToken(token);

        // Only Manager or Director can add users
        if (!("Manager".equals(role) || "Director".equals(role))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only Managers and Directors can add users.");
        }

        // Manager-specific restriction: Cannot add Managers or Directors
        if ("Manager".equals(role) && (request.getRole() == Role.Manager || request.getRole() == Role.Director)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Managers can only add Team Members or Team Leads.");
        }

        // Director can add Manager, Team Lead, and Team Member
        // if ("Director".equals(role) && request.getRole() == Role.Director) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Directors cannot add other Directors.");

        //}

        // Generate user_Id manually
        Long maxUserId = userRepository.findMaxUserId();
        Long newUserId = maxUserId + 1;
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setUser_Id(newUserId);
        newUser.setFullname(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setRole(request.getRole());
        newUser.setReportsTo_Id(request.getReportsTo_Id());
        newUser.setPassword(hashedPassword);
        newUser.setTeam(request.getTeam());
        newUser.setMobile_No(request.getMobile_No());
        newUser.setLeavesAllowed(request.getLeavesAllowed());

        // Save user and return the created user
        return userRepository.save(newUser);
    }
    public List<UserWithReportsDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserWithReportsDTO)
                .collect(Collectors.toList());
    }

    private UserWithReportsDTO mapToUserWithReportsDTO(User user) {
        UserWithReportsDTO reportsTo = null;
        if (user.getReportsTo_Id() != null) {
            Optional<User> reportsToUser = userRepository.findById(user.getReportsTo_Id());
            reportsTo = reportsToUser.map(this::mapToUserWithReportsDTO).orElse(null);
        }
        return new UserWithReportsDTO(
                user.getUser_Id(),
                user.getFullname(),
                user.getEmail(),
                user.getRole(),
                reportsTo,
                user.getJoiningDate(),
                user.getPassword(),
                user.getTeam(),
                user.getMobile_No(),
                user.getLeavesAllowed()
        );
    }
    public User updateUser(Long userId, UpdateUserDTO request, String token) {
        String role = jwtUtil.extractRoleFromToken(token);

        // Only Manager or Director can update users
        if (!("Manager".equals(role) || "Director".equals(role))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only Managers and Directors can edit.");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Update allowed fields (userId cannot be changed)
        if (request.getFullName() != null) existingUser.setFullname(request.getFullName());
        if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
        if (request.getReportsTo_Id() != null) existingUser.setReportsTo_Id(request.getReportsTo_Id());
        if (request.getJoiningDate() != null) existingUser.setJoiningDate(request.getJoiningDate());
        if (request.getTeam() != null) existingUser.setTeam(request.getTeam());
        if (request.getMobileNo() != null) existingUser.setMobile_No(request.getMobileNo());
        if (request.getLeavesAllowed() != null) existingUser.setLeavesAllowed(request.getLeavesAllowed());
        if (request.getPassword() != null) existingUser.setPassword(request.getPassword());

        // Save and return updated user
        return userRepository.save(existingUser);
    }
    public void deleteUser(Long userId, String token) throws AccessDeniedException {
        String role = jwtUtil.extractRoleFromToken(token);

        // Only MANAGER or DIRECTOR can delete users
        if (!("Manager".equals(role) || "Director".equals(role))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only Managers and Directors can delete users");
        }

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Director can delete anyone
        if ("Director".equals(role)) {
            userRepository.delete(userToDelete);
            return;
        }

        // Manager-specific rules
        if ("Manager".equals(role)) {
            if ("Manager".equals(userToDelete.getRole()) || "Director".equals(userToDelete.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Managers can not delete other managers and directors");
            }

            if ("Team_Lead".equals(userToDelete.getRole()) || "Team_Member".equals(userToDelete.getRole())) {
                userRepository.delete(userToDelete);
                return;
            }
        }

        throw new AccessDeniedException("Invalid operation: " + role + " cannot delete " + userToDelete.getRole());
    }




}