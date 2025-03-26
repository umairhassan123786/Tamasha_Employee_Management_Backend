package com.example.demo.converter;
import com.example.demo.Enum.LeaveStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LeaveStatusConverter implements AttributeConverter<LeaveStatus, String> {

    @Override
    public String convertToDatabaseColumn(LeaveStatus leaveStatus) {
        if (leaveStatus == null) return null;
        return leaveStatus.name();
    }

    @Override
    public LeaveStatus convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return LeaveStatus.valueOf(dbValue.toUpperCase());
    }
}