package com.example.demo.controller;
import com.example.demo.Models.Team;
import com.example.demo.Repository.TeamRepository;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private final TeamRepository teamRepository;
    private final UserService userService;

    @Autowired
    public TeamController(TeamRepository teamRepository, UserService userService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
    }

    @GetMapping("/getAllTeams")
    public ResponseEntity<List<Team>> getAllUser() {
        List<Team> teams = teamRepository.findAll();
        return ResponseEntity.ok(teams);
    }
}