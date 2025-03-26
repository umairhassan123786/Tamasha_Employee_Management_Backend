package com.example.demo.converter;

import com.example.demo.Enum.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return Role.fromValue(source);
    }
}
