package com.mqped.fims.model;

import org.junit.jupiter.api.Test;

import com.mqped.fims.model.enums.StatusType;

import static org.junit.jupiter.api.Assertions.*;

class StatusTypeTest {

    @Test
    void testEnumValuesCount() {
        StatusType[] values = StatusType.values();
        assertEquals(3, values.length, "StatusType should have exactly 3 values");
    }

    @Test
    void testEnumNames() {
        assertEquals("ON", StatusType.ON.name());
        assertEquals("CUT", StatusType.CUT.name());
        assertEquals("OFF", StatusType.OFF.name());
    }

    @Test
    void testValueOfValidNames() {
        assertEquals(StatusType.ON, StatusType.valueOf("ON"));
        assertEquals(StatusType.CUT, StatusType.valueOf("CUT"));
        assertEquals(StatusType.OFF, StatusType.valueOf("OFF"));
    }

    @Test
    void testEnumToString() {
        // By default, enums use their name() for toString()
        assertEquals("ON", StatusType.ON.toString());
        assertEquals("CUT", StatusType.CUT.toString());
        assertEquals("OFF", StatusType.OFF.toString());
    }
}
