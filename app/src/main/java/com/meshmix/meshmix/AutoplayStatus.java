package com.meshmix.meshmix;

public class AutoplayStatus {
    static AutoplayStatusEnum currentStatus = AutoplayStatusEnum.AUTOPLAY_OFF;

    static AutoplayStatusEnum getStatus() {
        return currentStatus;
    }

    static void setStatus(AutoplayStatusEnum newStatus) {
        currentStatus = newStatus;
    }
}