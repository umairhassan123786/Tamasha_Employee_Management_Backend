package com.example.demo.converter;

import com.example.demo.Enum.WhichHalf;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WhichHalfConverter implements AttributeConverter<WhichHalf, String> {

    @Override
    public String convertToDatabaseColumn(WhichHalf whichHalf) {
        if (whichHalf == null) {
            return null;
        }
        System.out.println("Converting to DB: " + whichHalf.getDbValue());
        return whichHalf.getDbValue();
    }

    @Override
    public WhichHalf convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return WhichHalf.fromDbValue(dbData);
    }
}
