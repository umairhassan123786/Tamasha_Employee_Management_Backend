package com.example.demo.Enum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private final String value;

    LeaveStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LeaveStatus fromValue(String value) {
        for (LeaveStatus status : LeaveStatus.values()) {
            if (status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid LeaveStatus value: " + value);
    }
}