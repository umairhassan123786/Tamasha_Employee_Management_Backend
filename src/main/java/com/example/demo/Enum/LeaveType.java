package com.example.demo.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveType {
    MEDICAL("Medical"),
    CASUAL("Casual"),
    ANNUAL("Annual");

    private final String value;

    LeaveType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LeaveType fromValue(String value) {
        for (LeaveType type : LeaveType.values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid LeaveType value: " + value);
    }
}
