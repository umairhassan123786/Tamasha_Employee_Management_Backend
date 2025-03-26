package com.example.demo.dto;
import com.example.demo.Enum.LeaveDays;
import com.example.demo.Enum.LeaveStatus;
import com.example.demo.Enum.LeaveType;
import com.example.demo.Enum.WhichHalf;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveDTO {

    private Long leaves_id;
    private LeaveDays days;
    private WhichHalf which_half;

    @JsonProperty("leaves_type")
    private LeaveType leave_type;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_date;

    private Long user_id;
    private LeaveStatus status;
    private String leave_description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime applied_Date;

    private String rejectionReason;
    private Integer responsible_PersonId;
    private Integer approved_by;

    // Getters and Setters
    public Long getLeaves_id() { return leaves_id; }
    public void setLeaves_id(Long leaves_id) { this.leaves_id = leaves_id; }

    public LeaveDays getDays() { return days; }
    public void setDays(LeaveDays days) { this.days = days; }

    public WhichHalf getWhich_half() { return which_half; }
    public void setWhich_half(WhichHalf which_half) { this.which_half = which_half; }

    public LeaveType getLeave_type() { return leave_type; }
    public void setLeave_type(LeaveType leave_type) { this.leave_type = leave_type; }

    public LocalDate getStart_date() { return start_date; }
    public void setStart_date(LocalDate start_date) { this.start_date = start_date; }

    public LocalDate getEnd_date() { return end_date; }
    public void setEnd_date(LocalDate end_date) { this.end_date = end_date; }

    public Long getUser_id() { return user_id; }
    public void setUser_id(Long user_id) { this.user_id = user_id; }

    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus status) { this.status = status; }

    public String getLeave_description() { return leave_description; }
    public void setLeave_description(String leave_description) { this.leave_description = leave_description; }

    public LocalDateTime getApplied_Date() { return applied_Date; }
    public void setApplied_Date(LocalDateTime applied_Date) { this.applied_Date = applied_Date; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public Integer getResponsible_PersonId() { return responsible_PersonId; }
    public void setResponsible_PersonId(Integer responsible_PersonId) { this.responsible_PersonId = responsible_PersonId; }

    public Integer getApproved_by() { return approved_by; }
    public void setApproved_by(Integer approved_by) { this.approved_by = approved_by; }
}