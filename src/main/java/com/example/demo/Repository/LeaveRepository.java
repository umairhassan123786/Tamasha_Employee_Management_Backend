package com.example.demo.Repository;

import com.example.demo.Enum.LeaveStatus;
import com.example.demo.Enum.Role;
import com.example.demo.Models.Leave;
import com.example.demo.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    @Query("SELECT COALESCE(MAX(l.leaves_id), 0) FROM Leave l")
    Long findMaxLeaveId();
    // Get all leaves by User ID with status filter and pagination
//    Page<Leave> findByUser_UserIdAndStatusOrderByAppliedDateDesc(Long userId, LeaveStatus status, Pageable pageable);
//
//    // Get all leaves by User ID without status filter and pagination
//    Page<Leave> findByUser_UserIdOrderByAppliedDateDesc(Long userId, Pageable pageable);
    @Query("SELECT u FROM User u WHERE (:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))")
    Page<User> findAllWithFilter(@Param("fullName") String fullName, Pageable pageable);
    // Get leaves of reportees (direct and indirect reports)
//    @Query("SELECT l FROM Leave l WHERE l.user.reportsTo_Id = :managerId OR l.responsible_PersonId = :managerId ORDER BY l.applied_Date DESC")
//    Page<Leave> findReporteeLeavesByManagerId(@Param("reportsTo_Id") Long managerId, Pageable pageable);
//
//    // Get reportee leaves with status filter
//    @Query("SELECT l FROM Leave l WHERE (l.user.reportsTo_Id = :managerId OR l.responsible_PersonId = :managerId) AND l.status = :status ORDER BY l.applied_Date DESC")
//    Page<Leave> findReporteeLeavesByManagerIdAndStatus(@Param("reportsTo_Id") Long managerId, @Param("status") LeaveStatus status, Pageable pageable);

//    @Query("SELECT l FROM Leave l WHERE l.user.user_Id = :userId ORDER BY l.appliedDate DESC")
//    Page<Leave> findByUserId(@Param("userId") Long userId, Pageable pageable);
//
//    // Get leaves by userId and status, ordered by appliedDate in descending order
//    @Query("SELECT l FROM Leave l WHERE l.user.user_Id = :userId AND l.status = :status ORDER BY l.appliedDate DESC")
//    Page<Leave> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") LeaveStatus status, Pageable pageable);
    Page<Leave> findByUser(User user, Pageable pageable);

    Page<Leave> findByUserAndLeaveStatus(User user, LeaveStatus leaveStatus, Pageable pageable);



}
