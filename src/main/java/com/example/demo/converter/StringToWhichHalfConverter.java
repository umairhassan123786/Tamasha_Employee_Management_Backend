package com.example.demo.converter;

import com.example.demo.Enum.WhichHalf;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToWhichHalfConverter implements Converter<String, WhichHalf> {

    @Override
    public WhichHalf convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return WhichHalf.fromDbValue(source);
    }
}
