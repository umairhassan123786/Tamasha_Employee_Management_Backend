package com.example.demo.converter;
import com.example.demo.Enum.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {


    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return "Team Member";
        }
        // Convert Enum to DB format
        return switch (role) {
            case Director -> "Director";
            case Manager -> "Manager";
            case Team_Lead -> "Team Lead";
            case Team_Member -> "Team Member";
        };
    }


    @Override
    public Role convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        return switch (dbData) {
            case "Director" -> Role.Director;
            case "Manager" -> Role.Manager;
            case "Team Lead" -> Role.Team_Lead;
            case "Team Member" -> Role.Team_Member;
            default->Role.Team_Member;
        };
    }
}