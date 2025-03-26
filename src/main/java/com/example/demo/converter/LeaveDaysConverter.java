package com.example.demo.converter;

import com.example.demo.Enum.LeaveDays;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LeaveDaysConverter implements AttributeConverter<LeaveDays, String> {

    @Override
    public String convertToDatabaseColumn(LeaveDays leaveDays) {
        if (leaveDays == null) return null;
        return switch (leaveDays) {
            case HALF_DAY -> "Half Day";
            case FULL_DAY -> "Full Day";
            case MULTIPLE_DAYS -> "Multiple Days";
        };
    }

    @Override
    public LeaveDays convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return switch (dbValue) {
            case "Half Day" -> LeaveDays.HALF_DAY;
            case "Full Day" -> LeaveDays.FULL_DAY;
            case "Multiple Days" -> LeaveDays.MULTIPLE_DAYS;
            default -> throw new IllegalArgumentException("Unknown value: " + dbValue);
        };
    }
}
