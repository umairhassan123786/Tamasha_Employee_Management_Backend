package com.example.demo.converter;

import com.example.demo.Enum.LeaveType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLeaveTypeConverter implements Converter<String, LeaveType> {

    @Override
    public LeaveType convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return LeaveType.fromValue(source);
    }
}
