package com.example.demo.config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class LeavesAllowedConverter implements AttributeConverter<Map<String, Integer>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Integer> leavesAllowed) {
        if (leavesAllowed == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(leavesAllowed);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, Integer> convertToEntityAttribute(String leavesAllowedJson) {
        if (leavesAllowedJson == null || leavesAllowedJson.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(leavesAllowedJson, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to map", e);
        }
    }
}