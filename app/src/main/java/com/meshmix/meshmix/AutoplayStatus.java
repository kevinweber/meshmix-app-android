package com.meshmix.meshmix;

public class AutoplayStatus {
    private static AutoplayStatusEnum currentStatus = AutoplayStatusEnum.AUTOPLAY_OFF;

    protected static AutoplayStatusEnum getStatus() {
        return currentStatus;
    }

    protected static void setStatus(AutoplayStatusEnum newStatus) {
        currentStatus = newStatus;
    }
}