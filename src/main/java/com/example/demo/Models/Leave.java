package com.example.demo.Models;
import com.example.demo.Enum.LeaveDays;
import com.example.demo.Enum.LeaveStatus;
import com.example.demo.Enum.LeaveType;
import com.example.demo.Enum.WhichHalf;
import com.example.demo.converter.LeaveStatusConverter;
import com.example.demo.converter.LeaveTypeConverter;
import com.example.demo.converter.WhichHalfConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaves")
public class Leave {

    @Id
    @Column(name = "leaves_id", nullable = false)
    private Long leaves_id;

    @Convert(converter = com.example.demo.converter.LeaveDaysConverter.class)
    @Column(name = "days")
    private LeaveDays days;

    @Convert(converter = WhichHalfConverter.class)
    @Column(name = "which_half")
    private WhichHalf whichHalf;

    @Convert(converter = LeaveTypeConverter.class)
    @Column(name = "leaves_type")
    private LeaveType leaves_type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;

    @ManyToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "user_Id", nullable = false)
    private User user;

    @Convert(converter = LeaveStatusConverter.class)
    @Column(name = "status")
    private LeaveStatus leaveStatus;

    private String leave_description;


   // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   @Column(name = "applied_Date")
   private LocalDateTime appliedDate;
  // ✅ camelCase


    private String rejectionReason;
    private Integer responsible_PersonId;
    private Integer approved_by;

    // ✅ Getters and Setters
    public Long getLeaves_id() {
        return leaves_id;
    }

    public void setLeaves_id(Long leaves_id) {
        this.leaves_id = leaves_id;
    }

    public LeaveDays getDays() {
        return days;
    }

    public void setDays(LeaveDays days) {
        this.days = days;
    }

    public WhichHalf getWhich_half() {
        return whichHalf;
    }

    public void setWhich_half(WhichHalf which_half) {
        this.whichHalf = which_half;
    }

    public LeaveType getLeaves_type() {
        return leaves_type;
    }

    public void setLeaves_type(LeaveType leaves_type) {
        this.leaves_type = leaves_type;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LeaveStatus getStatus() {
        return leaveStatus;
    }

    public void setStatus(LeaveStatus status) {
        this.leaveStatus = status;
    }

    public String getLeave_description() {
        return leave_description;
    }

    public void setLeave_description(String leave_description) {
        this.leave_description = leave_description;
    }

    public LocalDateTime getApplied_Date() {
        return appliedDate;
    }

    public void setApplied_Date(LocalDateTime applied_Date) {
        this.appliedDate = applied_Date;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Integer getResponsible_PersonId() {
        return responsible_PersonId;
    }

    public void setResponsible_PersonId(Integer responsible_PersonId) {
        this.responsible_PersonId = responsible_PersonId;
    }

    public Integer getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(Integer approved_by) {
        this.approved_by = approved_by;
    }
}