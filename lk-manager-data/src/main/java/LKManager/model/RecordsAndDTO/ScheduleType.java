package LKManager.model.RecordsAndDTO;

public enum ScheduleType{

    standardSchedule,
    swissSchedule ,
    oneDaySchedule ;
}

/*    private byte value;

    ScheduleType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static ScheduleType fromValue(byte value) {
        for (ScheduleType type : ScheduleType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}*/