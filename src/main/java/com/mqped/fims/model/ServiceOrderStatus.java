package com.mqped.fims.model;

public enum ServiceOrderStatus {
    CREATED,
    IN_EXECUTION,
    EXECUTED,
    CANCELED;

    public boolean isFinal() {
        return this == EXECUTED || this == CANCELED;
    }

    public boolean isActive() {
        return this == CREATED || this == IN_EXECUTION;
    }
}
