package com.example.demo.Service;
import com.example.demo.Enum.LeaveStatus;
import com.example.demo.Enum.LeaveType;
import com.example.demo.Enum.Role;
import com.example.demo.Enum.WhichHalf;
import com.example.demo.Models.Leave;
import com.example.demo.Models.User;
import com.example.demo.Repository.LeaveRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.LeaveDTO;
import com.example.demo.util.JwtUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.criteria.Predicate;


@Service
public class LeaveService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveService.class);

    public LeaveService(LeaveRepository leaveRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EntityManager entityManager;
    public Leave editLeave(Long leaveId, LeaveDTO leaveDTO) {
        // Step 1: Check if leave exists
        Optional<Leave> leaveOptional = leaveRepository.findById(leaveId);
        if (leaveOptional.isEmpty()) {
            throw new RuntimeException("Leave not found with ID: " + leaveId);
        }
        Leave existingLeave = leaveOptional.get();

        // Step 2: Update fields only if provided (to avoid overwriting)
        if (leaveDTO.getDays() != null) {
            existingLeave.setDays(leaveDTO.getDays());
        }
        if (leaveDTO.getWhich_half() != null) {
            existingLeave.setWhich_half(leaveDTO.getWhich_half());
        }
        if (leaveDTO.getLeave_type() != null) {
            existingLeave.setLeaves_type(leaveDTO.getLeave_type());
        }
        if (leaveDTO.getStart_date() != null) {
            existingLeave.setStart_date(leaveDTO.getStart_date());
        }
        if (leaveDTO.getEnd_date() != null) {
            existingLeave.setEnd_date(leaveDTO.getEnd_date());
        }
        if (leaveDTO.getLeave_description() != null) {
            existingLeave.setLeave_description(leaveDTO.getLeave_description());
        }
        if (leaveDTO.getStatus() != null) {
            existingLeave.setStatus(leaveDTO.getStatus());
        }
        if (leaveDTO.getResponsible_PersonId() != null) {
            existingLeave.setResponsible_PersonId(leaveDTO.getResponsible_PersonId());
        }

        // Step 3: Save updated leave
        Leave updatedLeave = leaveRepository.save(existingLeave);
        logger.info("Leave updated: " + updatedLeave);
        return updatedLeave;
    }
//    public Leave acceptOrRejectLeave(Long leaveId, LeaveStatus status, String rejectionReason, Integer approvedBy) {
//        // Step 1: Find leave by ID
//        Optional<Leave> leaveOptional = leaveRepository.findById(leaveId);
//        if (leaveOptional.isEmpty()) {
//            throw new RuntimeException("Leave not found with ID: " + leaveId);
//        }
//        Leave leave = leaveOptional.get();
//
//        // Step 2: Update status and other fields
//        leave.setStatus(status);
//        if (status == LeaveStatus.REJECTED && rejectionReason != null) {
//            leave.setRejectionReason(rejectionReason);
//        }
//        if (approvedBy != null) {
//            leave.setApproved_by(approvedBy);
//        }
//
//        // Step 3: Save changes
//        Leave updatedLeave = leaveRepository.save(leave);
//        logger.info("Leave {} with ID: {}", status, leaveId);
//        return updatedLeave;
//    }
public void deleteLeave(Long leaveId) {
    // ✅ Get logged-in user from JWT
    String loggedInUsername = jwtUtil.getLoggedInUsername();
    User loggedInUser = userRepository.findByemail(loggedInUsername)
            .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

    // ✅ Fetch the leave to be deleted
    Leave existingLeave = leaveRepository.findById(leaveId)
            .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + leaveId));

    // ✅ Check if the logged-in user is the owner of the leave
    if (!existingLeave.getUser().getUser_Id().equals(loggedInUser.getUser_Id())) {
        throw new RuntimeException("Authorization Error: You can only delete your own leaves.");
    }

    // ✅ Directors cannot delete any leaves
    if (loggedInUser.getRole() == Role.Director) {
        throw new RuntimeException("Authorization Error: DIRECTOR cannot delete leaves.");
    }

