package com.example.demo.controller;
import com.example.demo.Enum.LeaveStatus;
import com.example.demo.Models.Leave;
import com.example.demo.Service.LeaveService;
import com.example.demo.dto.LeaveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/add")
    public ResponseEntity<String> addLeave(@RequestBody LeaveDTO leaveDTO) {
        try {
            leaveService.addLeave(leaveDTO);
            return ResponseEntity.ok("Leave added successfully!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update/{leaveId}")
    public ResponseEntity<String> updateLeave(@PathVariable Long leaveId, @RequestBody LeaveDTO leaveDTO) {
        try {
            leaveService.updateLeave(leaveId, leaveDTO);
            return ResponseEntity.ok("Leave updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/leaves/myleaves")
    public ResponseEntity<?> getMyLeaves(
            @RequestParam(required = false) String leaveType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        System.out.println("Leave Type: " + leaveType);
        System.out.println("Status: " + status);

        // Call service with filters
        var leaves = leaveService.getFilteredLeaves(leaveType, status, startDate, endDate, page, size);

        return ResponseEntity.ok(leaves);
    }




    @DeleteMapping("/delete/{leaveId}")
    public ResponseEntity<String> deleteLeave(@PathVariable Long leaveId) {
        try {
            leaveService.deleteLeave(leaveId);
            return ResponseEntity.ok("Leave deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @PutMapping("/updateleave/{id}")
//    public ResponseEntity<String> updateLeaveById(@PathVariable Long id, @RequestBody LeaveDTO leaveDTO) {
//        try {
//            leaveService.editLeave(id, leaveDTO);
//            return ResponseEntity.ok("Leave updated successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @PatchMapping("/status/{leaveId}")
    public ResponseEntity<String> acceptOrRejectLeave(
            @PathVariable Long leaveId,
            @RequestParam LeaveStatus status,
            @RequestParam(required = false) String rejectionReason,
            @RequestParam(required = false) Integer approvedBy
    ) {
        try {
            leaveService.acceptOrRejectLeave(leaveId, status, rejectionReason, approvedBy);
            return ResponseEntity.ok("Leave " + status + " successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @GetMapping("/getleavebyid/{userId}")
//    public ResponseEntity<Page<Leave>> getLeavesByUserId(
//            @PathVariable Long userId,
//            @RequestParam(required = false) LeaveStatus status,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Page<Leave> leaves = leaveService.getLeavesByUserId(userId, status, PageRequest.of(page, size));
//        return ResponseEntity.ok(leaves);
//    }

//    @GetMapping("/reportees/{managerId}")
//    public ResponseEntity<Page<Leave>> getReporteeLeaves(
//            @PathVariable Long managerId,
//            @RequestParam(required = false) LeaveStatus status,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Page<Leave> leaves = leaveService.getReporteeLeaves(managerId, status, PageRequest.of(page, size));
//        return ResponseEntity.ok(leaves);
//    }

    @GetMapping("/getallleavesbyuser")
    public ResponseEntity<Page<Leave>> getAllLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Leave> leaves = leaveService.getAllLeaves(PageRequest.of(page, size));
        return ResponseEntity.ok(leaves);
    }
}