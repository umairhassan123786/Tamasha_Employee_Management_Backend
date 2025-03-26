package com.example.demo.Enum;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveDays {
    FULL_DAY("Full Day"),
    HALF_DAY("Half Day"),
    MULTIPLE_DAYS("Multiple Days");

    private final String value;

    LeaveDays(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LeaveDays fromValue(String value) {
        for (LeaveDays day : LeaveDays.values()) {
            if (day.value.equalsIgnoreCase(value)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid LeaveDays value: " + value);
    }
}

