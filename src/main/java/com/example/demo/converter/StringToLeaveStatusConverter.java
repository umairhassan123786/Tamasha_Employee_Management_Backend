package com.example.demo.converter;

import com.example.demo.Enum.LeaveStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLeaveStatusConverter implements Converter<String, LeaveStatus> {

    @Override
    public LeaveStatus convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return LeaveStatus.fromValue(source);
    }
}
