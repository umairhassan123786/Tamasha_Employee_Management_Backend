package com.example.demo.Enum;

public enum WhichHalf {
    FIRST_HALF("First Half"),
    SECOND_HALF("Second Half"),
    NA("N/A");

    private final String dbValue;

    WhichHalf(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static WhichHalf fromDbValue(String dbValue) {
        for (WhichHalf whichHalf : WhichHalf.values()) {
            if (whichHalf.dbValue.equalsIgnoreCase(dbValue)) {
                return whichHalf;
            }
        }
        throw new IllegalArgumentException("Invalid WhichHalf value: " + dbValue);
    }
}
