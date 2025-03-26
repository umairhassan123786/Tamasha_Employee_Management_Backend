package com.example.demo.Enum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    Director("Director"),
    Manager("Manager"),
    Team_Lead("Team Lead"),
    Team_Member("Team Member");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.replace(" ", "").equalsIgnoreCase(value.replace(" ", "").trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}