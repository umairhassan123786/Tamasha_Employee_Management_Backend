package com.example.demo.Models;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int team_id;

    private String team_name;


    public int getTeamId() {
        return team_id;
    }

    @JsonProperty("team_name")
    public String getTeamName() {
        return team_name;
    }

}