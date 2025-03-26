package com.example.demo.converter;

import com.example.demo.Enum.LeaveType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LeaveTypeConverter implements AttributeConverter<LeaveType, String> {

    @Override
    public String convertToDatabaseColumn(LeaveType leaveType) {
        if (leaveType == null) return null;
        return switch (leaveType) {
            case MEDICAL -> "Medical";
            case CASUAL -> "Casual";
            case ANNUAL -> "Annual";
        };
    }

    @Override
    public LeaveType convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return switch (dbValue) {
            case "Medical" -> LeaveType.MEDICAL;
            case "Casual" -> LeaveType.CASUAL;
            case "Annual" -> LeaveType.ANNUAL;
            default -> throw new IllegalArgumentException("Unknown value: " + dbValue);
        };
    }
}