//    // ✅ Check if leave can still be deleted (before the start date or till 11:59 PM the day before)
//    LocalDateTime now = LocalDateTime.now();
//    LocalDate today = now.toLocalDate();
//    LocalDate startDate = existingLeave.getStart_date().atStartOfDay().toLocalDate();
//
//    // If start date is in the past, throw error
//    if (today.isAfter(startDate)) {
//        throw new RuntimeException("Leave cannot be deleted after the start date.");
//    }
//
//    // If start date is tomorrow, check the time (can delete before midnight)
//    if (today.plusDays(1).isEqual(startDate) && now.getHour() >= 23 && now.getMinute() >= 59) {
//        throw new RuntimeException("Leave cannot be deleted after 11:59 PM the day before.");
//    }

    // ✅ Delete the leave
    leaveRepository.delete(existingLeave);
    logger.info("Leave deleted: " + leaveId);
}
    public Page<Leave> getUserLeaves(String status, int page, int size) {
        // ✅ Get logged-in user from JWT
        String loggedInUsername = jwtUtil.getLoggedInUsername();
        User loggedInUser = userRepository.findByemail(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        // ✅ Create pageable object (descending order)
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedDate").descending());

        // ✅ Fetch leaves by status
        if ("All".equalsIgnoreCase(status)) {
            return leaveRepository.findByUser(loggedInUser, pageable);
        } else {
            LeaveStatus leaveStatus = LeaveStatus.fromValue(status);
            return leaveRepository.findByUserAndLeaveStatus(loggedInUser, leaveStatus, pageable);
        }
    }
    public Page<Leave> getFilteredLeaves(String leaveType, String status, String startDate, String endDate, int page, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Leave> cq = cb.createQuery(Leave.class);
        Root<Leave> leaveRoot = cq.from(Leave.class);

        List<Predicate> predicates = new ArrayList<>();

        // Leave Type Filter (Enum)
        if (leaveType != null && !leaveType.isEmpty()) {
            predicates.add(cb.equal(leaveRoot.get("leaveType"), LeaveType.valueOf(leaveType.toUpperCase())));
        }

        // Status Filter (Enum)
        if (status != null && !status.isEmpty()) {
            predicates.add(cb.equal(leaveRoot.get("status"), LeaveStatus.valueOf(status.toUpperCase())));
        }

        // Start Date Filter
        if (startDate != null && !startDate.isEmpty()) {
            predicates.add(cb.greaterThanOrEqualTo(leaveRoot.get("startDate"), LocalDate.parse(startDate)));
        }

        // End Date Filter
        if (endDate != null && !endDate.isEmpty()) {
            predicates.add(cb.lessThanOrEqualTo(leaveRoot.get("endDate"), LocalDate.parse(endDate)));
        }

        // Apply Predicates
        cq.where(predicates.toArray(new Predicate[0]));

        // Pagination
        TypedQuery<Leave> query = entityManager.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<Leave> resultList = query.getResultList();
        long totalElements = resultList.size();  // Count bhi nikal sakte ho

        return new org.springframework.data.domain.PageImpl<>(resultList, PageRequest.of(page, size), totalElements);
    }

    public Page<Leave> getUserLeavesWithFilters(
            LeaveType leaveType,
            LeaveStatus status,
            LocalDate startDate,
            LocalDate endDate,
            WhichHalf whichHalf,
            String leaveDescription,
            String rejectionReason,
            Integer responsiblePersonId,
            Integer approvedBy,
            int page,
            int size) {

        Logger logger = LoggerFactory.getLogger(this.getClass());

        // Log input parameters
        logger.info("Fetching leaves with filters - leaveType: {}, status: {}, startDate: {}, endDate: {}, whichHalf: {}, leaveDescription: {}, rejectionReason: {}, responsiblePersonId: {}, approvedBy: {}, page: {}, size: {}",
                leaveType, status, startDate, endDate, whichHalf, leaveDescription, rejectionReason, responsiblePersonId, approvedBy, page, size);

        // ✅ Get logged-in user from JWT
        String loggedInUsername = jwtUtil.getLoggedInUsername();
        logger.info("Logged in user: {}", loggedInUsername);

        User loggedInUser = userRepository.findByemail(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        // ✅ Create CriteriaBuilder
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Leave> query = cb.createQuery(Leave.class);
        Root<Leave> root = query.from(Leave.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("user"), loggedInUser));

        if (leaveType != null) {
            predicates.add(cb.equal(root.get("leaves_type"), leaveType));
        }
        if (status != null) {
            predicates.add(cb.equal(root.get("leaveStatus"), status));
        }
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(root.get("start_date"), startDate, endDate));
        }
        if (whichHalf != null) {
            predicates.add(cb.equal(root.get("whichHalf"), whichHalf));
        }
        if (leaveDescription != null && !leaveDescription.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("leave_description")), "%" + leaveDescription.toLowerCase() + "%"));
        }
        if (rejectionReason != null && !rejectionReason.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("rejectionReason")), "%" + rejectionReason.toLowerCase() + "%"));
        }
        if (responsiblePersonId != null) {
            predicates.add(cb.equal(root.get("responsible_PersonId"), responsiblePersonId));
        }
        if (approvedBy != null) {
            predicates.add(cb.equal(root.get("approved_by"), approvedBy));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(root.get("appliedDate")));

        logger.info("Executing query with {} predicates", predicates.size());

        // ✅ Pagination
        TypedQuery<Leave> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        List<Leave> resultList = typedQuery.getResultList();
        logger.info("Fetched {} leaves", resultList.size());

        // ✅ Count query for total elements
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Leave> countRoot = countQuery.from(Leave.class);
        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();
        logger.info("Total leaves found: {}", totalElements);

        return new PageImpl<>(resultList, PageRequest.of(page, size), totalElements);
    }
    public Leave updateLeave(Long leaveId, LeaveDTO leaveDTO) {
        // ✅ Get logged-in user from JWT
        String loggedInUsername = jwtUtil.getLoggedInUsername();
        User loggedInUser = userRepository.findByemail(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        // ✅ Fetch existing leave
        Leave existingLeave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + leaveId));

        // ✅ Check if the logged-in user is the owner of the leave
        if (!existingLeave.getUser().getUser_Id().equals(loggedInUser.getUser_Id())) {
            throw new RuntimeException("Authorization Error: You can only update your own leaves.");
        }

        // ✅ Check if logged-in user is DIRECTOR (can't apply or update leaves)
        if (loggedInUser.getRole() == Role.Director) {
            throw new RuntimeException("Authorization Error: DIRECTOR cannot update leaves.");
        }

        // ✅ Update leave details
        existingLeave.setDays(leaveDTO.getDays());
        existingLeave.setWhich_half(leaveDTO.getWhich_half());
        existingLeave.setLeaves_type(leaveDTO.getLeave_type());
        existingLeave.setStart_date(leaveDTO.getStart_date());
        existingLeave.setEnd_date(leaveDTO.getEnd_date());
        existingLeave.setStatus(leaveDTO.getStatus());
        existingLeave.setLeave_description(leaveDTO.getLeave_description());
        existingLeave.setRejectionReason(leaveDTO.getRejectionReason());
        existingLeave.setResponsible_PersonId(leaveDTO.getResponsible_PersonId());
        existingLeave.setApproved_by(leaveDTO.getApproved_by());

        // ✅ Save updated leave
        Leave updatedLeave = leaveRepository.save(existingLeave);
        logger.info("Leave updated: " + updatedLeave);

        return updatedLeave;
    }

    public Leave acceptOrRejectLeave(Long leaveId, LeaveStatus status, String rejectionReason, Integer approvedBy) {
    // Step 1: Find leave by ID
    Optional<Leave> leaveOptional = leaveRepository.findById(leaveId);
    if (leaveOptional.isEmpty()) {
        throw new RuntimeException("Leave not found with ID: " + leaveId);
    }
    Leave leave = leaveOptional.get();
    User applicant = leave.getUser();

    // Step 2: Fetch the approving user (approvedBy)
    Optional<User> approverOptional = userRepository.findById(approvedBy.longValue());
    if (approverOptional.isEmpty()) {
        throw new RuntimeException("Approver not found with ID: " + approvedBy);
    }
    User approver = approverOptional.get();

    // Step 3: Enforce Approval Rules
    switch (applicant.getRole()) {
        case Team_Member:
            // Team Members' leave can be approved/rejected by TL, Manager, or Director only.
            if (approver.getRole() == Role.Team_Member) {
                throw new RuntimeException("Team Members cannot approve/reject leaves.");
            }
            break;

        case Team_Lead:
            // Team Leads' leave can only be approved/rejected by Manager or Director.
            if (approver.getRole() == Role.Team_Member || approver.getRole() == Role.Team_Lead) {
                throw new RuntimeException("Only Managers or Directors can approve/reject Team Lead leaves.");
            }
            break;

        case Manager:
            // Managers' leave can only be approved/rejected by Director.
            if (approver.getRole() != Role.Director) {
                throw new RuntimeException("Only Directors can approve/reject Manager leaves.");
            }
            break;

        case Director:
            // Directors cannot apply for leaves.
            throw new RuntimeException("Directors cannot apply for leaves.");
    }

    // Step 4: Update status and other fields
    leave.setStatus(status);
    if (status == LeaveStatus.REJECTED && rejectionReason != null) {
        leave.setRejectionReason(rejectionReason);
    }
    leave.setApproved_by(approvedBy);

    // Step 5: Save changes
    Leave updatedLeave = leaveRepository.save(leave);
    logger.info("Leave {} with ID: {}", status, leaveId);
    return updatedLeave;
}


    public Leave addLeave(LeaveDTO leaveDTO) {
        // ✅ Get logged-in user from JWT
        String loggedInUsername = jwtUtil.getLoggedInUsername();
        User loggedInUser = userRepository.findByemail(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        // ✅ Check if logged-in user is DIRECTOR
        if (loggedInUser.getRole() == Role.Director) {
            throw new RuntimeException("Authorization Error: DIRECTOR cannot apply for leave.");
        }

        // ✅ Now proceed with adding leave
        Long previousId = leaveRepository.findMaxLeaveId();
        Long newId = (previousId != null ? previousId : 0) + 1;
        Leave newLeave = new Leave();
        newLeave.setLeaves_id(newId);
        newLeave.setDays(leaveDTO.getDays());
        newLeave.setWhich_half(leaveDTO.getWhich_half());
        newLeave.setLeaves_type(leaveDTO.getLeave_type());
        newLeave.setStart_date(leaveDTO.getStart_date());
        newLeave.setEnd_date(leaveDTO.getEnd_date());
        newLeave.setUser(loggedInUser);  // ✅ Logged-in user is applying
        newLeave.setStatus(leaveDTO.getStatus());
        newLeave.setLeave_description(leaveDTO.getLeave_description());
        newLeave.setApplied_Date(LocalDateTime.now());
        newLeave.setRejectionReason(leaveDTO.getRejectionReason());
        newLeave.setResponsible_PersonId(leaveDTO.getResponsible_PersonId());
        newLeave.setApproved_by(leaveDTO.getApproved_by());

        // ✅ Save in DB
        Leave savedLeave = leaveRepository.save(newLeave);
        logger.info("Leave added: " + savedLeave);

        return savedLeave;
    }


    /**
     * Fetch leaves for reportees based on optional filters.
     */

//    public Page<Object[]> getAllUsersWithReports(String fullName, Pageable pageable) {
//       return userRepository.findAllUsersWithReports(fullName,  pageable);
//        //return userRepository.findAllUsersWithReports(fullName, pageable);
//    }

    public Page<Leave> getAllLeaves(Pageable pageable) {
        return leaveRepository.findAll(pageable);  // Correct method
    }

}